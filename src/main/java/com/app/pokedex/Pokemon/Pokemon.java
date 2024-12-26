package com.app.pokedex.Pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

// information on the pokemon the player is trying to capture

public class Pokemon {

    private String name;
    private String type;

    // represents the pokemon's defense stat level
    private int statDefense;

    // the picture used to represent the pokemon
    private String mainSprite;

    // hashmap of pictures (sprites) returned from api
    private HashMap<String, Object> sprites;

    // dives into the 'types' array to return the
    // first (0) type name in the array and assign it here
    @JsonProperty("types")
    private void typesDive(JsonNode[] types) {
        this.type = types[0].get("type").get("name").asText();
    }

    // dives into the 'stats' array to get the 'base_stat' (int)
    // that represents 'defense' and assign it here
    @JsonProperty("stats")
    private void statsDive(JsonNode[] stats) {
        this.statDefense = stats[2].get("base_stat").asInt();
    }

    // assigns and gets the picture (sprite) we want to use from the sprites hashmap
    public String getMainSprite() {

        if (sprites != null && sprites.containsKey("front_default")) {
            this.mainSprite = sprites.get("front_default").toString();
        }
        return mainSprite;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getStatDefense() {
        return statDefense;
    }

    public HashMap<String, Object> getSprites() {
        return sprites;
    }
}
