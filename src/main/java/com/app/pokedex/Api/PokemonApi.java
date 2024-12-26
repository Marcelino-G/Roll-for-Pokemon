package com.app.pokedex.Api;

import com.app.pokedex.Pokemon.Pokemon;
import org.springframework.web.client.RestTemplate;

// api that makes requests for data related to 'pokemon' in the pokemon world

public class PokemonApi {

    private final String pokemonApiPath = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate = new RestTemplate();

    // requests a specific pokemon by plugging in a string (pokemon name)
    public Pokemon requestPokemonByName(String pokemonName) {


        Pokemon pokemonResponse = restTemplate.getForObject(pokemonApiPath + pokemonName, Pokemon.class);

        return pokemonResponse;
    }

    // requests a specific pokemon by plugging in an int (pokemon api id)
    public Pokemon requestPokemonById(int pokemonId){
        Pokemon pokemonResponse = restTemplate.getForObject(pokemonApiPath + pokemonId, Pokemon.class);

        return pokemonResponse;
    }



}
