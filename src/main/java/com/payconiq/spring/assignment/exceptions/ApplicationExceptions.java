package com.payconiq.spring.assignment.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Application related Exception cases.
 * 
 * @author sravana.pullivendula@gmail.com
 *
 */
public class ApplicationExceptions extends RuntimeException{
    
    private static final long serialVersionUID = 1L;
    
    private HttpStatus status;

    public ApplicationExceptions(String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.status = status;
    }
    
    public ApplicationExceptions(String errorMessage, HttpStatus status, Throwable e) {
        super(errorMessage, e);
        this.status = status;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    

}
