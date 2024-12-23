package com.app.pokedex.Exception;

public class PokemonNotFoundException extends RuntimeException {

    public PokemonNotFoundException() {
    }

    public PokemonNotFoundException(String message) {
        super(message);
    }

    public PokemonNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
