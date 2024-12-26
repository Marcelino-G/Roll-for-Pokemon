package com.app.pokedex.Exception;


// when the player tries to use a pokeball that is out of stock

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
