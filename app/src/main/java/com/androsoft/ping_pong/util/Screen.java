package com.androsoft.ping_pong.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class Screen {

    public static float widthToAngle(float px){
        return 100f - ((px / Device.getScreenWidth()) * 100);
    }

    public static float angleToWidth(float angle){
        return angle / 100 * Device.getScreenWidth();
    }

    public static float heightToAngle(float px){
        return 100f - (px / Device.getScreenHeight() * 100);
    }

    public static float angleToHeight(float angle){
        return angle / 100 * Device.getScreenHeight();
    }

    public static int pxToDp(Context context, float dp){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}
