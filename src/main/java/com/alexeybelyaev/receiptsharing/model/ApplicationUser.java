package com.alexeybelyaev.receiptsharing.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class ApplicationUser implements UserDetails {

//    // uuid could also prevent injection attacks
    private final UUID uuid;
    private final String username;
    private final String password;
    private final String email;
    private final Set<? extends GrantedAuthority> grantedAuthorities;
    private boolean isEnabled;

    public ApplicationUser(UUID uuid, String username,
                           String password, String email,
                           Set<? extends GrantedAuthority> grantedAuthorities,
                           boolean isEnabled) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.grantedAuthorities = grantedAuthorities;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String grantedAuthoritiesToString() {

        Set<String> auth= new HashSet<>();
        this.getGrantedAuthorities().stream().
                forEach(grantedAuthority -> auth.add(grantedAuthority.getAuthority()));
        String result = String.join(",",auth);
        return result;
    }
}
