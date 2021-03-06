package com.alexeybelyaev.receiptsharing.web.controller;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUser;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserService;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserServiceImpl;
import com.alexeybelyaev.receiptsharing.events.OnCompleteRegistrationEvent;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserAlreadyExistException;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserUpdateException;
import com.alexeybelyaev.receiptsharing.exceptions.GeneralResponse;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.service.ReceiptService;
import com.alexeybelyaev.receiptsharing.validation.VerificationToken;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserNotFoundException;
import com.alexeybelyaev.receiptsharing.web.dto.ApplicationUserDto;
import com.alexeybelyaev.receiptsharing.model.Person;
import com.alexeybelyaev.receiptsharing.service.PersonService;
import com.alexeybelyaev.receiptsharing.web.dto.CaptchaResponseDto;
import com.alexeybelyaev.receiptsharing.web.dto.ReceiptDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.thymeleaf.context.LazyContextVariable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/")
public class TemplateController {
    private static final String CAPTCHA_URL="https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final PersonService personService;
    private final ApplicationUserService applicationUserService;
    private final ReceiptService receiptService;

    @Value("${recaptcha.secret}")
    private String secretKey;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageSource messages;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    public TemplateController(PersonService personService,
                              ApplicationUserServiceImpl applicationUserService,
                              ReceiptService receiptService) {
        this.personService = personService;
        this.applicationUserService = applicationUserService;
        this.receiptService = receiptService;
    }

    @GetMapping("login")
    public String getLoginView(WebRequest request,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "message", required = false) String message){

        return "login";
    }

    @GetMapping("persons")
    public String getPersons(Model model){
        model.addAttribute("persons", new LazyContextVariable<List<Person>>() {
            @Override
            protected List<Person> loadValue() {
                return personService.getAllPersons();
            }
        });
        return "persons";
    }

    @GetMapping("/user/registration")
    public String getRegistrationForm(Model model){
        ApplicationUserDto applicationUserDto = new ApplicationUserDto();
        model.addAttribute("user",applicationUserDto);
        return "registration";
    }

    @PostMapping("/user/registration")
    public String registerApplicationUser(
            @ModelAttribute("user") @Valid ApplicationUserDto applicationUserDto,
            BindingResult bindingResult, //must be right after validated object
            @RequestParam("g-recaptcha-response") String captchaResponse,
            HttpServletRequest request, Model model, Locale locale){

        String url = String.format(CAPTCHA_URL, secretKey,captchaResponse);
        CaptchaResponseDto responseDto = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!responseDto.isSuccess()){
            bindingResult.addError(new ObjectError("captchaError",
                    messages.getMessage("message.fillCaptcha", null,
                            locale)));
        }

        if (bindingResult.hasErrors()){
            return "registration";
        }

        try{
             ApplicationUser registeredUser
                     = applicationUserService.registerNewUser(applicationUserDto);
             eventPublisher.publishEvent(new OnCompleteRegistrationEvent(registeredUser,
                     request.getLocale(), request.getContextPath()));
        }catch (AppUserAlreadyExistException exception){
            bindingResult.addError(new ObjectError("UserExistError",
                   exception.getMessage()));

            return "registration";
        } catch ( RuntimeException ex) {
            return "login?error="+ex.getMessage();
        }
        return "successRegistration";
    }


    @GetMapping("/registrationConfirm.html")
    public String confirmRegistration
            (WebRequest request, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        Locale locale = request.getLocale();

        //Check token: -1 token not found, 0 token found but expired, 1 token found and not expired
        int checkResult= applicationUserService.checkVerificationToken(UUID.fromString(token));
        log.debug("CheckResult = {}",checkResult);
        switch (checkResult) {
            case -1:
                String message = messages.getMessage("auth.message.invalidToken", null, locale);
                redirectAttributes.addAttribute("message", message);
                redirectAttributes.addAttribute("token", token);
                return "redirect:/badUser.html?lang=" + locale.getLanguage();

            case 0:
                message = messages.getMessage("auth.message.expired", null, locale);
                redirectAttributes.addAttribute("message", message);
                redirectAttributes.addAttribute("expired", true);
                redirectAttributes.addAttribute("token", token);
                return "redirect:/badUser.html?lang=" + locale.getLanguage();

            default:

                Optional<VerificationToken> verificationTokenOptional = applicationUserService.getVerificationToken(UUID.fromString(token));
                VerificationToken verificationToken =
                        verificationTokenOptional.orElseThrow(
                                ()->new AppUserNotFoundException("User for token: "+token+" not found")
                        );
                ApplicationUser user = verificationToken.getUser();
                applicationUserService.enableUser(user);
                return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        }
    }

    @GetMapping("/badUser.html")
    public String getBadUserForm(WebRequest request, Model model){
        log.debug("Model attribute expired = {}",model.getAttribute("expired"));
        log.debug("Model attribute expired = {}",model.getAttribute("token"));

        return "badUser";
    }

    @GetMapping("/user/resendRegistrationToken")
    public GeneralResponse resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String token) {


        Optional<VerificationToken> newTokenOptional = applicationUserService.generateNewToken(token);
        VerificationToken newToken = newTokenOptional
                .orElseThrow(
                        ()->new AppUserUpdateException(
                                messages.getMessage("message.tokenNotUpdated", null, request.getLocale())
                ));
        // generate link;
        String appUrl =  "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        SimpleMailMessage mailMessage = constructConfirmationEmail(appUrl, newToken, request.getLocale());

        mailSender.send(mailMessage);
        return new GeneralResponse(
                messages.getMessage("message.resentLink", mailMessage.getTo(), request.getLocale()),
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
    }

    public SimpleMailMessage constructConfirmationEmail(String appUrl, VerificationToken token, Locale locale){
        String confirmationLink = appUrl
                + "/registrationConfirm.html?token="+token.getToken();
        String message =  messages.getMessage("message.resentConfirmationLink", null, locale);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(token.getUser().getEmail());
        mailMessage.setSubject("Confirm registration");
        mailMessage.setText(message+ " rn"+confirmationLink);
        return mailMessage;

    }


    //  -- RECEIPT CONTROLLER --
    @GetMapping("receipt")
    public String getReceiptView(HttpServletRequest request, Model model,
                                 @RequestParam(value = "trybeta", required = false) Boolean trybeta ) {

        if (trybeta!=null && trybeta==true){
            model.addAttribute("receipt", new LazyContextVariable<Receipt>() {
                @Override
                protected Receipt loadValue() {
                    return receiptService.getTestReceipt();
                }
            });
        }

        return "receipt.html";
    }


    @PostMapping("saveReceipt")
    public String saveReceipt(@ModelAttribute("receipt") @Valid ReceiptDto receiptDto){



    }
}
