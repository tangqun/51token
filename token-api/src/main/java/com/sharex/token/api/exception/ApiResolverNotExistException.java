package com.sharex.token.api.exception;

public class ApiResolverNotExistException extends RuntimeException {

    public ApiResolverNotExistException() {
        super("exchange is not support now!");
    }
}
