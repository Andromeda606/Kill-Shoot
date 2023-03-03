package com.androsoft.ping_pong.physics;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.util.Bullet;
import com.androsoft.ping_pong.util.Game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BulletPhysics {
    public static ArrayList<String> xyPositions = new ArrayList<>();
    Activity activity;
    View rootLayout;
    Player.Type playerType;
    Character.Type characterType;

    public BulletPhysics(Activity activity, Player.Type playerType, Character.Type characterType, View rootLayout) {
        this.activity = activity;
        this.rootLayout = rootLayout;
        this.playerType = playerType;
        this.characterType = characterType;
    }

    public static Drawable GLOBAL_IMAGE;

    //todo bunu create olarak yap, ardından "yakalandı" diye bir event ekle onyakalandi gibi yakalanıp yakalanmadığını sürekli bir timer kontrol edebilir
    //todo classlar oluştur ve her bullet tipini ayarla
    public void shoot() {
        shootGunner((int) (Bullet.BULLET_SPEEDS.get(characterType) * 1000));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //Log.wtf("POSITION", bulletImage.getTranslationX() + ":" + bulletImage.getTranslationY());
            }
        }, 20, 20);

        Log.wtf("SHOOT", "VİEW ADD YAPILDI");
    }


    private ImageView createBullet() {
        ImageView bulletImage = new ImageView(activity);
        bulletImage.setImageResource(R.drawable.ic_android_black_24dp);
        bulletImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        bulletImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return bulletImage;
    }

    protected void shootGunner(int speed) {
        ImageView bulletImage = createBullet();
        ((FrameLayout) rootLayout).addView(bulletImage);

        if (bulletImage.getParent() != null) {
            ((ViewGroup) bulletImage.getParent()).removeView(bulletImage);
        }
        ((FrameLayout) rootLayout).addView(bulletImage);
        ImageView player1 =  rootLayout.findViewById(R.id.player1);
        ImageView player2 =  rootLayout.findViewById(R.id.player2);
        float startX = (float) (player1.getX() / 4.0);
        int endX = Game.toEnd(bulletImage.getWidth());
        float startY = (float) (player1.getY() + (player1.getHeight() / 4.0));
        Log.wtf("startY", String.valueOf(startY));
        if (playerType == Player.Type.PLAYER2) {
            //startX = Game.toEnd(bulletImage.getWidth());
            startX = (float) (player2.getX() + player2.getWidth() / 4.0);
            startY = (float) (player2.getY() + (player2.getHeight() / 4.0));
            endX = Game.toStart();
        }

        bulletImage.setTranslationX(startX);
        bulletImage.setTranslationY(startY);
        int speedTime = 500;

        ObjectAnimator animation = ObjectAnimator.ofFloat(bulletImage, "translationX", endX);
        animation.setDuration(speedTime);
        animation.start();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((FrameLayout) rootLayout).removeView(bulletImage);
                    }
                });
            }
        }, speedTime);
    }


}

