package com.alexeybelyaev.receiptsharing.model;

import lombok.Data;

@Data
public class ReceiptItem {

    private int rowNumber;
    private String title;
    private double price;
    private int quantity;
    private double sum;

    public ReceiptItem(int rowNumber, String title, double price, int quantity, double sum) {
        this.rowNumber = rowNumber;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.sum = sum;
    }
}
