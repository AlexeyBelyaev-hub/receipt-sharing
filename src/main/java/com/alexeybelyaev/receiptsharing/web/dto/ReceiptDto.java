package com.alexeybelyaev.receiptsharing.web.dto;

import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class ReceiptDto {

    private String uuid;
    private LocalDateTime receiptDateTime;
    private String receiptNumber;
    private String company;
    private List<ReceiptItem> items;


}
