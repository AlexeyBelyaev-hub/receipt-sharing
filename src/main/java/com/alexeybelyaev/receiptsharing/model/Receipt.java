package com.alexeybelyaev.receiptsharing.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Receipt {
    private ZonedDateTime timeStamp;
    private String uuid;
    private String receiptNumber;
    private String company;
    private List<ReceiptItem> items;

    public Receipt(ZonedDateTime timeStamp, String uuid, String receiptNumber, String company) {
        this.timeStamp = timeStamp;
        this.uuid = uuid;
        this.receiptNumber = receiptNumber;
        this.company = company;
    }


}
