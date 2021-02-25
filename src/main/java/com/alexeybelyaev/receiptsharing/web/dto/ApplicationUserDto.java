package com.alexeybelyaev.receiptsharing.web.dto;

import com.alexeybelyaev.receiptsharing.validation.PasswordConfirmed;
import com.alexeybelyaev.receiptsharing.validation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordConfirmed
public class ApplicationUserDto {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String confirmPassword;

    @NotNull
    @NotEmpty
    @ValidEmail
    private String email;

}
