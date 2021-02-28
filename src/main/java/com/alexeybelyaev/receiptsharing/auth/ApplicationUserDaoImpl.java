package com.alexeybelyaev.receiptsharing.auth;

import com.alexeybelyaev.receiptsharing.validation.VerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.alexeybelyaev.receiptsharing.security.ApplicationUserRole.*;

@Slf4j
@Repository("postgresUser")
public class ApplicationUserDaoImpl implements ApplicationUserDao {

    private NamedParameterJdbcTemplate namedParamJdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserDaoImpl(NamedParameterJdbcTemplate namedParamJdbcTemplate, PasswordEncoder passwordEncoder) {
        this.namedParamJdbcTemplate = namedParamJdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUserName(String name) {
        String sqlQuery = "SELECT * FROM app_user WHERE LOWER(nick_name) = LOWER(:appUserName)";

        MapSqlParameterSource params = new MapSqlParameterSource("appUserName",name);
        List<ApplicationUser> users = namedParamJdbcTemplate.query(sqlQuery,params,new ApplicationUserRowMapper());
        Optional<ApplicationUser> user = Optional.ofNullable(users.isEmpty()?null:users.get(0));
        return user;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByEmail(String email) {
        String sqlQuery = "SELECT * FROM app_user WHERE LOWER(email) = LOWER(:email)";
        MapSqlParameterSource params = new MapSqlParameterSource("email",email);
        List<ApplicationUser> users = namedParamJdbcTemplate.query(sqlQuery,params, new ApplicationUserRowMapper());
        Optional<ApplicationUser> user = Optional.ofNullable(users.isEmpty()?null:users.get(0));
        return user;
    }

    @Override
    public int insertApplicationUser(UUID id, ApplicationUser user) {
        String sqlQuery = "INSERT INTO app_user (app_user_id, nick_name, email, password, is_enabled, granted_authorities)" +
                "VALUES(:id, :name, :email, :password, :isEnabled, :grantedAuthorities)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name",user.getUsername())
                .addValue("email",user.getEmail())
                .addValue("password",user.getPassword())
                .addValue("grantedAuthorities", user.grantedAuthoritiesToString())
                .addValue("isEnabled",user.isEnabled())
                .addValue("id",id);

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public List<ApplicationUser> selectAllApplicationUsers() {
        return null;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserById(UUID uid) {
        String sqlQuery = "SELECT * FROM app_user WHERE app_user_id = :uid";
        MapSqlParameterSource params = new MapSqlParameterSource("uid",uid);
        List<ApplicationUser> users = namedParamJdbcTemplate.query(sqlQuery,params, new ApplicationUserRowMapper());
        return Optional.ofNullable(users.isEmpty()?null:users.get(0));
    }

    @Override
    public int updateApplicationUser(ApplicationUser user) {
        String sqlQuery = "UPDATE app_user " +
                "SET nick_name=:username, email=:email, password = :password, is_enabled = :enabled, granted_authorities = :grantedAuthorities" +
                " WHERE app_user_id = :uuid";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username",user.getUsername())
                .addValue("email",user.getEmail())
                .addValue("password",user.getPassword())
                .addValue("grantedAuthorities", user.grantedAuthoritiesToString())
                .addValue("enabled",user.isEnabled())
                .addValue("uuid",user.getUuid());
        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    @Override
    public int deleteApplicationUser(UUID uid) {
        return 0;
    }

    @Override
    public int saveVerificationToken(VerificationToken token){

        String sqlQuery = "INSERT INTO token (token, app_user_uuid, expiry_date) " +
                "VALUES(:token, :user, :expiryDateTime)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("token", UUID.fromString(token.getToken()));
        params.addValue("user",token.getUser().getUuid());
        params.addValue("expiryDateTime",token.getExpiryDateTime());

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }

    // method checkVerificationToken should return:
    //-1 token not found
    //0 token found but expired
    //1 token found and not expired
    @Override
    public Optional<VerificationToken> getVerificationToken(UUID token) {
        String sqlQuery = "SELECT token, app_user_uuid, expiry_date FROM token WHERE token = :uid";
        MapSqlParameterSource params = new MapSqlParameterSource("uid", token);

        List<VerificationToken> verificationTokens =
                namedParamJdbcTemplate.query(sqlQuery, params, new RowMapper<VerificationToken>() {
                    @Override
                    public VerificationToken mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        VerificationToken vt = new VerificationToken();
                        vt.setToken(resultSet.getString("token"));
                        vt.setUser(selectApplicationUserById(UUID.fromString(resultSet.getString("app_user_uuid"))).orElseThrow());
                        vt.setExpiryDateTime(resultSet.getObject("expiry_date", LocalDateTime.class));
                        return vt;
                    }
                });

       return Optional.ofNullable(verificationTokens.isEmpty()?null:verificationTokens.get(0));
    }

    @Override
    public int updateVerificationToken(VerificationToken newVerificationToken) {
        String sqlQuery = "UPDATE token" +
                " SET token = :newToken, expiry_date = :newExpiryDate" +
                " WHERE app_user_uuid = :userUid";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userUid", newVerificationToken.getUser().getUuid());
        params.addValue("newToken", UUID.fromString(newVerificationToken.getToken()));
        params.addValue("newExpiryDate", newVerificationToken.getExpiryDateTime());

        return namedParamJdbcTemplate.update(sqlQuery,params);
    }


    // end of body
}
