package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.exceptions.AppUserNotFoundException;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReceiptRowMapper implements RowMapper<Receipt> {


    private final ApplicationUserDao applicationUserDao;

    public ReceiptRowMapper(ApplicationUserDao applicationUserDao) {
        this.applicationUserDao = applicationUserDao;
    }

    @Override
    public Receipt mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Receipt receipt = new Receipt(
                resultSet.getString("receipt_id"),
                resultSet.getObject("date_time", LocalDateTime.class),
                applicationUserDao.selectApplicationUserById(
                        UUID.fromString(resultSet.getString("owner_user_id")))
                        .orElseThrow(()->new AppUserNotFoundException("User not found while row mapping receipt entity")),
                resultSet.getString("receipt_number"),
                resultSet.getString("seller")
                );
        return receipt;
    }
}
