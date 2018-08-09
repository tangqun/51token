package com.sharex.token.api.exception;

public class OverfrequencyException extends RuntimeException {

    public OverfrequencyException() {
        super("over frequency request");
    }
}
