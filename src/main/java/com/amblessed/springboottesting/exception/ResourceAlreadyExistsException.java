package com.amblessed.springboottesting.exception;



/*
 * @Project Name: springboot-testing
 * @Author: Okechukwu Bright Onwumere
 * @Created: 21-Feb-25
 */


public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

}
