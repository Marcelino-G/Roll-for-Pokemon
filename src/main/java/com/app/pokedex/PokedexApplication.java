package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Ball.Ball;
import com.app.pokedex.Ball.BallBuilder;
import com.app.pokedex.Exception.NotAChoiceException;
import com.app.pokedex.Exception.PokemonNotFoundException;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
public class PokedexApplication {

    private final Scanner userInput = new Scanner(System.in);

    //    pokedex is an empty ArrayList of Pokemon. Pokemon are added to it with
//    each successful roll. This helps us track our captured Pokemon.
    private ArrayList<Pokemon> pokedex = new ArrayList<>();
    private final PokemonApi pokemonApi = new PokemonApi();
    private final ItemApi itemApi = new ItemApi();
    private final BallBuilder ballBuilder = new BallBuilder(itemApi);
    private final RandomNumberMaker randomNumberMaker = new RandomNumberMaker();

    // balls is an ArrayList of all the classes that
    // extend the Ball class. This allows us access to
    // ball count and special ball effects.
    private ArrayList<Ball> balls = ballBuilder.buildBalls();

    private final String introMessage = """
            ----------------------------
            POKEDEX
            ----------------------------
            It's 2025. What else can go wrong? BOOM! You've been struck by lightning while walking your cat.
            When you rise, you notice the world around you is infested with monsters, known as Pokemon.
            Your head hurts, your cat is missing, and you won't be getting tacos this Tuesday evening.
            What are you going to do?
            """;
    private final String rulesMessage = """
            ----------------------------
            RULES
            ----------------------------
            You've found a bag full of Poke Balls. You better take it because who knows
            what type of journey lies ahead.
            
            You'll either manually search or randomly search for a Pokemon to capture. Some information
            about the Pokemon will be returned, including its base defense stat. This is important
            because you'll have to "roll" for a number higher than this stat to capture it.
            You will then be given the option to begin your roll or to search for a different Pokemon.
            
            If you roll lower than the Pokemon's defense stat, you fail to add it to your Pokedex.
            If you roll higher than the Pokemon's stat, you capture the Pokemon and add it to your Pokedex.
            Both outcomes result in you losing the Poke Ball you chose to use.
            
            The type of Poke Ball that you choose to use may also increase your chances of capture by
            multiplying its capture rate by your rolled number.
            
            Check your bag to view your Poke Balls.
            """;
    private final String menuPrompt = """
            ----------------------------
            MAIN MENU
            ----------------------------
            
            1. Rules
            2. Play game
            3. Save file
            0. Exit
            """;
    private final String playGamePrompt = """
            ----------------------------
            PLAY GAME
            ----------------------------
            
            1. Check bag
            2. Check Pokedex
            3. Search Pokemon
            4. Randomly search Pokemon
            0. Return to main menu
            """;
    private final String searchAgainPrompt = """
             Search again?
            
                1. Yes
                2. No
            """;

    public static void main(String[] args) {
//		SpringApplication.run(PokedexApplication.class, args);
        PokedexApplication app = new PokedexApplication();
        app.run();
    }

    public void run() {

        System.out.println(introMessage);

        // main loop for the main menu prompt
        // loop ends when you hit '0' which also closes the program.
        // loop continues whenever a valid input is entered
        // and may dive into another loop with a different prompt.
        while (true) {

            // a try|catch that catches a NotAChoiceException
            // which is thrown within the loop when the user
            // enters an invalid prompt choice.
            try {
                System.out.println(menuPrompt);
                int userMenuChoice = userInputChoiceSelected();
                if (userMenuChoice == 1) {
                    System.out.println(rulesMessage);
                } else if (userMenuChoice == 2) {
                    // nested play game loop (within main loop).
                    // loop ends and returns to the main menu prompt when '0' is entered.
                    // loop continues whenever a valid input is entered
                    // and may dive into another loop with a different prompt
                    while (true) {

                        try {
                            System.out.println(playGamePrompt);
                            int userPlayGameChoice = userInputChoiceSelected();
                            if (userPlayGameChoice == 1) {
                                displayBagInventory(balls);
                            } else if (userPlayGameChoice == 2) {
                                displayPokedex(pokedex);
                            } else if (userPlayGameChoice == 3) {

                                // This is a loop for actual gameplay.
                                // nested roll for pokemon loop (within play game loop).
                                // user String input is needed here for API related functionality
                                // and is also why try|catch is used here because adding
                                // the String to the API may or may not return a match
                                // and instead an error.

                                while (true) {

                                    // a try|catch that catches a PokemonNotFoundException.
                                    // which is thrown when a HttpClientErrorException is caught
                                    // from a nested try|catch. PokemonNotFoundException is a
                                    // custom exception that is meant to translate to the user's input
                                    // `userInputPokemon` not existing (not a pokemon) within the API.
                                    try {
                                        System.out.println("Type a Pokemon's name to search for:");
                                        String userInputPokemon = userInput.nextLine();

                                        // a try|catch that catches a HttpClientErrorException
                                        // and then throws the custom PokemonNotFoundException.
                                        try {
                                            Pokemon pokemon = pokemonApi.requestPokemonByName(userInputPokemon);
                                            rollForPokemonPrompt(pokemon);
                                            System.out.println(searchAgainPrompt);
                                            int userSearchDecision = userInputChoiceSelected();

                                            if (userSearchDecision == 2) {
                                                break;
                                            }
                                        } catch (HttpClientErrorException ex) {
                                            throw new PokemonNotFoundException("\n Could not find " + userInputPokemon + "\n");
                                        }
                                    } catch (PokemonNotFoundException ex) {
                                        System.out.println(ex.getMessage());
                                    }


                                }
                            } else if (userPlayGameChoice == 4) {


                                // This is a loop for actual gameplay.
                                // nested roll for pokemon loop (within play game loop).
                                // user String input is not needed here for API related functionality
                                // because a random number (int) is generated and added to the API
                                // instead of a string which is lessens the chance of API related errors.
                                // This was made for those who don't know any pokemon name's or
                                // simply dont want to enter any.
                                while (true) {

                                    int randomPokemonId = randomNumberMaker.makeRandomNumberPokemonId();
                                    Pokemon pokemon = pokemonApi.requestPokemonById(randomPokemonId);
                                    rollForPokemonPrompt(pokemon);
                                    System.out.println(searchAgainPrompt);
                                    int userSearchDecision = userInputChoiceSelected();

                                    if (userSearchDecision == 2) {
                                        break;
                                    }
                                }
                            } else if (userPlayGameChoice == 0) {
                                // returns to main menu
                                break;
                            } else if (userPlayGameChoice > 4) {
                                // an invalid option was entered, throwing an exception
                                // and rerunning the current loop.
                                // there are no options greater than '4'
                                throw new NotAChoiceException("Please choose a valid number choice");
                            }


                        } catch (NotAChoiceException ex) {
                            System.out.println(ex.getMessage());
                        }


                    }
                } else if (userMenuChoice == 3) {
                    System.out.println("save");
                } else if (userMenuChoice == 0) {
                    // ends the program
                    userInput.close();
                    break;
                } else if (userMenuChoice > 3) {
                    // an invalid option was entered, throwing an exception
                    // and rerunning the current loop.
                    // there are no options greater than '3'
                    throw new NotAChoiceException("Please choose a valid number choice");
                }
            } catch (NotAChoiceException ex) {

                System.out.println(ex.getMessage());


            }


        }
    }

    public int userInputChoiceSelected() {
        int menuSelection = -1;
        String input = "";

        // try|catch to make sure user input a number (int)
        // not a String
        try {
            input = userInput.nextLine();
            menuSelection = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(input + " is not a valid number choice");
        }

        return menuSelection;
    }

    public void displayBagInventory(ArrayList<Ball> balls) {
        System.out.println("""
                ----------------------------
                BAG
                ----------------------------
                
                Id  ||  Item        ||  Quantity        ||  Effect
                """);
        for (Ball ball : balls) {

            int ballId = ball.getBagId();
            String ballName = ball.getName().replace("-", " ");
            int ballInventory = ball.getInventory();
            String ballEffect = ball.getShort_effect();


            System.out.println(ballId + "  ||  " + ballName + "      ||  " + ballInventory + "             ||  " + ballEffect);
        }
        System.out.println(" ");
    }

    public void displayPokedex(ArrayList<Pokemon> pokedex) {
        System.out.println("""
                ----------------------------
                POKEDEX
                ----------------------------
                
                Pokemon     ||      Type    ||      Defense stat
                """);
        for (Pokemon pokemon : pokedex) {

            String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
            String pokemonType = pokemon.getType().substring(0, 1).toUpperCase() + pokemon.getType().substring(1);
            int pokemonDefenseStat = pokemon.getStatDefense();

            System.out.println(pokemonName + "      ||      " + pokemonType + "   ||        " + pokemonDefenseStat);
        }
        System.out.println(" ");
    }

    public void rollForPokemonPrompt(Pokemon pokemon) {

        String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
        String pokemonType = pokemon.getType().substring(0, 1).toUpperCase() + pokemon.getType().substring(1);
        int pokemonDefenseStat = pokemon.getStatDefense();

        System.out.println("\nPokemon: " + pokemonName + " || Type: " + pokemonType + " || Defense stat: " + pokemonDefenseStat);
        System.out.println("""
                
                Would you like to roll for Pokemon?
                1. Yes
                2. No
                """);
        int userRollChoice = userInputChoiceSelected();
        if (userRollChoice == 1) {

            while (true) {


                System.out.println("Choose a Pokeball to use: \n");
                displayBagInventory(balls);


                Ball chosenBall = balls.get(Integer.parseInt(userInput.nextLine()) - 1);
//                userInput.nextLine();

                String ballName = chosenBall.getName().replace("-", " ");
                int ballInventory = chosenBall.getInventory();
                double ballCatchRate = chosenBall.getCatchRate();


                if (ballInventory == 0) {
//                    System.out.println("You have 0 " + ballName + "s.");
//                    System.out.println("Choose a different ball to use.");

                    System.out.printf("""
                            You have 0 %s's left.
                            Choose a different ball to use.
                            
                            """, ballName);

                } else {
//                    double catchRateMultiplier = ballCatchRate;


                    int rolledNumber = randomNumberMaker.makeRandomNumberRoll();
                    int rolledWithMultiplier = (int) Math.round(rolledNumber * ballCatchRate);


//                    System.out.println("Your roll: " + rolledNumber);
                    System.out.println("Pokemon's defense stat: " + pokemonDefenseStat);
                    System.out.println("Your roll: " + rolledWithMultiplier);

                    if (rolledWithMultiplier > pokemonDefenseStat) {
                        pokedex.add(pokemon);
                        System.out.println("\n \uD83C\uDFC6 Congrats! You caught " + pokemonName + " \uD83C\uDFC6 \n");
                    } else {
                        System.out.println("\n \uD83D\uDE2D " + pokemonName + " got away! \uD83D\uDE2D \n");

                    }
                    chosenBall.setInventory(ballInventory - 1);
                    break;
                }


            }


        }
    }


}
