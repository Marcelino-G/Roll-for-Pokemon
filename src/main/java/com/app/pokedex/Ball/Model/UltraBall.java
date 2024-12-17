package com.app.pokedex.Ball.Model;

import com.app.pokedex.Ball.Ball;

public class UltraBall extends Ball {

    private final int apiId = 2;
    private int inventory = 4;
    private final double catchRate = 2.0;

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
