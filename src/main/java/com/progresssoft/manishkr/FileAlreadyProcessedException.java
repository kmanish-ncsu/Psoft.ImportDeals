package com.progresssoft.manishkr;

public class FileAlreadyProcessedException extends Exception{
    public FileAlreadyProcessedException () {
    }

    public FileAlreadyProcessedException (String message) {
        super (message);
    }

    public FileAlreadyProcessedException (Throwable cause) {
        super (cause);
    }

    public FileAlreadyProcessedException (String message, Throwable cause) {
        super (message, cause);
    }
}
