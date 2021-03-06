package com.alexeybelyaev.receiptsharing.web.dto;

import com.alexeybelyaev.receiptsharing.model.ReceiptItem;

import java.util.List;

public class ReceiptDto {

    private String receiptNumber;
    private String company;
    private List<ReceiptItem> items;


}
