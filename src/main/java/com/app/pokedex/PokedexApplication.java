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
    private final ItemApi itemApi = new ItemApi();
    private final PokemonApi pokemonApi = new PokemonApi();
    private ArrayList<Pokemon> pokedex = new ArrayList<>();
    private final BallBuilder ballBuilder = new BallBuilder();
    private final RandomNumberMaker randomNumberMaker = new RandomNumberMaker();
    private ArrayList<Ball> balls = ballBuilder.buildBalls(itemApi);

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
                        
            You'll either search or randomly search for a Pokemon to capture. Some information
            about the Pokemon will be returned, including its base defense stat. This is important
            because you'll have to "roll" for a number higher than this stat to capture it.
            You will then be given the option to begin your roll or to search or randomly search
            for a different Pokemon.

            If you roll lower than the Pokemon's stat, you lose your Poke Ball.
            If you roll higher than the Pokemon's stat, you capture the Pokemon and add it to your Pokedex.
                        
            The type of Poke Ball that you choose to use may also increase your chances of capture by
            multiplying its capture rate by your rolled number.
                        
            The bag contains:
            - Poke Ball (10 quantity) (1x capture rate)
            - Great Ball (5 quantity) (1.5x capture rate)
            - Ultra Ball (4 quantity) (2x capture rate)
            - Master Ball (1 quantity) (guaranteed capture)
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
                        System.out.println("pokedex");
                    } else if (userPlayGameChoice == 3) {







                        while(true){
                            System.out.println("Type a Pokemon's name to search for:");
                            String userInputPokemon = userInput.nextLine();
                            Pokemon pokemon = pokemonApi.requestPokemonByName(userInputPokemon);
                            System.out.println("\nPokename: " + pokemon.getName() + " || Type: " + pokemon.getType() + " || Stat defense: " + pokemon.getStatDefense());
                            System.out.println("""
                                
                                You must roll higher than the Pokemon's defense stat to catch it.
                                Would you like to roll for Pokemon?
                                1. Yes
                                2. No
                                """);
                            int userRollChoice = userInputChoiceSelected();
                            if(userRollChoice == 1){
                                System.out.println("Choose a Pokeball to use: \n");
                                displayBagInventory(balls);


                                Ball chosenBall = balls.get(userInput.nextInt() - 1);
                                userInput.nextLine();

                                double catchRateMultiplier = chosenBall.getCatchRate();


                                int rolledNumber = randomNumberMaker.makeRandomNumberRoll();
                                int rolledWithMultiplier = (int)Math.round(rolledNumber * catchRateMultiplier);


                                System.out.println("Your roll: " + rolledNumber);
                                System.out.println("Total with success rate: " + rolledWithMultiplier);
                                System.out.println("Pokemon's defense stat: " + pokemon.getStatDefense());
                                if(rolledWithMultiplier > pokemon.getStatDefense()){
                                    pokedex.add(pokemon);
                                    chosenBall.setInventory(chosenBall.getInventory() - 1);
                                    System.out.println("Congrats! You caught " + pokemon.getName());
                                } else {
                                    System.out.println("awh");
                                    chosenBall.setInventory(chosenBall.getInventory() - 1);
                                }
                            }

                            break;


//
//                            if(userRollChoice == 1){
//                                int rolledNumber = randomNumberMaker.makeRandomNumberRoll();
//                                System.out.println("Your roll: " + rolledNumber);
//                                System.out.println("Pokemon's defense stat: " + pokemon.getStatDefense());
//                                if(rolledNumber > pokemon.getStatDefense()){
//                                    pokedex.add(pokemon);
//                                    System.out.println("Congrats! You caught " + pokemon.getName());
//                                } else {
//                                    System.out.println("awh");
//                                }
//                                break;
//                            }




//                            if(userInputPokemon.equals("0")){
//                                int randomNumberId = makeRandomNumberId();
//                                Pokemon pokemon = pokemonApi.requestPokemonById(randomNumberId);
//                            } else{
//
//                            }


















                        }
                    } else if (userPlayGameChoice == 4) {
                        System.out.println("random");
                    } else if (userPlayGameChoice == 0) {
                        System.out.println("return");
                        break;
                    }
                }
            } else if (userMenuChoice == 3) {
                System.out.println("save");
            } else if (userMenuChoice == 0) {
                userInput.close();
                System.out.println("end");
                break;
            }
        }
    }

    public int userInputChoiceSelected() {
        int menuSelection;
        try {
            menuSelection = userInput.nextInt();
            userInput.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Please choose from the selection of numbers");
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void displayBagInventory(ArrayList<Ball> balls) {
        System.out.println("""
                ----------------------------
                BAG
                ----------------------------
                               \s
                id  ||  name    ||  quantity    ||  effect
                """);
        for (Ball ball : balls) {
            System.out.println(ball.getBagId() + "  ||  " + ball.getName() + "  ||  " + ball.getInventory() + " ||  " + ball.getShort_effect());
        }
    }



}
