package com.app.pokedex.Exception;

public class ZeroBallsLeftException extends RuntimeException {

    public ZeroBallsLeftException() {
    }

    public ZeroBallsLeftException(String message) {
        super(message);
    }

    public ZeroBallsLeftException(String message, Throwable cause) {
        super(message, cause);
    }
}
