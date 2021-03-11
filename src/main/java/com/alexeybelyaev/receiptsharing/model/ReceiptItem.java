package com.alexeybelyaev.receiptsharing.model;

import lombok.Data;

import java.util.Objects;

@Data
public class ReceiptItem {

    private int rowNumber;
    private String title;
    private double price;
    private int quantity;
    private double sum;

    public ReceiptItem() {
    }

    public ReceiptItem(int rowNumber, String title, double price, int quantity, double sum) {
        this.rowNumber = rowNumber;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptItem)) return false;
        ReceiptItem item = (ReceiptItem) o;
        return rowNumber == item.rowNumber &&
                Double.compare(item.price, price) == 0 &&
                quantity == item.quantity &&
                Double.compare(item.sum, sum) == 0 &&
                Objects.equals(title, item.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNumber, title, price, quantity, sum);
    }
}
