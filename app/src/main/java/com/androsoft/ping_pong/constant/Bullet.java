package com.androsoft.ping_pong.constant;

import java.util.HashMap;

import static com.androsoft.ping_pong.constant.Character.Type.GUNNER;

public class Bullet {
    private double speed;
    public static HashMap<Character.Type, Double> BULLET_SPEEDS = new HashMap<Character.Type, Double>(){{
        put(GUNNER, 0.5);
    }};
}
