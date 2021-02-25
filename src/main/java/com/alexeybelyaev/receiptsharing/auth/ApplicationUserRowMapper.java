package com.alexeybelyaev.receiptsharing.auth;

import com.alexeybelyaev.receiptsharing.security.ApplicationUserPermission;
import com.alexeybelyaev.receiptsharing.security.ApplicationUserRole;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ApplicationUserRowMapper implements RowMapper<ApplicationUser> {


    @Override
    public ApplicationUser mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return
                new ApplicationUser(
                        UUID.fromString(resultSet.getString("app_user_id")),
                        resultSet.getString("nick_name"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        parseGrantedAuthorities(resultSet.getString("granted_authorities")),
                        resultSet.getBoolean("is_enabled"));
    }

    private Set<?extends GrantedAuthority> parseGrantedAuthorities(String string){

        Set<SimpleGrantedAuthority> grantedAuthoritySet = new HashSet<SimpleGrantedAuthority>();

        if (string!=null){

            String[] authorities = string.split(",");

            for (String auth : authorities) {
                //get only roles
                if (auth.startsWith("ROLE")){
                    grantedAuthoritySet.addAll(ApplicationUserRole.valueOf(auth.replaceFirst("ROLE_","")).grantedAuthoritySet());
                }

            }
        }

        return grantedAuthoritySet;
    }
}
