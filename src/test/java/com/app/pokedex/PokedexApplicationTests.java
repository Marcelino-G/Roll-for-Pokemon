package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;
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
	public void pokemon_array_list_size_equals_the_amount_of_pokemon_api_name_requests(){

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

	@Test
	public void pokemon_array_list_size_equals_the_amount_of_pokemon_api_id_requests(){

		// arrange
		String simulatedInput = "72\n43\n121\n333\n65\n";
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

		java.util.Scanner userInput = new java.util.Scanner(System.in);

		ArrayList<Pokemon> pokedex = new ArrayList<>();

		// act
		int userInputPokemon1 = Integer.parseInt(userInput.nextLine());
		Pokemon pokemon1 = pokemonApi.requestPokemonById(userInputPokemon1);
		int userInputPokemon2 = Integer.parseInt(userInput.nextLine());
		Pokemon pokemon2 = pokemonApi.requestPokemonById(userInputPokemon2);
		int userInputPokemon3 = Integer.parseInt(userInput.nextLine());
		Pokemon pokemon3 = pokemonApi.requestPokemonById(userInputPokemon3);
		int userInputPokemon4 = Integer.parseInt(userInput.nextLine());
		Pokemon pokemon4 = pokemonApi.requestPokemonById(userInputPokemon4);
		int userInputPokemon5 = Integer.parseInt(userInput.nextLine());
		Pokemon pokemon5 = pokemonApi.requestPokemonById(userInputPokemon5);

		pokedex.add(pokemon1);
		pokedex.add(pokemon2);
		pokedex.add(pokemon3);
		pokedex.add(pokemon4);
		pokedex.add(pokemon5);

		// assert
		Assertions.assertEquals(5, pokedex.size());

	}

	@Test
	public void random_number_pokemon_id_maker_works_with_pokemon_api_id_requests(){

		// arrange
		RandomNumberMaker randomNumberMaker = new RandomNumberMaker();
		ArrayList<Integer> pokemonIds = new ArrayList<>();
		ArrayList<Integer> randomNumbers = new ArrayList<>();

		// act
		int randomNum1 = randomNumberMaker.makeRandomNumberPokemonId();
		randomNumbers.add(randomNum1);
		Pokemon pokemon1 = pokemonApi.requestPokemonById(randomNum1);
		pokemonIds.add(pokemon1.getId());

		int randomNum2 = randomNumberMaker.makeRandomNumberPokemonId();
		randomNumbers.add(randomNum2);
		Pokemon pokemon2 = pokemonApi.requestPokemonById(randomNum2);
		pokemonIds.add(pokemon2.getId());

		int randomNum3 = randomNumberMaker.makeRandomNumberPokemonId();
		randomNumbers.add(randomNum3);
		Pokemon pokemon3 = pokemonApi.requestPokemonById(randomNum3);
		pokemonIds.add(pokemon3.getId());

		// assert
		Assertions.assertEquals(randomNumbers, pokemonIds);

	}


//
//	@Test
//	void contextLoads() {
//	}

}
