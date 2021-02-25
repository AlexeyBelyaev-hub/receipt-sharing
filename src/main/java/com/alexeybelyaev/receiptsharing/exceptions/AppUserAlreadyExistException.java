package com.alexeybelyaev.receiptsharing.exceptions;

public class AppUserAlreadyExistException extends RuntimeException{
    public AppUserAlreadyExistException(String message) {
        super(message);
    }
}
