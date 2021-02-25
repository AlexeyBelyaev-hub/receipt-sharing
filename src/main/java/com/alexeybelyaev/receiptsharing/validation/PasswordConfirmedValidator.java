package com.alexeybelyaev.receiptsharing.validation;

import com.alexeybelyaev.receiptsharing.web.dto.ApplicationUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//Custom validator to check if password has been confirmed by user.
//Check if to passwords matches otherwise got message defined in interface PasswordConfirmed
public class PasswordConfirmedValidator implements ConstraintValidator<PasswordConfirmed,Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        ApplicationUserDto user = (ApplicationUserDto)o;
        return user.getPassword().matches(user.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordConfirmed constraintAnnotation) {

    }
}
