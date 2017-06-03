package com.progresssoft.manishkr.exception;

public class FileParseException extends Exception{
    public FileParseException() {
    }

    public FileParseException(String message) {
        super (message);
    }

    public FileParseException(Throwable cause) {
        super (cause);
    }

    public FileParseException(String message, Throwable cause) {
        super (message, cause);
    }
}
