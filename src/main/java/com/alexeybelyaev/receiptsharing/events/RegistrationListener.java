package com.alexeybelyaev.receiptsharing.events;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUser;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnCompleteRegistrationEvent> {

    @Autowired
    ApplicationUserService applicationUserService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    JavaMailSender mailSender;

    @Value("${hostname}")
    private String hostname;

    @Override
    public void onApplicationEvent(OnCompleteRegistrationEvent event) {
        this.confirmRegistration(event);
    }


    private void confirmRegistration(OnCompleteRegistrationEvent event) {
        ApplicationUser user = event.getUser();
        UUID token = UUID.randomUUID();
        applicationUserService.createVerificationToken(token, user);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("message.regSuccess", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://"+hostname + confirmationUrl);
        mailSender.send(email);
    }


}
