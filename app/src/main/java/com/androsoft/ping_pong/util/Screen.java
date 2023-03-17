package com.androsoft.ping_pong.util;

public class Screen {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;


    public static float widthToAngle(float px){
        return 100f - ((px / SCREEN_WIDTH) * 100);
    }

    public static float angleToWidth(float angle){
        return angle / 100 * SCREEN_WIDTH;
    }

    public static float heightToAngle(float px){
        return 100f - (px / SCREEN_HEIGHT * 100);
    }

    public static float angleToHeight(float angle){
        return angle / 100 * SCREEN_HEIGHT;
    }
}
