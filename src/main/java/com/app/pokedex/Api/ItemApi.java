package com.app.pokedex.Api;

import com.app.pokedex.Ball.Ball;
import org.springframework.web.client.RestTemplate;

public class ItemApi {

    private final String itemApiPath = "https://pokeapi.co/api/v2/item/";

    public Ball requestBall(Ball ball) {

        RestTemplate restTemplate = new RestTemplate();
        Ball ballResponse = restTemplate.getForObject(itemApiPath + ball.getApiId() + "/", ball.getClass());
        return ballResponse;
    }

}
