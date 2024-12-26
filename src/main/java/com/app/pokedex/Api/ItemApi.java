package com.app.pokedex.Api;

import com.app.pokedex.Ball.Ball;
import org.springframework.web.client.RestTemplate;

// api that makes requests for data related to 'items' in the pokemon world

public class ItemApi {


    private final String itemApiPath = "https://pokeapi.co/api/v2/item/";
    private final RestTemplate restTemplate = new RestTemplate();

    // requests a certain ball depending on it's apiId.
    // these balls are all subclasses that extend the Ball class
    // so that specific (sub)ball class is returned
    public Ball requestBall(Ball ball) {


        Ball ballResponse = restTemplate.getForObject(itemApiPath + ball.getApiId() + "/", ball.getClass());
        return ballResponse;
    }

}
