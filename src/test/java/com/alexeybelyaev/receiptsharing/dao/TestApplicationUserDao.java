package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUser;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.security.ApplicationUserRole;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class TestApplicationUserDao {

    @Autowired
    private ApplicationUserDao applicationUserDao;


    @Test
    @Transactional
    @Rollback
    public void testInsertApplicationUser() {
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Alex", "all", "bel.alex@mail.ru", ApplicationUserRole.ADMIN.grantedAuthoritySet(), true);

        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        Optional<ApplicationUser> testedUser = applicationUserDao.selectApplicationUserByUserName("Alex");

        Assert.assertTrue(testedUser.isPresent());
        Assert.assertEquals(uid.toString(), testedUser.get().getUuid().toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testSelectApplicationUserByEmail() {
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Alex", "all", "bel.alex@mail.ru", ApplicationUserRole.ADMIN.grantedAuthoritySet(), true);

        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        Optional<ApplicationUser> testedUser = applicationUserDao.selectApplicationUserByEmail("bel.alex@mail.ru");

        Assert.assertTrue(testedUser.isPresent());
        Assert.assertEquals(uid.toString(), testedUser.get().getUuid().toString());
    }


    @Test
    @Transactional
    @Rollback
    public void testUpdateUser(){

        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@mail.ru",
                ApplicationUserRole.USER.grantedAuthoritySet(), false);

        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        ApplicationUser userToUpdate = new ApplicationUser(uid, "Test1","test1","test1@mail.ru",
                ApplicationUserRole.ADMIN.grantedAuthoritySet(),true);

        int resultUpdate = applicationUserDao.updateApplicationUser(userToUpdate);
        Assert.assertEquals(1, resultUpdate);
        Optional<ApplicationUser> testedUserOptional = applicationUserDao.selectApplicationUserById(uid);

        Assert.assertTrue(testedUserOptional.isPresent());

        ApplicationUser testedUser = testedUserOptional.orElseThrow();

        Assert.assertEquals(userToUpdate.getUsername(), testedUser.getUsername());
        Assert.assertEquals(userToUpdate.getEmail(), testedUser.getEmail());
        Assert.assertEquals(userToUpdate.getPassword(), testedUser.getPassword());
        Assert.assertEquals(userToUpdate.getGrantedAuthorities(), testedUser.getGrantedAuthorities());
        Assert.assertEquals(userToUpdate.isEnabled(),testedUser.isEnabled());

    }




}
