package com.app.pokedex.Api;

import com.app.pokedex.Pokemon.Pokemon;
import org.springframework.web.client.RestTemplate;

public class PokemonApi {

    private final String pokemonApiPath = "https://pokeapi.co/api/v2/pokemon/";
    RestTemplate restTemplate = new RestTemplate();

    public Pokemon requestPokemonByName(String pokemonName) {


        Pokemon pokemonResponse = restTemplate.getForObject(pokemonApiPath + pokemonName, Pokemon.class);

        return pokemonResponse;
    }

    public Pokemon requestPokemonById(int pokemonId){
        Pokemon pokemonResponse = restTemplate.getForObject(pokemonApiPath + pokemonId, Pokemon.class);

        return pokemonResponse;
    }



}
