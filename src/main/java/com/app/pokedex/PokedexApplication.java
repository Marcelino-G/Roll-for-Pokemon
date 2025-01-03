package com.app.pokedex;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Api.PokemonApi;
import com.app.pokedex.Ball.Ball;
import com.app.pokedex.Ball.BallBuilder;
import com.app.pokedex.Exception.NotAChoiceException;
import com.app.pokedex.Exception.PokemonNotFoundException;

import com.app.pokedex.Export.Excel.PokemonServiceExcel;
import com.app.pokedex.Pokemon.Pokemon;
import com.app.pokedex.RandomNumberMaker.RandomNumberMaker;
import com.app.pokedex.Export.Sql.PokemonServiceSql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
public class PokedexApplication implements CommandLineRunner {

    // related to creating a sql database
    @Autowired
    private PokemonServiceSql pokemonService;

    // pokemon are added to this list with each successful roll.
    private ArrayList<Pokemon> pokedex = new ArrayList<>();

    // increments everytime a pokemon is captured and sets the pokemons id.
    // seen when exporting data.
    int pokemonId = 0;

    private final PokemonApi pokemonApi = new PokemonApi();
    private final ItemApi itemApi = new ItemApi();
    private final BallBuilder ballBuilder = new BallBuilder(itemApi);

    // all the types of balls that extend the Ball class.
    private ArrayList<Ball> balls = ballBuilder.buildBalls();

    private final Scanner userInput = new Scanner(System.in);
    private final RandomNumberMaker randomNumberMaker = new RandomNumberMaker();

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
            The minimum possible number roll you can get is 5 while the maximum is 100 with a 
            regular Poke Ball. Use special Poke Balls to boost your roll and chances of a capture.
                        
            Check your bag to view your Poke Balls in the "Play game" menu.
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
    }

    @Override
    public void run(String... args) {
        app();
        System.exit(0);
    }

    public void app() {

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
                                String userInputPokemon = userInput.nextLine().toLowerCase().trim().replace(" ", "-");

                                // userInputPokemon cannot be an empty string
                                if (userInputPokemon.isEmpty()) {

                                    System.out.println("Input cannot be blank. Please enter a valid Pokemon name. \n");
                                    continue;  // Skip to the next iteration of the loop to prompt again

                                } else if (userInputPokemon.matches("\\d+")) {

                                    // a number is valid for the api but we want names (String) only. id (number) = pokemon
                                    System.out.println("Input cannot be a number. Please enter a valid Pokemon name. \n");
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

                                    Ball chosenBall = null;

                                    while (true) {

                                        // a try|catch catches a NotAChoiceException when
                                        // an invalid input is entered.
                                        try {
                                            chosenBall = choosePokeballPrompt();

                                        } catch (NotAChoiceException ex) {
                                            System.out.println(ex.getMessage());

                                        }

                                        if (chosenBall == null) {

                                            // a "0" user response to choosePokeballPrompt()
                                            // will return a null 'chosenBall'
                                            break;

                                        } else if (chosenBall != null) {

                                            // acceptable chosen ball, but we want to rerun the loop
                                            // and choose a ball we actually have inventory of
                                            if (chosenBall.getInventory() == 0) {

                                                String ballName = chosenBall.getName().replace("-", " ");
                                                System.out.println(("""
                                                         You have 0 %s's left.
                                                         Choose a different Poke Ball to use.

                                                        """.formatted(ballName)));

                                                continue;

                                            } else {
                                                break;

                                            }
                                        }
                                    }

                                    if (chosenBall == null) {
                                        // userBallChoice = null means that "0" was entered in
                                        // the choosePokeballPrompt() which means to exit the loop within the function
                                        // and back here, keeping the chosenBall null and back to play game menu with this break
                                        break;

                                    }

                                    capturePokemon(pokemon, chosenBall);

                                }

                            } catch (PokemonNotFoundException ex) {
                                System.out.println(ex.getMessage());

                            }

                            // returns an int that shows your interest in
                            // doing another search. 1 = another search
                            // 2 = breaks the search for pokemon loop
                            int userSearchDecision = searchAgainPrompt();

                            if (userSearchDecision == 2) {
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

                            // a random number is generated and inserted into the .requestPokemonById()
                            // because a name can also be search by id
                            int randomPokemonId = randomNumberMaker.makeRandomNumberPokemonId();
                            Pokemon pokemon = pokemonApi.requestPokemonById(randomPokemonId);

                            // runs through the rollForPokemonPrompt, and returns an int
                            // the represents your interest in pursuing it based on it's
                            // displayed stats.
                            int userRollChoice = rollForPokemonPrompt(pokemon);

                            // '1' means yes, and you proceed to choose the ball you want to use
                            if (userRollChoice == 1) {

                                Ball chosenBall = null;

                                while (true) {

                                    // a try|catch catches a NotAChoiceException when
                                    // an invalid input is entered.
                                    try {
                                        chosenBall = choosePokeballPrompt();

                                    } catch (NotAChoiceException ex) {
                                        System.out.println(ex.getMessage());

                                    }

                                    if (chosenBall == null) {

                                        // a "0" user response to choosePokeballPrompt()
                                        // will return a null 'chosenBall'
                                        break;

                                    } else if (chosenBall != null) {

                                        // acceptable chosen ball but we want to rerun the loop
                                        // and choose a ball we actually have inventory of
                                        if (chosenBall.getInventory() == 0) {

                                            String ballName = chosenBall.getName().replace("-", " ");
                                            System.out.println(("""
                                                     You have 0 %s's left.
                                                     Choose a different Poke Ball to use.

                                                    """.formatted(ballName)));

                                            continue;

                                        } else {

                                            break;

                                        }
                                    }

                                }

                                if (chosenBall == null) {
                                    // userBallChoice = null means that "0" was entered in
                                    // the choosePokeballPrompt() which means to exit the loop within the function
                                    // and back here, keeping the chosenBall null and back to play game menu with this break
                                    break;

                                }

                                capturePokemon(pokemon, chosenBall);

                            }

                            // returns an int that shows your interest in
                            // doing another search. 1 = another search
                            // 2 = breaks the search for pokemon loop
                            int userSearchDecision = searchAgainPrompt();

                            if (userSearchDecision == 2) {
                                break;

                            }

                        }
                    } else if (userPlayGameChoice == 0) {
                        // returns to main menu
                        break;

                    }

                }
            } else if (userMenuChoice == 3) {

                // makes sure you even have pokemon to save
                if (pokedex.isEmpty()) {
                    System.out.println("Catch some Pokemon first!");

                } else {

                    while (true) {

                        System.out.println("""
                                What type of file would you like to save as?
                                1. Sql
                                2. Excel
                                0. Cancel
                                                        
                                """);

                        int saveTypeDecision = userInputChoiceSelected(2);

                        if (saveTypeDecision == 1) {

                            File databaseFile = new File("mydatabase.db");

                            while (true) {

                                // we will drop the only table within the database
                                // and create a new table with fresh new data each session
                                // so we can make a clean sql file to export
                                System.out.println("""
                                        Any existing databases will be overwritten.
                                        Would you like to continue?
                                        1. Yes
                                        2. No

                                        """);

                                int userSaveChoice = userInputChoiceSelected(2);

                                if (userSaveChoice == 1) {

                                    // if it exists, we drop the only table inside of it
                                    // so we can have a clean exported (sql, excel) save
                                    // with each new app run, getting rid of any lingering data from other sessions
                                    if (databaseFile.exists()) {
                                        pokemonService.dropTable();
                                    }

                                    pokemonService.createTable();

                                    for (Pokemon pokemon : pokedex) {
                                        pokemonService.addPokemon(pokemon);
                                    }

                                    String fileName = nameYourFilePrompt(".sql", false);

                                    if (fileName.isEmpty()) {
                                        // "0" was entered in the nameyourfileprompt() to exit the loop
                                        break;
                                    }

                                    pokemonService.exportTableToSqlFile(fileName);

                                    break;

                                } else if (userSaveChoice == 2) {

                                    // returns us back "what type of file" prompt
                                    break;

                                }

                            }

                        } else if (saveTypeDecision == 2) {

                            PokemonServiceExcel pokemonServiceExcel = new PokemonServiceExcel();

                            String fileName = nameYourFilePrompt(".xlsx", false);

                            if (fileName.isEmpty()) {
                                // "0" was entered in the nameyourfileprompt() to exit the loop
                                break;
                            }

                            pokemonServiceExcel.exportDataToExcelFile(fileName, pokedex);

                            break;

                        } else if (saveTypeDecision == 0) {
                            // returns us back to main menu
                            break;

                        }

                    }

                }

            } else if (userMenuChoice == 0) {

                // ends the app() function
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

        StringBuilder bagInventoryStringBuilder = new StringBuilder();

        bagInventoryStringBuilder.append("""
                ----------------------------
                BAG
                ----------------------------
                """);

        bagInventoryStringBuilder.append(String.format(
                "%-3s|| %-15s || %-9s || %-100s\n\n",
                "Id", "Item", "Quantity", "Effect"));

        for (Ball ball : balls) {

            int ballId = ball.getBagId();
            String ballName = ball.getName().replace("-", " ");
            int ballInventory = ball.getInventory();
            String ballEffect = ball.getShort_effect();

            bagInventoryStringBuilder.append(String.format(
                    "%-3s|| %-15s || %-9s || %-100s\n",
                    ballId, ballName, ballInventory, ballEffect
            ));

        }

        System.out.println(bagInventoryStringBuilder);

    }

    // displays your pokedex (pokemon) with a for loop to keep track of
    // all the pokemon you've caught and their information.
    public void displayPokedex(ArrayList<Pokemon> pokedex) {

        StringBuilder pokedexStringBuilder = new StringBuilder();

        pokedexStringBuilder.append("""
                ----------------------------
                POKEDEX
                ----------------------------
                """);

        pokedexStringBuilder.append(String.format(
                "%-4s || %-25s || %-15s || %-15s || %-100s\n\n",
                "Id", "Pokemon", "Type", "Defense stat", "URL Picture"));

        for (Pokemon pokemon : pokedex) {

            int pokemonId = pokemon.getIdApp();
            String pokemonName = pokemon.getName().substring(0, 1).toUpperCase() + pokemon.getName().substring(1);
            String pokemonType = pokemon.getType().substring(0, 1).toUpperCase() + pokemon.getType().substring(1);
            String pokemonSprite = pokemon.getMainSprite();
            int pokemonDefenseStat = pokemon.getStatDefense();

            pokedexStringBuilder.append(String.format(
                    "%-4s || %-25s || %-15s || %-15s || %-100s\n",
                    pokemonId, pokemonName, pokemonType, pokemonDefenseStat, pokemonSprite));

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

            System.out.println("Choose a Poke Ball to use: \n");
            displayBagInventory(balls);
            System.out.println("0. Exit");

            // ball.size() is used to represent the highestChoiceNum argument in
            // userInputChoiceSelected()... because the highest number option equals it's size.
            userBallChoice = userInputChoiceSelected(balls.size());

            if (userBallChoice == 0) {
                break;

            }

            // if a user enters a number above balls.size() (highestChoiceNum)
            // it throws and displays its exception message but keeps running in this case.
            // userInputChoiceSelected() also returns -1 along with this exception.
            // if this occurs, this 'if' statement prevents from trying
            // to get an index that doesn't exist within the 'balls' arraylist
            if (userBallChoice != -1) {

                chosenBall = balls.get(userBallChoice - 1);

                break;

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

            pokemonId++;
            pokemon.setIdApp(pokemonId);
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

    // user input filename is what is returned and the fileextension parameter
    // is inserted to the end of it. the paramter is dependent on the users
    // save type preference and then the "if" statement it falls under.
    // ISTESTING IS FOR POKEDEXAPPLICATIONTESTS PURPOSES. THESE SHOULD ALL
    // BE SET TO FALSE HERE WITHIN THE ACTUAL APPLICATION
    public String nameYourFilePrompt(String fileExtension, boolean isTesting) {

        String fileName = "";

        while (true) {

            System.out.println("\n\nPlease name your file:");
            fileName = userInput.nextLine().replace(" ", "_");

            File fileToSave = new File(fileName + fileExtension);

            // we want to warn the user that the file (name) already exists
            // and that if we proceed, we overwrite it
            // TEST CANT CREATE FILES SINCE THIS IS JUST ABOUT NAMING THE FILE.
            // SO WE FORCE THE TEST INTO THIS STATEMENT WITH THE ISTESTING BOOLEAN
            if (fileToSave.exists() || isTesting) {

                System.out.printf("""

                        %s already exists.
                        Would you like to overwrite it?
                        1. Yes
                        2. No
                        0. Exit
                        """, fileToSave);

                int userOverwriteFileChoice = userInputChoiceSelected(2);

                if (userOverwriteFileChoice == 1) {
                    break;

                } else if (userOverwriteFileChoice == 2) {
                    continue;
                } else if (userOverwriteFileChoice == 0) {
                    // an empty filename will break the next loop to exit to previous menu
                    fileName = "";
                    break;
                }

            } else if (!fileToSave.exists()) {
                break;
            }

        }

        return fileName;

    }

}
