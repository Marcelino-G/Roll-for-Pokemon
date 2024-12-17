package com.app.pokedex.RandomNumberMaker;

import java.util.Random;

public class RandomNumberMaker {

    private final int minPokemonId = 1;
    private final int maxPokemonId = 1025;

    private final int minRollAmount = 30;
    private final int maxRollAmount = 110;

    private final Random random = new Random();

    public int makeRandomNumberPokemonId(){

        int randomNumberPokemonId = random.nextInt(maxPokemonId - minPokemonId + 1) + minPokemonId;

        return randomNumberPokemonId;
    }

    public int makeRandomNumberRoll(){

        int randomNumberRoll = random.nextInt(maxRollAmount - minRollAmount + 1) + minRollAmount;

        return randomNumberRoll;
    }








}
