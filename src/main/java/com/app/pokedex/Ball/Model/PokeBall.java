package com.app.pokedex.Ball.Model;

import com.app.pokedex.Ball.Ball;

public class PokeBall extends Ball {

    private final int apiId = 4;
    private int inventory = 10;
    private final double catchRate = 1.0;

    @Override
    public double getCatchRate() {
        return catchRate;
    }

    @Override
    public int getApiId() {
        return apiId;
    }

    @Override
    public int getInventory() {
        return inventory;
    }

    @Override
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
