package com.alexeybelyaev.receiptsharing.exceptions;

public class AppUserNotFoundException extends RuntimeException{
    public AppUserNotFoundException(String message) {
        super(message);
    }
}
