package com.app.pokedex.Exception;

public class NotAChoiceException extends RuntimeException{

    public NotAChoiceException() {
    }

    public NotAChoiceException(String message) {
        super(message);
    }

    public NotAChoiceException(String message, Throwable cause) {
        super(message, cause);
    }



}
