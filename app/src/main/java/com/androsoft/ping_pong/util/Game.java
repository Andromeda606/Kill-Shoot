package com.androsoft.ping_pong.util;

import android.app.Activity;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.content.res.AppCompatResources;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.constant.Player;


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
