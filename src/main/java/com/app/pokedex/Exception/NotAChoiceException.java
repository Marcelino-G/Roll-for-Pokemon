package com.app.pokedex.Exception;

// when the player enters a number (int) that is not a valid choice

public class NotAChoiceException extends RuntimeException {

    public NotAChoiceException() {
    }

    public NotAChoiceException(String message) {
        super(message);
    }

    public NotAChoiceException(String message, Throwable cause) {
        super(message, cause);
    }


}
