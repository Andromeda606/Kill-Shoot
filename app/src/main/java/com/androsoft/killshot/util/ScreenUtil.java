package com.androsoft.killshot.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ScreenUtil {

    public static float widthToAngle(float px){
        return 100f - ((px / DeviceUtil.getScreenWidth()) * 100);
    }

    public static float angleToWidth(float angle){
        return angle / 100 * DeviceUtil.getScreenWidth();
    }

    public static float heightToAngle(float px){
        return 100f - (px / DeviceUtil.getScreenHeight() * 100);
    }

    public static float angleToHeight(float angle){
        return angle / 100 * DeviceUtil.getScreenHeight();
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
