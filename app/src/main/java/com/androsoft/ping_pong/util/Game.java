package com.androsoft.ping_pong.util;

public class Game {
    public static int toEnd() {
        return Device.getScreenWidth();
    }
    public static int toStart() {
        return 0;
    }
    public static int toAngle(int value, float data) {
        return (int) (value * data / 100);
    }
}
