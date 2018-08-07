package com.sharex.token.api.exception;

public class OverfrequencyException extends Exception {

    public OverfrequencyException() {
        super("over frequency request");
    }
}
