package com.androsoft.ping_pong.util;

import android.util.Log;

public class Screen {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;


    public static float widthToAngle(float px){
        Log.wtf("SCREEN_WIDTH / px * 100", String
                .valueOf(SCREEN_WIDTH / px * 100));
        return (float) SCREEN_WIDTH / (SCREEN_WIDTH - px) * 100;
    }

    public static int angleToWidth(float angle){
        Log.wtf("SCREEN_WIDTH / (int) angle * 100", String.valueOf(SCREEN_WIDTH / (int) angle * 100));
        return (int) SCREEN_WIDTH - (SCREEN_WIDTH / (int) angle * 100);
    }

    public static float heightToAngle(float px){
        return (float) SCREEN_HEIGHT / px * 100;
    }

    public static int angleToHeight(float angle){
        return (int) SCREEN_HEIGHT / (int) angle * 100;
    }
}
