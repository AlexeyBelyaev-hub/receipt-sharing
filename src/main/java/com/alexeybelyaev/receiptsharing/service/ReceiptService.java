package com.alexeybelyaev.receiptsharing.service;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.auth.ApplicationUserService;
import com.alexeybelyaev.receiptsharing.dao.ReceiptDao;
import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import com.alexeybelyaev.receiptsharing.web.dto.ApplicationUserDto;
import com.alexeybelyaev.receiptsharing.web.dto.ReceiptDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReceiptService {

    private final ReceiptDao receiptDao;
    private final ApplicationUserService applicationUserService;
    @Autowired
    private ApplicationUserDao applicationUserDao;

    @Autowired
    public ReceiptService(ReceiptDao receiptDao, ApplicationUserService applicationUserService) {
        this.receiptDao = receiptDao;
        this.applicationUserService = applicationUserService;
    }

    public Receipt getTestReceipt(){
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(1, "Amstel",1.5,5,7.5));
        items.add(new ReceiptItem(2, "Roasted Chicken",2,2,4));
        items.add(new ReceiptItem(3, "Burger",7,3,21));
        items.add(new ReceiptItem(4, "Fries",3.6,3,10.8));
        Receipt receipt = new Receipt();
        receipt.setReceiptDateTime(LocalDateTime.now());
        receipt.setUuid(UUID.randomUUID().toString());
        receipt.setReceiptNumber("1234");
        receipt.setCompany("Bar");
        receipt.setItems(items);
        return receipt;
    }

    public Optional<Receipt> saveReceipt(ReceiptDto receiptDto, String userName) {
        return Optional.ofNullable(null);
    }

    public List<ReceiptDto> getUserReceipts(String name) {
        Optional<ApplicationUser> user = applicationUserDao.selectApplicationUserByUserName(name);
        List<Receipt> receipts = receiptDao.getUserReceipts(user.orElseThrow());
        List<ReceiptDto> receiptDtos = Collections.EMPTY_LIST;
        for (Receipt receipt:receipts) {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setUuid(receipt.getUuid());
            receiptDto.setCompany(receipt.getCompany());
            receiptDto.setReceiptNumber(receipt.getReceiptNumber());
            receiptDto.setReceiptDateTime(receipt.getReceiptDateTime());
            receiptDtos.add(receiptDto);

        }
        return receiptDtos;
    }
}
