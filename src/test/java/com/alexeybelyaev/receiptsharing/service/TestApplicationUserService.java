package com.alexeybelyaev.receiptsharing.service;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserService;
import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.security.ApplicationUserRole;
import com.alexeybelyaev.receiptsharing.validation.VerificationToken;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class TestApplicationUserService {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private ApplicationUserDao applicationUserDao;

    // method checkVerificationToken should return:
    //-1 token not found
    //0 token found but expired
    //1 token found and not expired

    //Test createVerificationToken and checkVerificationToken
    // valid token(1 token found and not expired)
    @Test
    @Transactional
    @Rollback
    public void testCreateAndCheckValidVerificationToken() {

        // 1. create new user
        UUID user_uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(user_uid, "Test", "test", "test.test@mail.ru", ApplicationUserRole.USER.grantedAuthoritySet(), false);

        applicationUserDao.insertApplicationUser(user_uid, user);

        // 2. create and test valid token
        UUID token_uid = UUID.randomUUID();
        int insertTokenResult = applicationUserService.createVerificationToken(token_uid,user,60);
        Assert.assertEquals(1, insertTokenResult);


        int result = applicationUserService.checkVerificationToken(token_uid);
        Assert.assertEquals(1,result);
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateAndCheckExpiredVerificationToken() {

        // 1. create new user
        UUID user_uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(user_uid, "Test", "test", "test.test@mail.ru", ApplicationUserRole.USER.grantedAuthoritySet(), false);

        applicationUserDao.insertApplicationUser(user_uid, user);

        // 2. create and test expired token
        UUID token_uid = UUID.randomUUID();
        int insertTokenResult = applicationUserService.createVerificationToken(token_uid,user,0);
        Assert.assertEquals(1, insertTokenResult);

        int result = applicationUserService.checkVerificationToken(token_uid);
        Assert.assertEquals(0,result);
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateAndCheckNotFoundVerificationToken() {

        int result = applicationUserService.checkVerificationToken(UUID.randomUUID());
        Assert.assertEquals(-1,result);
    }

    @Test
    @Transactional
    @Rollback
    public void testGenerateNewToken(){
        // 1. create new user
        UUID user_uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(user_uid, "Test", "test", "test.test@mail.ru", ApplicationUserRole.USER.grantedAuthoritySet(), false);

        applicationUserDao.insertApplicationUser(user_uid, user);

        // 2. create and test expired token
        UUID token_uid = UUID.randomUUID();
        applicationUserService.createVerificationToken(token_uid,user,0);

        int result = applicationUserService.checkVerificationToken(token_uid);
        Assert.assertEquals(0 , result);

        Optional<VerificationToken> verificationTokenOptional
                    = applicationUserService.generateNewToken(token_uid.toString());

        if (verificationTokenOptional.isEmpty()){
            Assert.assertTrue(false);
        }else{
           VerificationToken newVerificationToken = verificationTokenOptional.get();
            result = applicationUserService.checkVerificationToken(
                    UUID.fromString(newVerificationToken.getToken()) );
            Assert.assertEquals(1, result);
        }



    }

}
