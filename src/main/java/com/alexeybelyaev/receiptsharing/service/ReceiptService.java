package com.alexeybelyaev.receiptsharing.service;

import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReceiptService {
    public Receipt getTestReceipt(){
        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem("Amstel",1.5,5,7.5));
        items.add(new ReceiptItem("Roasted Chicken",2,2,4));
        items.add(new ReceiptItem("Burger",7,3,21));
        items.add(new ReceiptItem("Fries",3.6,3,10.8));
        Receipt receipt = new Receipt(ZonedDateTime.now(), UUID.randomUUID().toString(),"1234","Bar");
        receipt.setItems(items);
        return receipt;
    }
}
