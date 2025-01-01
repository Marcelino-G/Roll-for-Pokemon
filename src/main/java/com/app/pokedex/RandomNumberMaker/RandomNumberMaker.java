package com.app.pokedex.RandomNumberMaker;

import java.util.Random;

// makes random numbers that we plug in else where

public class RandomNumberMaker {

    private final Random random = new Random();

    // the minimum pokemon id that exists within the api used
    private final int minPokemonId = 1;

    // the max pokemon id this application can request from
    // the api used (there are more id's within api)
    private final int maxPokemonId = 1025;

    // the minimum roll a player can get.
    // based on pokemon with the lowest defense stats (chansey) is 5.
    // these are their defense stat according to the api.
    private final int minRollAmount = 5;

    // the maximum roll a player can get.
    // based on one of the strongest pokemon (groudon) the regular (1x catch rate)
    // pokeball can catch. 140 is its defense stat according to the api. this is a reference, we lowered to 100.
    // you would then be required to use a pokeball with special effects (increased catch rate)
    // to catch a stronger pokemon.
    private final int maxRollAmount = 100;

    // makes a random number (random pokemon id) in range that is usable by the api
    public int makeRandomNumberPokemonId() {

        int randomNumberPokemonId = random.nextInt(maxPokemonId - minPokemonId + 1) + minPokemonId;

        return randomNumberPokemonId;
    }

    // makes a random number (players roll) between the weakest pokemon in regard to base defense stat
    // and the strongest pokemon base defense stat that can be caught with a regular (1x catch rate) pokeball
    public int makeRandomNumberRoll() {

        int randomNumberRoll = random.nextInt(maxRollAmount - minRollAmount + 1) + minRollAmount;

        return randomNumberRoll;
    }


}
