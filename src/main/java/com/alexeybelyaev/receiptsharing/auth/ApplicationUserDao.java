package com.alexeybelyaev.receiptsharing.auth;

import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.validation.VerificationToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserDao {
     
     Optional<ApplicationUser> selectApplicationUserByUserName(String name);

     // COMMENT THIS !!!!!!!!!
     int insertApplicationUser(UUID id, ApplicationUser user);

     default int insertApplicationUser(ApplicationUser user){
          UUID id = UUID.randomUUID();
          return insertApplicationUser(id,user);
     }

     Optional<ApplicationUser> selectApplicationUserByEmail(String email);

     List<ApplicationUser> selectAllApplicationUsers();

     Optional<ApplicationUser> selectApplicationUserById(UUID uid);

     int updateApplicationUser(ApplicationUser user);

     int deleteApplicationUser(UUID uid);

     int saveVerificationToken(VerificationToken verificationToken);

     Optional<VerificationToken> getVerificationToken(UUID token);

     int updateVerificationToken(VerificationToken newVerificationToken);
}
