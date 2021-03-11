package com.alexeybelyaev.receiptsharing.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class Receipt {

    private String uuid;
    private LocalDateTime receiptDateTime;

    private ApplicationUser applicationUser;

    private String receiptNumber;
    private String company;
    private List<ReceiptItem> items= Collections.EMPTY_LIST;;


    public Receipt() {

    }

    public Receipt(String uuid, LocalDateTime receiptDateTime, ApplicationUser applicationUser, String receiptNumber, String company) {
        this.uuid = uuid;
        this.receiptDateTime = receiptDateTime;
        this.applicationUser = applicationUser;
        this.receiptNumber = receiptNumber;
        this.company = company;

    }
}
