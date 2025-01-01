package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Pokemon.Pokemon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

//@SpringBootTest
class PokedexApplicationTests {

	private static PokemonApi pokemonApi;
	private static ItemApi itemApi;

	@BeforeAll
	public static void instantiate_apis(){
		pokemonApi = new PokemonApi();
		itemApi = new ItemApi();
	}

	@Test
	public void pokemon_array_list_size_should_equal_the_amount_of_api_pokemon_string_requests(){

		// arrange
		String simulatedInput = "ditto\nSquirtle\nenTei \nmr miMe \n";
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		java.util.Scanner userInput = new java.util.Scanner(System.in);

		ArrayList<Pokemon> pokedex = new ArrayList<>();

		// act
		String userInputPokemon1 = userInput.nextLine().toLowerCase().trim().replace(" ", "-");
		Pokemon pokemon1 = pokemonApi.requestPokemonByName(userInputPokemon1);
		String userInputPokemon2 = userInput.nextLine().toLowerCase().trim().replace(" ", "-");
		Pokemon pokemon2 = pokemonApi.requestPokemonByName(userInputPokemon2);
		String userInputPokemon3 = userInput.nextLine().toLowerCase().trim().replace(" ", "-");
		Pokemon pokemon3 = pokemonApi.requestPokemonByName(userInputPokemon3);
		String userInputPokemon4 = userInput.nextLine().toLowerCase().trim().replace(" ", "-");
		Pokemon pokemon4 = pokemonApi.requestPokemonByName(userInputPokemon4);

		pokedex.add(pokemon1);
		pokedex.add(pokemon2);
		pokedex.add(pokemon3);
		pokedex.add(pokemon4);

		// assert
		Assertions.assertEquals(4, pokedex.size());

	}


//
//	@Test
//	void contextLoads() {
//	}

}
