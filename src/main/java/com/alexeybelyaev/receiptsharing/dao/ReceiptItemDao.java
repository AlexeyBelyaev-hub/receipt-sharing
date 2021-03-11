package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceiptItemDao {

     //save item by fields: receiptId and receipt_item_id(row number)
     int save(UUID receiptId, ReceiptItem receiptItem);

     //delete item by fields: receiptId and receipt_item_id(row number)
     int delete(UUID receiptId, ReceiptItem receiptItem);

     //delete all items by field receiptId
     int deleteAll(UUID receiptId);

     List<ReceiptItem> getReceiptItemsByReceiptId(UUID receiptId);

}
