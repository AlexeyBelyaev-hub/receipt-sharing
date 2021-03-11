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
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class ReceiptItemDaoImplTest {

    @Autowired
    private ReceiptItemDao receiptItemDao;

    @Autowired
    private ReceiptDao receiptDao;

    @Autowired
    private ApplicationUserDao applicationUserDao;

    @Test
    @Transactional
    @Rollback
    public void save() {

        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        ReceiptItem item1 = new ReceiptItem(1, "Amstel",1.5,5,7.5);
        ReceiptItem item2 = new ReceiptItem(2, "Roasted Chicken",2,2,4);
        ReceiptItem item3 =new ReceiptItem(3, "Burger",7,3,21);
        ReceiptItem item4 = new ReceiptItem(4, "Fries",3.6,3,10.8);
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        result = receiptDao.insert(receipt);
        Assert.assertEquals(1, result);

        int result1 = receiptItemDao.save(receiptUUID,item1);
        Assert.assertEquals(1, result1);
        int result2 = receiptItemDao.save(receiptUUID,item2);
        Assert.assertEquals(1, result2);
        int result3 = receiptItemDao.save(receiptUUID,item3);
        Assert.assertEquals(1, result3);
        int result4 = receiptItemDao.save(receiptUUID,item4);
        Assert.assertEquals(1, result4);

        Optional<Receipt> found = receiptDao.getReceiptById(receiptUUID);
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getUuid(),receipt.getUuid());

        List<ReceiptItem> items = receiptItemDao.getReceiptItemsByReceiptId(receiptUUID);
        Assert.assertTrue(items.contains(item1));
        Assert.assertTrue(items.contains(item2));
        Assert.assertTrue(items.contains(item3));
        Assert.assertTrue(items.contains(item4));

    }


    @Test
    @Transactional
    @Rollback
    public void delete() {

        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        ReceiptItem item1 = new ReceiptItem(1, "Amstel",1.5,5,7.5);
        ReceiptItem item2 = new ReceiptItem(2, "Roasted Chicken",2,2,4);
        ReceiptItem item3 =new ReceiptItem(3, "Burger",7,3,21);
        ReceiptItem item4 = new ReceiptItem(4, "Fries",3.6,3,10.8);
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        result = receiptDao.insert(receipt);
        Assert.assertEquals(1, result);

        int result1 = receiptItemDao.save(receiptUUID,item1);
        Assert.assertEquals(1, result1);
        int result2 = receiptItemDao.save(receiptUUID,item2);
        Assert.assertEquals(1, result2);
        int result3 = receiptItemDao.save(receiptUUID,item3);
        Assert.assertEquals(1, result3);
        int result4 = receiptItemDao.save(receiptUUID,item4);
        Assert.assertEquals(1, result4);

        int deleteItem2 = receiptItemDao.delete(receiptUUID,item2);
        Assert.assertEquals(1,deleteItem2);
//
//        Optional<Receipt> found = receiptDao.getReceiptById(receiptUUID);
//        Assert.assertTrue(found.isEmpty());

        List<ReceiptItem> items = receiptItemDao.getReceiptItemsByReceiptId(receiptUUID);
        Assert.assertTrue(items.contains(item1));
        Assert.assertFalse(items.contains(item2));
        Assert.assertTrue(items.contains(item3));
        Assert.assertTrue(items.contains(item4));

    }


    @Test
    @Transactional
    @Rollback
    void deleteAll() {
        UUID uid = UUID.randomUUID();
        ApplicationUser user = new ApplicationUser(uid, "Test", "test", "test@test.ru", ApplicationUserRole.USER.grantedAuthoritySet(), true);
        int result = applicationUserDao.insertApplicationUser(uid, user);
        Assert.assertEquals(1, result);

        ReceiptItem item1 = new ReceiptItem(1, "Amstel",1.5,5,7.5);
        ReceiptItem item2 = new ReceiptItem(2, "Roasted Chicken",2,2,4);
        ReceiptItem item3 =new ReceiptItem(3, "Burger",7,3,21);
        ReceiptItem item4 = new ReceiptItem(4, "Fries",3.6,3,10.8);
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        UUID receiptUUID = UUID.randomUUID();
        receipt.setUuid(receiptUUID.toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setApplicationUser(user);
        result = receiptDao.insert(receipt);
        Assert.assertEquals(1, result);

        int result1 = receiptItemDao.save(receiptUUID,item1);
        Assert.assertEquals(1, result1);
        int result2 = receiptItemDao.save(receiptUUID,item2);
        Assert.assertEquals(1, result2);
        int result3 = receiptItemDao.save(receiptUUID,item3);
        Assert.assertEquals(1, result3);
        int result4 = receiptItemDao.save(receiptUUID,item4);
        Assert.assertEquals(1, result4);

        int deleteItem2 = receiptItemDao.deleteAll(receiptUUID);
        Assert.assertEquals(4,deleteItem2);
//
//        Optional<Receipt> found = receiptDao.getReceiptById(receiptUUID);
//        Assert.assertTrue(found.isEmpty());

        List<ReceiptItem> items = receiptItemDao.getReceiptItemsByReceiptId(receiptUUID);
        Assert.assertFalse(items.contains(item1));
        Assert.assertFalse(items.contains(item2));
        Assert.assertFalse(items.contains(item3));
        Assert.assertFalse(items.contains(item4));

    }
}