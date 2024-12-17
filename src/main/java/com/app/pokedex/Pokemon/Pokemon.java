package com.app.pokedex.Pokemon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

public class Pokemon {

    private String name;
    private String type;
    private int statDefense;
    private String mainSprite;
    private HashMap<String, Object> sprites;

    @JsonProperty("types")
    private void typesDive(JsonNode[] types){
        this.type = types[0].get("type").get("name").asText();
    }

    @JsonProperty("stats")
    private void statsDive(JsonNode[] stats){
        this.statDefense = stats[2].get("base_stat").asInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatDefense() {
        return statDefense;
    }

    public void setStatDefense(int statDefense) {
        this.statDefense = statDefense;
    }

    public String getMainSprite() {

        if (sprites != null && sprites.containsKey("front_default")) {
            this.mainSprite = sprites.get("front_default").toString();
        }
        return mainSprite;
    }

    public void setMainSprite(String mainSprite) {
        this.mainSprite = mainSprite;
    }

    public HashMap<String, Object> getSprites() {
        return sprites;
    }

    public void setSprites(HashMap<String, Object> sprites) {
        this.sprites = sprites;
    }
}
