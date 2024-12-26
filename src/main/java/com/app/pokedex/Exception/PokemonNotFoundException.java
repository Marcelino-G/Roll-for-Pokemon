package com.app.pokedex.Exception;

// when the string ran through PokemonApi.requestPokemonByName(String name) doesn't find a match

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
