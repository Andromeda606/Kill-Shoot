package com.androsoft.ping_pong.constant.character;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.ImageView;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.view.BulletImage;

import java.util.Timer;
import java.util.TimerTask;

public class Tringle extends Character {
    private final int movementSpeed, seperatorSpeed;

    public Tringle(
            BulletPhysics bulletPhysics,
            int health,
            int damage,
            int duration,
            int characterImage,
            int bulletImage,
            int movementSpeed,
            int seperatorSpeed,
            int title,
            int description
    ) {
        super(bulletPhysics, health, damage, duration, characterImage, bulletImage, title, description);
        this.seperatorSpeed = seperatorSpeed;
        this.movementSpeed = movementSpeed;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getSeperatorSpeed() {
        return seperatorSpeed;
    }

    @Override
    public void shoot() {
        Timer times = new Timer();
        final int[] timeCount = {0}; // final variable, java is trash
        final AnimatorSet[] animatior = {null}; // another final variable, java is trash
        BulletImage bullet = getBulletPhysics().createBullet(getBulletImage());
        ImageView enemyImage = getBulletPhysics().getEnemy(getCharacterType());
        times.schedule(new TimerTask() {
            @SuppressLint("Recycle")
            @Override
            public void run() {
                getBulletPhysics().getActivity().runOnUiThread(() -> {
                    if (timeCount[0]++ == 3) {
                        // Eğer 3 kere arama yapıldı ve bulunmadıysa ekrandan kaldır
                        times.purge();
                        times.cancel();
                        if (animatior[0] != null) {
                            animatior[0].cancel();
                            animatior[0].end();
                        }
                        getBulletPhysics().removeScreen(bullet);
                        bullet.setStatus(false);
                        return;
                    }
                    if (animatior[0] != null) {
                        animatior[0].cancel();
                        animatior[0].end();
                    }
                    ObjectAnimator animX = ObjectAnimator.ofFloat(bullet, "translationX", enemyImage.getX());
                    ObjectAnimator animY = ObjectAnimator.ofFloat(bullet, "translationY", enemyImage.getY());
                    animX.setDuration(getMovementSpeed());
                    double angle = Math.toDegrees(Math.atan2(bullet.getY() - enemyImage.getY(), bullet.getX() - enemyImage.getX()));
                    Log.wtf("angle", String.valueOf(angle));
                    ObjectAnimator animZ = ObjectAnimator.ofFloat(bullet, "rotation", (int) (180 - angle));
                    animY.setDuration(getMovementSpeed());
                    animZ.setDuration(200);
                    animatior[0] = new AnimatorSet();
                    animatior[0].playTogether(animX, animY, animZ);
                    animatior[0].start();
                });

            }
        }, 0, getSeperatorSpeed());
        BulletPhysics.XY_POSITIONS.add(bullet);
    }

}
