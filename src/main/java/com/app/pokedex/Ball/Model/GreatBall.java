package com.app.pokedex.Ball.Model;

import com.app.pokedex.Ball.Ball;

public class GreatBall extends Ball {

    private final int apiId = 3;
    private int inventory = 5;
    private final double catchRate = 1.5;

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
