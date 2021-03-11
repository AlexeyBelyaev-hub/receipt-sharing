package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.exceptions.AppUserNotFoundException;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ReceiptItemRowMapper implements RowMapper<ReceiptItem> {
    @Override
    public ReceiptItem mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new ReceiptItem(
                resultSet.getInt("item_id"),
                resultSet.getString("title"),
                resultSet.getDouble("price"),
                resultSet.getInt("quantity"),
                resultSet.getDouble("sum")
        );
    }
}
