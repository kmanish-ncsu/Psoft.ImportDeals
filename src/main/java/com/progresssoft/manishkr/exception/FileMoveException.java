package com.progresssoft.manishkr.exception;

public class FileMoveException extends Exception{
    public FileMoveException() {
    }

    public FileMoveException(String message) {
        super (message);
    }

    public FileMoveException(Throwable cause) {
        super (cause);
    }

    public FileMoveException(String message, Throwable cause) {
        super (message, cause);
    }
}
