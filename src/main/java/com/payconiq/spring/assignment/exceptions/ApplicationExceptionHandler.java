package com.payconiq.spring.assignment.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handling Application Exceptions.
 * 
 * @author sravana.pullivendula@gmail.com
 *
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleUnhandledException(Exception ex) {
        logger.error("Server Internal Issue: ", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<Object>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        logger.error("Server Internal Issue: ", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return new ResponseEntity<Object>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { ApplicationExceptions.class })
    public ResponseEntity<Object> handleCreateException(ApplicationExceptions ex) {
        ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage(), ex);
        return new ResponseEntity<Object>(apiError, ex.getStatus());
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleCreateException(IllegalArgumentException ex) {
        logger.error("Invalid Input Exception : ", ex.getMessage());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
    }

}
