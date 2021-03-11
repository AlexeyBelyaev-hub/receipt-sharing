package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptDao  {

     //insert new receipt and receipt items
     int insert(Receipt receipt);

     //update receipt and receipt items
     int update(Receipt receipt);

     //delete receipt and receipt items
     int delete(UUID uuid);

     //get receipt with list of receipt items
     Optional<Receipt> getReceiptById(UUID uid);

     //get receipts with EMPTY list of receipt items for app user
     List<Receipt> getUserReceipts(ApplicationUser applicationUser);

     //get receipt with list of receipt items
     List<ReceiptItem> getReceiptItems(Receipt receipt);

}
