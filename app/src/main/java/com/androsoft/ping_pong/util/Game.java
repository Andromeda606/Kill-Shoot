package com.androsoft.ping_pong.util;

import android.app.Activity;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.constant.Player;

import static com.androsoft.ping_pong.physics.BulletPhysics.GLOBAL_IMAGE;

public class Game {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static View CURRENT_VIEW;
    public static Player.Type PLAYER_TYPE;

    public static int ENEMY_HEALTH = 100;

    public static int PLAYER_HEALTH = 100;

    public static void init(Activity activity) {
        GLOBAL_IMAGE = AppCompatResources.getDrawable(activity, R.drawable.ic_android_black_24dp);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        Screen.SCREEN_HEIGHT = displayMetrics.heightPixels;

        Screen.SCREEN_WIDTH =displayMetrics.widthPixels;
    }

    public static int toEnd() {
        return SCREEN_WIDTH;
    }

    public static int toStart() {
        return 0;
    }

    public static int toAngle(int value, float data) {
        return (int) (value * data / 100);
    }

    public static float calculateAngle(float x, float y) {
        return (y / x) * 100;
    }
    //todo OHHA HER ŞEYİ ACTIVITYE ALABİLİRSİN MEMORY LEAK ÖNLENİR
    public static ImageView getCurrentPlayer() {
        if (Player.Type.PLAYER2 == PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.player2);
        } else {
            return CURRENT_VIEW.findViewById(R.id.player1);
        }
    }

    public static String DEVICE_NAME;

    public static ImageView getEnemyPlayer() {
        if (Player.Type.PLAYER2 != PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.player2);
        } else {
            return CURRENT_VIEW.findViewById(R.id.player1);
        }
    }

}
