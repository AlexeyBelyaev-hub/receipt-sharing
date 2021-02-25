package com.alexeybelyaev.receiptsharing.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.thymeleaf.expression.Sets;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alexeybelyaev.receiptsharing.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(new HashSet<ApplicationUserPermission>
                (List.of(PERSON_READ, PERSON_WRITE))),
    ADMIN_TRAINEE(new HashSet<ApplicationUserPermission>
            (List.of(PERSON_READ))),

    USER(Collections.EMPTY_SET);

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> grantedAuthoritySet(){
        Set<SimpleGrantedAuthority> grantedAuthority =
                permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        grantedAuthority.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return grantedAuthority;
    }

}
