package com.androsoft.killshot.constant.character;

import android.animation.ObjectAnimator;
import com.androsoft.killshot.constant.Character;
import com.androsoft.killshot.constant.Player;
import com.androsoft.killshot.physics.BulletPhysics;
import com.androsoft.killshot.util.DeviceUtil;
import com.androsoft.killshot.view.BulletImage;

import java.util.Timer;
import java.util.TimerTask;

public class Circler extends Character {
    private final int waitingMs, movementSpeed;

    public Circler(
            BulletPhysics bulletPhysics,
            int health,
            int damage,
            int duration,
            int characterImage,
            int bulletImage,
            int waitingMs,
            int movementSpeed,
            int title,
            int description
    ) {
        super(bulletPhysics, health, damage, duration, characterImage, bulletImage, title, description);
        this.waitingMs = waitingMs;
        this.movementSpeed = movementSpeed;
    }

    public int getWaitingMs() {
        return waitingMs;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    @Override
    public void shoot() {
        getBulletPhysics().getActivity().runOnUiThread(() -> {
            BulletImage bullet = getBulletPhysics().createBullet(getBulletImage());
            float bulletLastLocation = bullet.getX();
            float measure = DeviceUtil.getScreenWidth() / 2f;
            if (getCharacterType() == Player.Type.PLAYER2) {
                bulletLastLocation -= measure;
            } else {
                bulletLastLocation += measure;
            }
            ObjectAnimator animation = ObjectAnimator.ofFloat(bullet, "translationX", bulletLastLocation);
            animation.setDuration(getMovementSpeed());
            animation.start();
            BulletPhysics.XY_POSITIONS.add(bullet);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getBulletPhysics().removeScreen(bullet);
                }
            }, getWaitingMs() + getMovementSpeed());
        });

    }
}
