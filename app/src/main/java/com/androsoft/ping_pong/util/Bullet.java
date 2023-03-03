package com.androsoft.ping_pong.util;

import com.androsoft.ping_pong.constant.Character;

import java.util.HashMap;

import static com.androsoft.ping_pong.constant.Character.Type.GUNNER;

public class Bullet {
    public static HashMap<Character.Type, Double> BULLET_SPEEDS = new HashMap<Character.Type, Double>(){{
        put(GUNNER, 0.5);
    }};

}
