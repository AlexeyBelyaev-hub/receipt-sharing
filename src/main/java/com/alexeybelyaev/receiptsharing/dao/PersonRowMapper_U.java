package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PersonRowMapper_U implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet resultSet, int rownum) throws SQLException {
        Person person =
                new Person(
                        UUID.fromString(resultSet.getString("person_id")),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"));
        return person;
    }
}
