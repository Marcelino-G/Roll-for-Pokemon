package com.app.pokedex.Ball;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

// detail on what we use to capture pokemon

public class Ball {

    // this id is set when BallBuilder.buildBalls() runs
    // to represent the item's id in the players bag
    private int bagId;

    // represents the actual id for this item in the api (initialized and override in subclass)
    private int apiId;

    // name of ball
    private String name;

    // string description of the ball's effect
    private String short_effect;

    // this is a double that represents the ball's effect on capture rate (initialized and override in subclass)
    private double catchRate;

    // the amount of balls we want to provide the player with (initialized and override in subclass)
    private int inventory;

    // dives into the effect_entries api array until we reach the desired
    // short_effect property and assign it here
    @JsonProperty("effect_entries")
    private void effect_entriesDive(JsonNode[] effect_entries) {
        this.short_effect = effect_entries[0].get("short_effect").asText();
    }

    public int getBagId() {
        return bagId;
    }

    public void setBagId(int bagId) {
        this.bagId = bagId;
    }

    public int getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    public String getShort_effect() {
        return short_effect;
    }

    public double getCatchRate() {
        return catchRate;
    }

    public int getInventory() {
        return inventory;
    }

    // we update everytime we use a ball
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

}
