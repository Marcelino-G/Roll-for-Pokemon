package com.app.pokedex.Ball;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class Ball {

    // this id is set when BallBuilder.buildBalls() runs
    // to represent the item's id in the players bag
    private int bagId;
    // represents the actual id for this item in the api
    private int apiId;
    // name of ball
    private String name;
    // string description of the ball's effect
    private String short_effect;
    // this is a double that represents the ball's effect on capture rate
    private double catchRate;
    // the amount of balls we want to provide the player with
    private int inventory;

    public double getCatchRate() {
        return catchRate;
    }

    @JsonProperty("effect_entries")
    private void effect_entriesDive(JsonNode[] effect_entries) {
        this.short_effect = effect_entries[0].get("short_effect").asText();
    }

    public int getBagId() {
        return bagId;
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

    public int getInventory() {
        return inventory;
    }

    public void setBagId(int bagId) {
        this.bagId = bagId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShort_effect(String short_effect) {
        this.short_effect = short_effect;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
