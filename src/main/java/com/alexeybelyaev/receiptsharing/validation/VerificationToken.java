package com.alexeybelyaev.receiptsharing.validation;

import com.alexeybelyaev.receiptsharing.auth.ApplicationUser;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VerificationToken {

    private String token;

    private ApplicationUser user;

    private LocalDateTime expiryDateTime;

    public static LocalDateTime calculateExpiryDate(long expiryTimeInMinutes) {

        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }
}
