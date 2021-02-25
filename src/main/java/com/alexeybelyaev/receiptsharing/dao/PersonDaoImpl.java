package com.alexeybelyaev.receiptsharing.dao;

import com.alexeybelyaev.receiptsharing.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository("postgres")
public class PersonDaoImpl implements PersonDao {

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    @Autowired
    public PersonDaoImpl(NamedParameterJdbcTemplate namedParamJdbcTemplate) {
        this.namedParamJdbcTemplate = namedParamJdbcTemplate;
    }

    //person DTO (data transfer object)
    @Override
    public int insertPerson(UUID id, Person person) {
        String sqlQuery = "INSERT INTO person (person_id, name, email, phone_number)" +
                "VALUES(:id,:name,:email,:phoneNumber)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name",person.getName())
                .addValue("email",person.getEmail())
                .addValue("phoneNumber",person.getPhoneNumber())
                .addValue("id",id);

        int result=-1;

        //catching any violation of data constraints: uuid - pk, email - check,
        //Spring wrap checked exceptions in unchecked
        // to make it possible to handle exception on application layer that makes app
        // more independent of dao implementation.
        // GET Exception handling OUT of Dao
        try {
            result=namedParamJdbcTemplate.update(sqlQuery,params);
        }catch (Exception exception){
            log.error(exception.getMessage());
        }
        return result;
    }

    @Override
    public List<Person> selectAllPersons() {
        final String query = "SELECT person_id, name, email, phone_number FROM person";
        List<Person> personList = namedParamJdbcTemplate.query(query,new PersonRowMapper_U());
        return personList;
    }

    @Override
    public Optional<Person> getPersonById(UUID uid) {

        SqlParameterSource params = new MapSqlParameterSource("uid",uid);
        String sqlQuery = "SELECT person_id, name, email, phone_number" +
                " FROM person WHERE person_id = :uid";
        Optional<Person> optionalPerson;
        // catching
        try {
            Person person = namedParamJdbcTemplate.queryForObject(sqlQuery,params, new PersonRowMapper_U());
            optionalPerson=Optional.of(person);
        }catch (EmptyResultDataAccessException exception){
            log.error(exception.getMessage());
            optionalPerson=Optional.empty();
        }
        return optionalPerson;
    }

    @Override
    public int updatePerson(UUID uid, Person person) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name",person.getName())
                .addValue("email",person.getEmail())
                .addValue("phoneNumber",person.getPhoneNumber())
                .addValue("uid",uid);

        String sqlQuery = "UPDATE person " +
                "SET name = :name, email = :email, phone_number = :phoneNumber " +
                " WHERE person_id = :uid";
        Object[] args = new Object[]{person.getName(),person.getEmail(),person.getPhoneNumber(),uid};
        int result = -1;
        result = namedParamJdbcTemplate.update(sqlQuery,params);

        return result;

    }

    @Override
    public int deletePerson(UUID uid) {
        SqlParameterSource params = new MapSqlParameterSource("uid",uid);
        String sqlQuery = "DELETE FROM person WHERE person_id = :uid";
        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public int addContact(UUID user_uid, UUID person_uid){
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("appUserId",user_uid);
        params.addValue("personId",user_uid);

        String sqlQuery  = "INSERT INTO contacts (app_user_id, person_id) "+
             "VALUES (:appUserId,:personId)";
        return namedParamJdbcTemplate.update(sqlQuery,params);

    }

    @Override
    public List<Person> getAllContactsByUserID(UUID user_uuid) {
        SqlParameterSource params = new MapSqlParameterSource("appUserId",user_uuid);
        String sqlQuery = "SELECT person.person_id, person.name, person.email, person.phone_number, person.app_user_id " +
                "FROM contacts " +
                "INNER JOIN person ON person.person_id=contacts.person_id WHERE contacts.app_user_id = :appUserId";

        return namedParamJdbcTemplate.query(sqlQuery,params, new PersonRowMapper_U());
    }

}
