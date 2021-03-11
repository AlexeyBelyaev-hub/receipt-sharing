package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import com.alexeybelyaev.receiptsharing.security.ApplicationUserRole;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class ReceiptDaoImplTest {

    @Autowired
    private ReceiptDao receiptDao;

    @Autowired
    private ApplicationUserDao applicationUserDao;

    @Test
    @Transactional
    @Rollback
    public void insert() {

        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Amstel",1.5,5,7.5));
        items.add(new ReceiptItem(2, "Roasted Chicken",2,2,4));
        items.add(new ReceiptItem(3, "Burger",7,3,21));
        items.add(new ReceiptItem(4, "Fries",3.6,3,10.8));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        receipt.setItems(items);
        receiptDao.insert(receipt);
        Assert.assertEquals(1, result);

        Optional<Receipt> found = receiptDao.getReceiptById(receiptUUID);
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getUuid(),receipt.getUuid());

        List<ReceiptItem> itemsTest = receiptDao.getReceiptItems(receipt);

        Assert.assertTrue(itemsTest.contains(items.get(0)));
        Assert.assertTrue(itemsTest.contains(items.get(1)));
        Assert.assertTrue(itemsTest.contains(items.get(2)));
        Assert.assertTrue(itemsTest.contains(items.get(3)));

    }

    @Test
    @Transactional
    @Rollback
    public void update() {

        ApplicationUser user = getTestUser();

        int result = applicationUserDao.insertApplicationUser(user.getUuid(), user);
        Assert.assertEquals(1, result);


        Receipt receipt = getTestReceipt(user);
        Receipt receipt1 = getTestReceipt1(user);

        result = receiptDao.insert(receipt);
        Assert.assertEquals(1, result);
        result = receiptDao.insert(receipt1);
        Assert.assertEquals(1, result);

        Receipt receiptToUpdate = getTestReceipt2(user);
        receiptToUpdate.setUuid(receipt1.getUuid());
        receiptDao.update(receiptToUpdate);

        Optional<Receipt> resultedReceiptOpt = receiptDao.getReceiptById(UUID.fromString(receipt1.getUuid()));

        Assert.assertTrue(resultedReceiptOpt.isPresent());
        Receipt resultedReceipt = resultedReceiptOpt.get();

        Assert.assertEquals(resultedReceipt.getUuid(),receipt1.getUuid());
        Assert.assertEquals(receiptToUpdate.getCompany(),resultedReceipt.getCompany());
        Assert.assertEquals(receiptToUpdate.getReceiptDateTime(),resultedReceipt.getReceiptDateTime());
        Assert.assertEquals(receiptToUpdate.getReceiptNumber(),resultedReceipt.getReceiptNumber());

        List<ReceiptItem> itemsTest=resultedReceipt.getItems();
        List<ReceiptItem> items=receiptToUpdate.getItems();

        Assert.assertEquals(itemsTest.size(),items.size());

        for (int i =0 ; i<items.size();i++){
            Assert.assertTrue(itemsTest.contains(items.get(i)));
        }

    }

    @Test
    @Transactional
    @Rollback
    public void delete() {
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Amstel",1.5,5,7.5));
        items.add(new ReceiptItem(2, "Roasted Chicken",2,2,4));
        items.add(new ReceiptItem(3, "Burger",7,3,21));
        items.add(new ReceiptItem(4, "Fries",3.6,3,10.8));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        receipt.setItems(items);
        receiptDao.insert(receipt);
        Assert.assertEquals(1, result);

        int delete = receiptDao.delete(receiptUUID);
        Assert.assertEquals(1,delete);

        Optional<Receipt> found = receiptDao.getReceiptById(receiptUUID);
        Assert.assertTrue(found.isEmpty());

        List<ReceiptItem> itemsTest = receiptDao.getReceiptItems(receipt);

        Assert.assertFalse(itemsTest.contains(items.get(0)));
        Assert.assertFalse(itemsTest.contains(items.get(1)));
        Assert.assertFalse(itemsTest.contains(items.get(2)));
        Assert.assertFalse(itemsTest.contains(items.get(3)));
    }

    @Test
    @Transactional
    @Rollback
    public void getUserReceipts() {

        ApplicationUser user = getTestUser();
        ApplicationUser user1 = getTestUser1();

        int result = applicationUserDao.insertApplicationUser(user.getUuid(), user);
        Assert.assertEquals(1, result);
        result = applicationUserDao.insertApplicationUser(user1.getUuid(), user1);
        Assert.assertEquals(1, result);

        Receipt receipt = getTestReceipt(user);
        Receipt receipt1 = getTestReceipt1(user);
        Receipt receipt2 = getTestReceipt2(user1);


        result = receiptDao.insert(receipt);
        Assert.assertEquals(1, result);
        result = receiptDao.insert(receipt1);
        Assert.assertEquals(1, result);
        result = receiptDao.insert(receipt2);
        Assert.assertEquals(1, result);

        List<Receipt> receipts = receiptDao.getUserReceipts(user);
        Assert.assertEquals(2,receipts.size());
        for (Receipt rcp:receipts) {
            Assert.assertTrue(rcp.getUuid().equals(receipt.getUuid())
                    ||rcp.getUuid().equals(receipt1.getUuid()));
        }

    }

    // --- TEST DATA GENERATOR METHODS ----

    private ApplicationUser getTestUser(){
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);

        return user;
    }

    private ApplicationUser getTestUser1(){
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test1", "test1", "test1@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        return user;
    }

    private Receipt getTestReceipt(ApplicationUser user){
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Amstel",1.5,5,7.5));
        items.add(new ReceiptItem(2, "Roasted Chicken",2,2,4));
        items.add(new ReceiptItem(3, "Burger",7,3,21));
        items.add(new ReceiptItem(4, "Fries",3.6,3,10.8));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        receipt.setItems(items);
        return receipt;
    }

    private Receipt getTestReceipt1(ApplicationUser user){
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Big mac",2,3,6));
        items.add(new ReceiptItem(2, "Fries",3,2,6));
        items.add(new ReceiptItem(3, "Iced Coffee",4,2,8));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("4321");
        receipt.setCompany("MacDonalds");
        receipt.setApplicationUser(user);
        receipt.setItems(items);
        return receipt;
    }

    private Receipt getTestReceipt2(ApplicationUser user){
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Борщ",150,3,450));
        items.add(new ReceiptItem(2, "Хлеб",30,1,30));
        items.add(new ReceiptItem(3, "Шашлык кур",250,1,250));
        items.add(new ReceiptItem(4, "Пельмени",190,2,380));
        items.add(new ReceiptItem(5, "Морс",50,3,150));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1");
        receipt.setCompany("Хачапури");
        receipt.setApplicationUser(user);
        receipt.setItems(items);
        return receipt;
    }


}