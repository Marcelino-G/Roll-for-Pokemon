package com.app.pokedex.Ball;

import com.app.pokedex.Api.ItemApi;
import com.app.pokedex.Ball.Model.GreatBall;
import com.app.pokedex.Ball.Model.MasterBall;
import com.app.pokedex.Ball.Model.PokeBall;
import com.app.pokedex.Ball.Model.UltraBall;

import java.util.ArrayList;

public class BallBuilder {

    private Ball[] balls = new Ball[]{new PokeBall(), new GreatBall(), new UltraBall(), new MasterBall()};
    private ArrayList<Ball> detailedBalls = new ArrayList<>();
//    private Ball[] detailedBalls = new Ball[balls.length];

    public ArrayList<Ball> buildBalls(ItemApi itemApi) {

        for (int i = 0; i < balls.length; i++) {
            detailedBalls.add(itemApi.requestBall(balls[i]));
            detailedBalls.get(i).setBagId(i + 1);
        }

        return detailedBalls;
    }
}
