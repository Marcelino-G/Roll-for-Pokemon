package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Ball.Ball;
import com.app.pokedex.Ball.BallBuilder;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


@SpringBootApplication
public class PokedexApplication {

    private final Scanner userInput = new Scanner(System.in);
    private ArrayList<Pokemon> pokedex = new ArrayList<>();
    private final PokemonApi pokemonApi = new PokemonApi();
    private final ItemApi itemApi = new ItemApi();
    private final BallBuilder ballBuilder = new BallBuilder(itemApi);
    private final RandomNumberMaker randomNumberMaker = new RandomNumberMaker();
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

    public static void main(String[] args) {
//		SpringApplication.run(PokedexApplication.class, args);
        PokedexApplication app = new PokedexApplication();
        app.run();
    }

    public void run() {

        System.out.println(introMessage);

        while (true) {
            System.out.println(menuPrompt);
            int userMenuChoice = userInputChoiceSelected();
            if (userMenuChoice == 1) {
                System.out.println(rulesMessage);
            } else if (userMenuChoice == 2) {
                while (true) {
                    System.out.println(playGamePrompt);
                    int userPlayGameChoice = userInputChoiceSelected();
                    if (userPlayGameChoice == 1) {
                        displayBagInventory(balls);
                    } else if (userPlayGameChoice == 2) {
                        displayPokedex(pokedex);
                    } else if (userPlayGameChoice == 3) {
                        while (true) {
                            System.out.println("Type a Pokemon's name to search for:");
                            String userInputPokemon = userInput.nextLine();
                            Pokemon pokemon = pokemonApi.requestPokemonByName(userInputPokemon);

                            int userSearchAgainChoice = rollForPokemonPrompt(pokemon);

                            if (userSearchAgainChoice == 2) {
                                break;
                            }
                        }
                    } else if (userPlayGameChoice == 4) {
                        while (true) {

                            int randomPokemonId = randomNumberMaker.makeRandomNumberPokemonId();
                            Pokemon pokemon = pokemonApi.requestPokemonById(randomPokemonId);
                            int userSearchAgainChoice = rollForPokemonPrompt(pokemon);

                            if (userSearchAgainChoice == 2) {
                                break;
                            }
                        }
                    } else if (userPlayGameChoice == 0) {
                        break;
                    }
                }
            } else if (userMenuChoice == 3) {
                System.out.println("save");
            } else if (userMenuChoice == 0) {
                userInput.close();
                break;
            }
        }
    }

    public int userInputChoiceSelected() {
        int menuSelection;
        try {
            menuSelection = userInput.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Please choose from the selection of numbers");
            menuSelection = -1;
        }
        userInput.nextLine();
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

            String pokemonName = pokemon.getName().substring(0,1).toUpperCase() + pokemon.getName().substring(1);
            String pokemonType = pokemon.getType().substring(0,1).toUpperCase() + pokemon.getType().substring(1);
            int pokemonDefenseStat = pokemon.getStatDefense();

            System.out.println(pokemonName + "      ||      " + pokemonType + "   ||        " + pokemonDefenseStat);
        }
        System.out.println(" ");
    }

    public int rollForPokemonPrompt(Pokemon pokemon) {

        String pokemonName = pokemon.getName().substring(0,1).toUpperCase() + pokemon.getName().substring(1);
        String pokemonType = pokemon.getType().substring(0,1).toUpperCase() + pokemon.getType().substring(1);
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


                Ball chosenBall = balls.get(userInput.nextInt() - 1);
                userInput.nextLine();

                String ballName = chosenBall.getName();
                int ballInventory = chosenBall.getInventory();
                double ballCatchRate = chosenBall.getCatchRate();


                if (ballInventory == 0) {
                    System.out.println("You have 0 " + ballName + "s.");
                    System.out.println("Choose a different ball to use.");
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

        System.out.println("""
                    Search again?
                    
                    1. Yes
                    2. No
                    """);

        int userSearchAgainChoice = userInputChoiceSelected();

        return userSearchAgainChoice;

    }


}
