package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Ball.Ball;
import com.app.pokedex.Ball.BallBuilder;
import com.app.pokedex.Ball.Model.GreatBall;
import com.app.pokedex.Ball.Model.MasterBall;
import com.app.pokedex.Ball.Model.PokeBall;
import com.app.pokedex.Ball.Model.UltraBall;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import java.util.*;

//@SpringBootTest
class PokedexApplicationTests {

    private static PokemonApi pokemonApi;
    private static ItemApi itemApi;

    @BeforeAll
    public static void instantiate_apis() {
        pokemonApi = new PokemonApi();
        itemApi = new ItemApi();
    }

    @Test
    public void pokemon_array_list_size_equals_the_amount_of_pokemon_api_name_requests() {

        // arrange
        String simulatedInput = "ditto\nSquirtle\nenTei \nmr miMe \n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner userInput = new Scanner(System.in);
        ArrayList<Pokemon> pokedex = new ArrayList<>();

        // act
        // the .tolowercase().trim().etc. is exactly how its set up in the main app
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

        // cleanup
        System.setIn(System.in);
    }

    @Test
    public void pokemon_array_list_size_equals_the_amount_of_pokemon_api_id_requests() {

        // arrange
        String simulatedInput = "72\n43\n121\n333\n65\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner userInput = new Scanner(System.in);
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

        // cleanup
        System.setIn(System.in);
    }

    @Test
    public void random_number_pokemon_id_maker_works_with_pokemon_api_id_requests() {

        // arrange
        RandomNumberMaker randomNumberMaker = new RandomNumberMaker();
        // the actual pokemon Ids in the pokemon world
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
        // the actual pokemon Ids in the pokemon world should match the random numbers
        Assertions.assertEquals(randomNumbers, pokemonIds);
    }

    @Test
    public void ball_builder_build_balls_returns_all_existing_ball_subclasses() {

        // arrange
        BallBuilder ballBuilder = new BallBuilder(itemApi);
        ArrayList<Ball> subClassBalls = new ArrayList<>(Arrays.asList(new GreatBall(), new MasterBall(), new PokeBall(), new UltraBall()));
        // increases everytime a certain ball in subClassBalls is found in builtBalls
        int containsBallCounter = 0;

        // act
        ArrayList<Ball> builtBalls = ballBuilder.buildBalls();

        for (Ball subBall : subClassBalls) {
            for (Ball builtBall : builtBalls) {
                if (subBall.getClass() == builtBall.getClass()) {
                    containsBallCounter++;
                    break;
                }
            }
        }

        // assert
        Assertions.assertEquals(subClassBalls.size(), containsBallCounter);
    }

    @Test
    public void user_input_choice_returns_negative_one() {

        // arrange
        String simulatedInput = "5\n32\n435";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        PokedexApplication pokedexApplication = new PokedexApplication();
        ArrayList<Integer> negativeOnes = new ArrayList<>();

        // act
        int firstNegativeOne = pokedexApplication.userInputChoiceSelected(4);
        negativeOnes.add(firstNegativeOne);
        int secondNegativeOne = pokedexApplication.userInputChoiceSelected(30);
        negativeOnes.add(secondNegativeOne);
        int thirdNegativeOne = pokedexApplication.userInputChoiceSelected(434);
        negativeOnes.add(thirdNegativeOne);

        // assert
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(-1, -1, -1)), negativeOnes);

        // cleanup
        System.setIn(System.in);
    }

    @Test
    public void user_input_choice_returns_users_choice() {

        // arrange
        String simulatedInput = "7\n2\n11";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        PokedexApplication pokedexApplication = new PokedexApplication();
        ArrayList<Integer> userChoices = new ArrayList<>();

        // act
        int userChoice1 = pokedexApplication.userInputChoiceSelected(7);
        userChoices.add(userChoice1);
        int userChoice2 = pokedexApplication.userInputChoiceSelected(3);
        userChoices.add(userChoice2);
        int userChoice3 = pokedexApplication.userInputChoiceSelected(15);
        userChoices.add(userChoice3);

        // assert
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(7, 2, 11)), userChoices);

        // cleanup
        System.setIn(System.in);
    }

    @Test
    public void choose_pokeball_prompt_accepts_and_handles_valid_choices() {

        // arrange
        String simulatedInput = "1\n2\n3\n4\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        PokedexApplication pokedexApplication = new PokedexApplication();
        ArrayList<String> balls = new ArrayList<>();

        // act
        Ball ball1 = pokedexApplication.choosePokeballPrompt();
        balls.add(ball1.getName());
        Ball ball2 = pokedexApplication.choosePokeballPrompt();
        balls.add(ball2.getName());
        Ball ball3 = pokedexApplication.choosePokeballPrompt();
        balls.add(ball3.getName());
        Ball ball4 = pokedexApplication.choosePokeballPrompt();
        balls.add(ball4.getName());
        // "0" breaks the loop in the function to "exit"
        // to the play game menu and returns a null Ball
        Ball ball5 = pokedexApplication.choosePokeballPrompt();
        balls.add(ball5 == null ? null : ball5.getName());

        // assert
        Assertions.assertEquals(new ArrayList<>(Arrays.asList("poke-ball", "great-ball", "ultra-ball", "master-ball", null)), balls);

        // cleanup
        System.setIn(System.in);
    }

    @Test
    public void name_your_file_prompt_returns_filename() {

        // arrange
        String simulatedInput = "my first sql file\n1\n" + // for first nameYourFilePrompt() call
                "my first sql file\n2\nmy second sql file\n1\n" + // for second nameYourFilePrompt() call
                "my first sql file\n1\n"; // for third nameYourFilePrompt() call
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        PokedexApplication pokedexApplication = new PokedexApplication();
        // hashmaps to simulate "overwriting" our files in the real app
        // since this is just naming files and not creating them.
        // the file names will be the "keys" because that is what gets overwritten
        // in hashmaps when handling duplicates
        HashMap<String, Integer> savedFiles = new HashMap<>();
        // our expected hashmap values
        HashMap<String, Integer> files = new HashMap<>();

        // act
        // the second argument (boolean) is set to true here, the only place it would be set
        // to true so that it can access a certain "if" statement that originally checked only for
        // existing files. Again, we're not creating files here, just naming them.
        String filename1 = pokedexApplication.nameYourFilePrompt("sql", true);
        savedFiles.put(filename1, 1);
        String filename2 = pokedexApplication.nameYourFilePrompt("sql", true);
        savedFiles.put(filename2, 2);
        String filename3 = pokedexApplication.nameYourFilePrompt("sql", true);
        savedFiles.put(filename3, 3);

        files.put("my_second_sql_file", 2);
        files.put("my_first_sql_file", 3);

        // assert
        Assertions.assertEquals(files, savedFiles);

        // cleanup
        System.setIn(System.in);
    }


//
//	@Test
//	void contextLoads() {
//	}

}
