package com.dbclm.eurostat.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class EuroStatExceptionHandler
        extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EuroStatExceptionHandler.class);

    @ExceptionHandler(value
            = {DuplicateOrderException.class})
    protected ResponseEntity<Object> handleConflict(
            final RuntimeException ex, final WebRequest request) {
        LOGGER.warn("Exception occurred!! ex: ", ex.getMessage());
        final String bodyOfResponse = "Exception occurred while saving duplicate orders";
        return handleExceptionInternal(ex, new Message(bodyOfResponse, "Try with different orders."),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}