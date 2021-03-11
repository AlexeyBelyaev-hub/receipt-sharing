package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository("receiptItem")
public class ReceiptItemDaoImpl implements ReceiptItemDao {


    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Autowired
    public ReceiptItemDaoImpl(NamedParameterJdbcTemplate namedParamJdbcTemplate) {
        this.namedParamJdbcTemplate = namedParamJdbcTemplate;
    }


    @Override
    public int save(UUID receiptId, ReceiptItem receiptItem) {
        String sqlQuery = "INSERT INTO receipt_items (receipt_id, item_id, title, quantity, price, sum)" +
                "VALUES(:receiptId, :itemId, :title, :quantity, :price, :sum)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("receiptId",receiptId)
                .addValue("itemId",receiptItem.getRowNumber())
                .addValue("title",receiptItem.getTitle())
                .addValue("quantity",receiptItem.getQuantity())
                .addValue("price",receiptItem.getPrice())
                .addValue("sum",receiptItem.getSum());

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public int delete(UUID receiptId, ReceiptItem receiptItem) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "DELETE FROM receipt_items WHERE receipt_id = :receiptId AND item_id = :itemId ";
        params.addValue("receiptId",receiptId);
        params.addValue("itemId",receiptItem.getRowNumber());

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public int deleteAll(UUID receiptId){
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "DELETE FROM receipt_items WHERE receipt_id = :receiptId";
        params.addValue("receiptId",receiptId);

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public List<ReceiptItem> getReceiptItemsByReceiptId(UUID receiptId) {
        String sqlQuery = "SELECT * FROM receipt_items WHERE receipt_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id",receiptId);
        List<ReceiptItem> receipts= namedParamJdbcTemplate.query(sqlQuery,params, new ReceiptItemRowMapper());
        return receipts;
    }
}
