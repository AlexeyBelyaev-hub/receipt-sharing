package com.alexeybelyaev.receiptsharing.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler  {

    @Autowired
    MessageSource messages;

    @ExceptionHandler(value = {AppUserNotFoundException.class})
    public ResponseEntity<Object> handleAppUserNotFoundException(AppUserNotFoundException exception){
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        log.error(notFound.toString(),exception);
        GeneralResponse response = new GeneralResponse(exception.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return  new ResponseEntity<>(response, notFound);
    }

    @ExceptionHandler(value = {AppUserUpdateException.class})
    public ResponseEntity<Object> handleAppUserUpdateException(AppUserUpdateException exception){
        HttpStatus internalError = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(internalError.toString(),exception);
        GeneralResponse response = new GeneralResponse(exception.getMessage(),
                internalError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(response,internalError);
    }

    @ExceptionHandler(value = {MailAuthenticationException.class})
    public ResponseEntity<Object> handleMailException(MailAuthenticationException exception, WebRequest request){
        HttpStatus internalError = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(internalError.toString(),exception);
        GeneralResponse response = new GeneralResponse(
                messages.getMessage("message.email.error", null, request.getLocale()),
                internalError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(response,internalError);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleInternalException(Exception exception){
        HttpStatus internalError = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(internalError.toString(),exception);
        GeneralResponse response = new GeneralResponse(exception.getMessage(),
                internalError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(response,internalError);
    }
}
