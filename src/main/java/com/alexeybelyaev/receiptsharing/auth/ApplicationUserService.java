package com.alexeybelyaev.receiptsharing.auth;

import com.alexeybelyaev.receiptsharing.exceptions.AppUserAlreadyExistException;
import com.alexeybelyaev.receiptsharing.validation.VerificationToken;
import com.alexeybelyaev.receiptsharing.web.dto.ApplicationUserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserService extends UserDetailsService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    ApplicationUser registerNewUser(ApplicationUserDto userDto) throws AppUserAlreadyExistException;
     int createVerificationToken(UUID token, ApplicationUser user);

     int createVerificationToken(UUID token, ApplicationUser user, long expiryTimeInMinutes);


    // method checkVerificationToken should return:
    //-1 token not found
    //0 token found but expired
    //1 token found and not expired
     int checkVerificationToken(UUID token);

    Optional<VerificationToken> getVerificationToken(UUID token);

    void enableUser(ApplicationUser user);
    void lockUser(ApplicationUser user);

    Optional<VerificationToken> generateNewToken(String token);
}
