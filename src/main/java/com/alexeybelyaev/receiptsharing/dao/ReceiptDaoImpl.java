package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUserDao;
import com.alexeybelyaev.receiptsharing.model.ApplicationUser;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.model.ReceiptItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository("receipt")
public class ReceiptDaoImpl implements ReceiptDao {

    private final NamedParameterJdbcTemplate namedParamJdbcTemplate;

    private final ReceiptItemDao receiptItemDao;

    private ApplicationUserDao applicationUserDao;

    @Autowired
    public ReceiptDaoImpl(NamedParameterJdbcTemplate namedParamJdbcTemplate, ReceiptItemDao receiptItemDao, ApplicationUserDao applicationUserDao) {
        this.namedParamJdbcTemplate = namedParamJdbcTemplate;
        this.receiptItemDao = receiptItemDao;
        this.applicationUserDao = applicationUserDao;
    }

    @Transactional
    @Override
    public int insert(Receipt receipt) {
        String sqlQuery = "INSERT INTO receipt (receipt_id, date_time, receipt_number, seller, owner_user_id)" +
                "VALUES(:id,:dateTime,:receiptNumber,:company,:appUserId)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id",UUID.fromString(receipt.getUuid()))
                .addValue("dateTime",receipt.getReceiptDateTime())
                .addValue("receiptNumber",receipt.getReceiptNumber())
                .addValue("company",receipt.getCompany())
                .addValue("appUserId",receipt.getApplicationUser().getUuid());
        int result=-1;


        //try {
            result=namedParamJdbcTemplate.update(sqlQuery,params);
            for (ReceiptItem receiptItem:receipt.getItems()) {
                receiptItemDao.save(UUID.fromString(receipt.getUuid()), receiptItem);
            }
      //  }catch (Exception exception){
       //     log.error(exception.getMessage());
       // }
        return result;
    }

    @Transactional
    @Override
    public int update(Receipt receipt) {

        String sqlQuery = "UPDATE receipt " +
                "SET  date_time=:dateTime, receipt_number = :receiptNumber, seller = :company, owner_user_id = :appUserId" +
                " WHERE receipt_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id",UUID.fromString(receipt.getUuid()))
                .addValue("dateTime",receipt.getReceiptDateTime())
                .addValue("receiptNumber",receipt.getReceiptNumber())
                .addValue("company",receipt.getCompany())
                .addValue("appUserId",receipt.getApplicationUser().getUuid());

        int result=-1;

        try {
            result=namedParamJdbcTemplate.update(sqlQuery,params);
            receiptItemDao.deleteAll(UUID.fromString(receipt.getUuid()));
            for (ReceiptItem receiptItem:receipt.getItems()) {
                receiptItemDao.save(UUID.fromString(receipt.getUuid()), receiptItem);
            }
        }catch (Exception exception){
            log.error(exception.getMessage());
        }
        return result;
    }

    @Override
    public int delete(UUID uuid) {

        SqlParameterSource params = new MapSqlParameterSource("id",uuid);
        String sqlQuery = "DELETE FROM receipt WHERE receipt_id = :id";

        int result=-1;

        try {
            receiptItemDao.deleteAll(uuid);
            result=namedParamJdbcTemplate.update(sqlQuery,params);
        }catch (Exception exception){
            log.error(exception.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Receipt> getReceiptById(UUID uuid) {
        String sqlQuery = "SELECT * FROM receipt WHERE receipt_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id",uuid);
        List<Receipt> receipts= namedParamJdbcTemplate.query(sqlQuery,params, new ReceiptRowMapper(applicationUserDao));

        Optional<Receipt> result;
        if (receipts.isEmpty()){
            result=Optional.ofNullable(null);
        }else{
            Receipt receipt = receipts.get(0);
            receipt.setItems(receiptItemDao.getReceiptItemsByReceiptId(UUID.fromString(receipt.getUuid())));
            result = Optional.of(receipt);
        }
        return result;
    }

    @Override
    public List<Receipt> getUserReceipts(ApplicationUser applicationUser) {
        String sqlQuery = "SELECT * FROM receipt WHERE owner_user_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id",applicationUser.getUuid());

        List<Receipt> receipts= namedParamJdbcTemplate.query(sqlQuery,params, new ReceiptRowMapper(applicationUserDao));
        return receipts;
    }

    @Override
    public List<ReceiptItem> getReceiptItems(Receipt receipt){
        return receiptItemDao.getReceiptItemsByReceiptId(UUID.fromString(receipt.getUuid()));
    }
}
