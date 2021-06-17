package com.epam.jwd.apotheca.dao;

public class CouldNotInitializeConnectionPoolException extends Exception {

    public CouldNotInitializeConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}