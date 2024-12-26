package com.app.pokedex.Ball;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Ball.Model.GreatBall;
import com.app.pokedex.Ball.Model.MasterBall;
import com.app.pokedex.Ball.Model.PokeBall;
import com.app.pokedex.Ball.Model.UltraBall;

import java.util.ArrayList;

public class BallBuilder {

    private final ItemApi itemApi;

    // an array of all the subclasses that extend the Ball class
    private Ball[] balls = new Ball[]{new PokeBall(), new GreatBall(), new UltraBall(), new MasterBall()};
    private ArrayList<Ball> detailedBalls = new ArrayList<>();

    public BallBuilder(ItemApi itemApi) {
        this.itemApi = itemApi;
    }

    // builds an arraylist with detailed information for each ball subclass
    // that extends Ball class by requesting information with the ItemApi class.
    // a loop is used to run through the balls array since it holds one instance of each subclass ball.
    public ArrayList<Ball> buildBalls() {

        for (int i = 0; i < balls.length; i++) {
            detailedBalls.add(itemApi.requestBall(balls[i]));
            detailedBalls.get(i).setBagId(i + 1);
        }

        return detailedBalls;
    }
}
