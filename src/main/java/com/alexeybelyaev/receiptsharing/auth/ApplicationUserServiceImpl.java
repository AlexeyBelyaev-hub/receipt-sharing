package com.alexeybelyaev.receiptsharing.auth;


import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.security.ApplicationUserRole;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserAlreadyExistException;
import com.alexeybelyaev.receiptsharing.validation.VerificationToken;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserNotFoundException;
import com.alexeybelyaev.receiptsharing.web.dto.ApplicationUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final ApplicationUserDao applicationUserDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    MessageSource messageSource;

    @Autowired
    public ApplicationUserServiceImpl(@Qualifier("postgresUser") ApplicationUserDao applicationUserDao, PasswordEncoder passwordEncoder) {
        this.applicationUserDao = applicationUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    // needs for spring boot auth
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            ApplicationUser user = applicationUserDao.selectApplicationUserByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(String.format("Username %s not found",email)));
            return user;
        } catch (Exception exception){
            throw new RuntimeException(exception);
        }


    }


    @Override
    public ApplicationUser registerNewUser(ApplicationUserDto userDto) throws AppUserAlreadyExistException{

        //Check if email or username exist
        Optional<ApplicationUser> applicationUserByEmail = applicationUserDao.selectApplicationUserByEmail(userDto.getEmail());

        Optional<ApplicationUser> applicationUserByUserName = applicationUserDao.selectApplicationUserByUserName(userDto.getUsername());
        if (applicationUserByEmail.isPresent()||applicationUserByUserName.isPresent()){
            throw new AppUserAlreadyExistException(
                    messageSource.getMessage(
                            "message.userExist",
                            new Object[]{userDto.getEmail()},
                            LocaleContextHolder.getLocale())
            );

        }

        UUID uuid =UUID.randomUUID();
        ApplicationUser  appUserToRegister = new ApplicationUser(uuid,
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail(),
                ApplicationUserRole.USER.grantedAuthoritySet(),
                false);

        applicationUserDao.insertApplicationUser(uuid, appUserToRegister);
        return appUserToRegister;

    }


    // expiry time = 60*24 minutes
    @Override
    public int createVerificationToken(UUID token, ApplicationUser user){

        return createVerificationToken(token, user, 60*24);
    }

    @Override
    public int createVerificationToken(UUID token, ApplicationUser user, long expiryTimeInMinutes){
        VerificationToken verificationToken =  new VerificationToken();
        verificationToken.setToken(token.toString());
        verificationToken.setUser(user);
        verificationToken.setExpiryDateTime(VerificationToken.calculateExpiryDate(expiryTimeInMinutes));

        return applicationUserDao.saveVerificationToken(verificationToken);
    }


    // method checkVerificationToken should return:
    //-1 token not found
    //0 token found but expired
    //1 token found and not expired
    @Override
    public int checkVerificationToken(UUID token) {

        Optional<VerificationToken> verificationToken = applicationUserDao.getVerificationToken(token);

        if (verificationToken.isEmpty()) {
            return -1;
        } else if (verificationToken.get().getExpiryDateTime().isBefore(LocalDateTime.now())) {
            return 0;
        }

        return 1;
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(UUID token){
        return applicationUserDao.getVerificationToken(token);
    }

    @Override
    public void enableUser(ApplicationUser user){

        user.setEnabled(true);
        log.debug("ApplicationUserService: enable user. UID = {}, name:{}. Set enable={}",user.getUuid(),user.getUsername(),user.isEnabled());
        applicationUserDao.updateApplicationUser(user);
    }

    @Override
    public void lockUser(ApplicationUser user){
        user.setEnabled(false);
        applicationUserDao.updateApplicationUser(user);
    }

    @Override
    public Optional<VerificationToken> generateNewToken(String token) {
        Optional<VerificationToken> verificationTokenOptional
                = applicationUserDao.getVerificationToken(UUID.fromString(token));
        VerificationToken verificationToken
                = verificationTokenOptional.orElseThrow(
                        ()->new AppUserNotFoundException("User not found"));

        VerificationToken newVerificationToken =  new VerificationToken();
        newVerificationToken.setToken(UUID.randomUUID().toString());
        newVerificationToken.setUser(verificationToken.getUser());
        newVerificationToken.setExpiryDateTime(VerificationToken.calculateExpiryDate(30));
        int result = applicationUserDao.updateVerificationToken(newVerificationToken);
        if (result == 1){
            return Optional.of(newVerificationToken);
        } else {
            return Optional.ofNullable(null);
        }
    }

}
