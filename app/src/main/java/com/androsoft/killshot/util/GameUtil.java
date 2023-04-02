package com.androsoft.killshot.util;

public class GameUtil {
    public static int toEnd() {
        return DeviceUtil.getScreenWidth();
    }
    public static int toStart() {
        return 0;
    }
    public static int toAngle(int value, float data) {
        return (int) (value * data / 100);
    }
}
