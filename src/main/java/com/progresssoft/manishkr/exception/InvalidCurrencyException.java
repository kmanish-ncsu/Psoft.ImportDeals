package com.progresssoft.manishkr.exception;

public class InvalidCurrencyException extends Exception{
    public InvalidCurrencyException() {
    }

    public InvalidCurrencyException(String message) {
        super (message);
    }

    public InvalidCurrencyException(Throwable cause) {
        super (cause);
    }

    public InvalidCurrencyException(String message, Throwable cause) {
        super (message, cause);
    }
}
