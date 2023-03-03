package com.androsoft.ping_pong.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import androidx.appcompat.content.res.AppCompatResources;
import com.androsoft.ping_pong.R;

import static com.androsoft.ping_pong.physics.BulletPhysics.GLOBAL_IMAGE;

public class Game {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static void init(Activity activity){
        GLOBAL_IMAGE = AppCompatResources.getDrawable(activity, R.drawable.ic_android_black_24dp);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
    }

    public static int toEnd(int width){
        return SCREEN_WIDTH - width;
    }

    public static int toStart(){
        return 0;
    }

    public static int toAngle(int value, float data){
        return (int) (value * data / 100);
    }

}
