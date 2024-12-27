package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Ball.Ball;
import com.app.pokedex.Ball.BallBuilder;
import com.app.pokedex.Exception.NotAChoiceException;
import com.app.pokedex.Exception.PokemonNotFoundException;
import com.app.pokedex.Exception.ZeroBallsLeftException;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;
import com.app.pokedex.SQL.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
public class PokedexApplication implements CommandLineRunner {

    @Autowired
    private PokemonService pokemonService;

    private final Scanner userInput = new Scanner(System.in);

    // pokemon are added to this list with each successful roll.
    private ArrayList<Pokemon> pokedex = new ArrayList<>();
    private final PokemonApi pokemonApi = new PokemonApi();
    private final ItemApi itemApi = new ItemApi();
    private final BallBuilder ballBuilder = new BallBuilder(itemApi);
    private final RandomNumberMaker randomNumberMaker = new RandomNumberMaker();

    // all the types of balls that extend the Ball class.
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
		SpringApplication.run(PokedexApplication.class, args);
//        PokedexApplication app = new PokedexApplication();
//
//        app.run();
    }

    @Override
    public void run(String... args){

        pokemonService.createTable();
        pokemonService.exportTableToFile();
        runApp();

    }

    public void runApp() {

//        pokemonService.createTable();

        System.out.println(introMessage);

        // main loop for the main menu prompt.
        // may dive into another while loop with a different prompt.
        while (true) {

            System.out.println(menuPrompt);
            int userMenuChoice = userInputChoiceSelected(3);
            if (userMenuChoice == 1) {
                System.out.println(rulesMessage);
            } else if (userMenuChoice == 2) {

                // nested play game loop (within main loop) that
                // brings up game options, not the actual game.
                // may dive into another loop with a different prompt
                while (true) {


                    System.out.println(playGamePrompt);
                    int userPlayGameChoice = userInputChoiceSelected(4);
                    if (userPlayGameChoice == 1) {
                        displayBagInventory(balls);
                    } else if (userPlayGameChoice == 2) {
                        displayPokedex(pokedex);
                    } else if (userPlayGameChoice == 3) {

                        // beginning of actual gameplay.
                        // searching for pokemon loop (within play game loop).
                        // user String input is needed here for API related functionality.
                        while (true) {
                            Pokemon pokemon;

                            // a try|catch that catches a PokemonNotFoundException.
                            // which is thrown when a HttpClientErrorException is caught
                            // from a nested try|catch.
                            try {
                                System.out.println("Type a Pokemon's name to search for:");
                                String userInputPokemon = userInput.nextLine();

                                // userInputPokemon cannot be an empty string
                                if (userInputPokemon.isEmpty()) {
                                    System.out.println("Input cannot be blank. Please enter a valid Pokemon name. \n");
                                    continue;  // Skip to the next iteration of the loop to prompt again
                                }

                                // a try|catch that catches a HttpClientErrorException
                                // and then throws the custom PokemonNotFoundException.
                                try {
                                    pokemon = pokemonApi.requestPokemonByName(userInputPokemon);

                                } catch (HttpClientErrorException ex) {
                                    throw new PokemonNotFoundException("\n Could not find " + userInputPokemon + "\n");
                                }

                                // runs through the rollForPokemonPrompt, and returns an int
                                // the represents your interest in pursuing it based on it's
                                // displayed stats.
                                int userRollChoice = rollForPokemonPrompt(pokemon);

                                // '1' means yes, and you proceed to choose the ball you want to use
                                if (userRollChoice == 1) {

                                    Ball userBallChoice = null;

                                    // choosing a poke ball loop (within searching for pokemon loop).
                                    while (true) {

                                        // a try|catch catches a NotAChoiceException when
                                        // an invalid input is entered and ZeroBallsLeftException
                                        // when the ball inventory is '0', meaning no balls left to use.
                                        try {
                                            userBallChoice = choosePokeballPrompt();
                                        } catch (NotAChoiceException | ZeroBallsLeftException ex) {
                                            System.out.println(ex.getMessage());
                                        }

                                        if (userBallChoice != null) {
                                            break;
                                        }
                                    }


                                    capturePokemon(pokemon, userBallChoice);

                                }

                            } catch (PokemonNotFoundException ex) {
                                System.out.println(ex.getMessage());
                            }

                            // returns an int that shows your interest in
                            // doing another search. 1 = another search
                            // 2 = breaks the search for pokemon loop
                            int userSearchDecision = searchAgainPrompt();
                            if (userSearchDecision == 1) {
                            } else if (userSearchDecision == 2) {
                                break;
                            }


                        }
                    } else if (userPlayGameChoice == 4) {

                        // beginning of actual gameplay.
                        // random searching for pokemon loop (within play game loop).
                        // a random number that acts as a Pokemons ID
                        // is generated and inserted into the api.
                        // This was made for those who don't know any pokemon name's or
                        // simply dont want to enter any.
                        while (true) {


                            int randomPokemonId = randomNumberMaker.makeRandomNumberPokemonId();
                            Pokemon pokemon = pokemonApi.requestPokemonById(randomPokemonId);

                            // runs through the rollForPokemonPrompt, and returns an int
                            // the represents your interest in pursuing it based on it's
                            // displayed stats.
                            int userRollChoice = rollForPokemonPrompt(pokemon);

                            // '1' means yes, and you proceed to choose the ball you want to use
                            if (userRollChoice == 1) {

                                Ball userBallChoice = null;

                                // choosing a poke ball loop (within searching for pokemon loop).
                                while (true) {

                                    // a try|catch catches a NotAChoiceException when
                                    // an invalid input is entered and ZeroBallsLeftException
                                    // when the ball inventory is '0', meaning no balls left to use.
                                    try {

                                        userBallChoice = choosePokeballPrompt();

                                    } catch (NotAChoiceException | ZeroBallsLeftException ex) {
                                        System.out.println(ex.getMessage());
                                    }

                                    if (userBallChoice != null) {
                                        break;
                                    }
                                }
                                capturePokemon(pokemon, userBallChoice);

                            }

                            // returns an int that shows your interest in
                            // doing another search. 1 = another search
                            // 2 = breaks the search for pokemon loop
                            int userSearchDecision = searchAgainPrompt();
                            if (userSearchDecision == 1) {

                            } else if (userSearchDecision == 2) {
                                break;
                            }


                        }
                    } else if (userPlayGameChoice == 0) {
                        // returns to main menu
                        break;
                    }


                }
            } else if (userMenuChoice == 3) {
                System.out.println("save");
            } else if (userMenuChoice == 0) {
                // ends the program
                userInput.close();
                break;
            }


        }
    }

    // int returned based on the user's input in response to a prompt.
    // handles exceptions for NumberFormatException when a string is entered
    // instead of a number (int). NotAChoiceException is thrown when the user enters
    // a number larger than the LARGEST possible choice the prompt asks for (highestChoiceNum).
    // This happens within an if statement that also returns the expected int as -1 for further error
    // handling when needed.
    public int userInputChoiceSelected(int highestChoiceNum) {
        int menuSelection = -1;
        String input = "";

        // try|catch to make sure user inputs a number (int)
        // not a String and a valid choice.
        try {
            input = userInput.nextLine();
            menuSelection = Integer.parseInt(input);
            if (menuSelection > highestChoiceNum) {
                menuSelection = -1;
                throw new NotAChoiceException("Please choose a valid number choice \n");
            }
        } catch (NumberFormatException ex) {
            System.out.println(input + " is not a valid number choice \n");
        } catch (NotAChoiceException ex) {
            System.out.println(ex.getMessage());
        }

        return menuSelection;
    }

    // displays your bag (balls) with a for loop to keep track of inventory,
    // effects, and options to choose from.
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

    // displays your pokedex (pokemon) with a for loop to keep track of
    // all the pokemon you've caught and their information.
    public void displayPokedex(ArrayList<Pokemon> pokedex) {
        StringBuilder pokedexStringBuilder = new StringBuilder();
        pokedexStringBuilder.append("""
                ----------------------------
                POKEDEX
                ----------------------------
                                
                Pokemon     ||      Type    ||      Defense stat    ||      Picture
                                
                """);

//        System.out.println("""
//                ----------------------------
//                POKEDEX
//                ----------------------------
//
//                Pokemon     ||      Type    ||      Defense stat
//                """);
        for (Pokemon pokemon : pokedex) {

            String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
            String pokemonType = pokemon.getType().substring(0, 1).toUpperCase() + pokemon.getType().substring(1);
            String pokemonSprite = pokemon.getMainSprite();
            int pokemonDefenseStat = pokemon.getStatDefense();

            pokedexStringBuilder.append(("%s      ||      %s   " +
                    "||        %s          ||        %s \n").formatted(pokemonName, pokemonType, pokemonDefenseStat, pokemonSprite));

//            System.out.println(pokemonName + "      ||      " + pokemonType + "   ||        " + pokemonDefenseStat);
        }
        System.out.println(pokedexStringBuilder);
    }

    // a prompt that displays to you information about the pokemon you've searched for and whether
    // you want to try and roll for it, a number (int) is returned that represents your decision.
    public int rollForPokemonPrompt(Pokemon pokemon) {

        String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
        String pokemonType = pokemon.getType().substring(0, 1).toUpperCase() + pokemon.getType().substring(1);
        String pokemonSprite = pokemon.getMainSprite();
        int pokemonDefenseStat = pokemon.getStatDefense();

        int userRollChoice;

        while (true) {

            System.out.printf("\nPokemon: %s   ||  Type: %s    ||  Defense stat: %s    ||  Picture: %s \n", pokemonName, pokemonType, pokemonDefenseStat, pokemonSprite);
            System.out.println("""
                                    
                    Would you like to roll for Pokemon?
                    1. Yes
                    2. No
                    """);
            userRollChoice = userInputChoiceSelected(2);

            if (userRollChoice == 1 || userRollChoice == 2) {
                break;
            }

        }

        return userRollChoice;
    }

    // a prompt that displays your bag (balls) to you so that you can decide which
    // pokeball you would like to use to try and capture a pokemon.
    // the choice (ball) is returned.
    public Ball choosePokeballPrompt() {

        Ball chosenBall = null;
        int userBallChoice;

        while (true) {
            System.out.println("Choose a Pokeball to use: \n");
            displayBagInventory(balls);

            // ball.size() is used to represent the highestChoiceNum argument in
            // userInputChoiceSelected()... because the highest number option equals it's size.
            userBallChoice = userInputChoiceSelected(balls.size());

            // if a user enters a number above balls.size() (highestChoiceNum)
            // it throws and displays its exception message but keeps running in this case.
            // userInputChoiceSelected() also returns -1 along with this exception.
            // if this occurs, this 'if' statement prevents from trying
            // to get an index that doesn't exist within the 'balls' arraylist
            if (userBallChoice != -1) {

                try {
                    chosenBall = balls.get(userBallChoice - 1);
                } catch (IndexOutOfBoundsException ex) {
                    throw new NotAChoiceException("Please choose a valid number choice \n");
                }

                String ballName = chosenBall.getName().replace("-", " ");
                int ballInventory = chosenBall.getInventory();
                double ballCatchRate = chosenBall.getCatchRate();

                // lets the user know that there are no more balls left
                // to use and reruns the loop.
                if (ballInventory == 0) {
                    throw new ZeroBallsLeftException("""
                             You have 0 %s's left.
                             Choose a different ball to use.

                            """.formatted(ballName));

                } else if (userBallChoice == -1) {

                } else {
                    break;
                }
            }
        }
        return chosenBall;

    }

    // takes in the searched pokemon and chosen pokeball
    // to determine whether a successful catch occurred
    // random roll * pokeball catch rate > pokemons defense stat = catch.
    // pokeball count (inventory) is adjusted.
    public void capturePokemon(Pokemon pokemon, Ball chosenBall) {

        String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
        int pokemonDefenseStat = pokemon.getStatDefense();
        int ballInventory = chosenBall.getInventory();
        double ballCatchRate = chosenBall.getCatchRate();

        int rolledNumber = randomNumberMaker.makeRandomNumberRoll();
        int rolledWithMultiplier = (int) Math.round(rolledNumber * ballCatchRate);


        System.out.println("Pokemon's defense stat: " + pokemonDefenseStat);
        System.out.println("Your roll: " + rolledWithMultiplier);

        if (rolledWithMultiplier > pokemonDefenseStat) {
            pokedex.add(pokemon);
            System.out.println("\n \uD83C\uDFC6 Congrats! You caught " + pokemonName + " \uD83C\uDFC6 \n");
        } else {
            System.out.println("\n \uD83D\uDE2D " + pokemonName + " got away! \uD83D\uDE2D \n");

        }
        chosenBall.setInventory(ballInventory - 1);


    }

    // asks whether you'd like to do another pokemon search.
    // an int is returned to represent your decision.
    public int searchAgainPrompt() {

        while (true) {
            System.out.println("""
                     Search again?
                                
                        1. Yes
                        2. No
                    """);
            int userSearchDecision = userInputChoiceSelected(2);
            if (userSearchDecision == 1 || userSearchDecision == 2) {
                return userSearchDecision;
            }
        }
    }

}
