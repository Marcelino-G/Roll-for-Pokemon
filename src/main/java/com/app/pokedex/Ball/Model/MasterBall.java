package com.app.pokedex.Ball.Model;

import com.app.pokedex.Ball.Ball;

public class MasterBall extends Ball {

    private final int apiId = 1;
    private int inventory = 1;
    private final double catchRate = 10.0;

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

    // we update everytime we use a ball
    @Override
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
