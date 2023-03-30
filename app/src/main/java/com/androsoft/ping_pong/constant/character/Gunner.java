package com.androsoft.ping_pong.constant.character;

import android.animation.ObjectAnimator;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.GameUtil;
import com.androsoft.ping_pong.view.BulletImage;

import java.util.Timer;
import java.util.TimerTask;

public class Gunner extends Character {
    private final int bulletSpeed;
    public Gunner(
            BulletPhysics bulletPhysics,
            int health,
            int damage,
            int duration,
            int characterImage,
            int bulletImage,
            int bulletSpeed,
            int title,
            int description
    ) {
        super(bulletPhysics, health, damage, duration, characterImage, bulletImage, title, description);
        this.bulletSpeed = bulletSpeed;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    @Override
    public void shoot() {
        BulletImage bulletImage = getBulletPhysics().createBullet(getBulletImage());
        int endX = GameUtil.toEnd();

        if (getCharacterType() == Player.Type.PLAYER2) {
            endX = GameUtil.toStart();
        }

        ObjectAnimator animation = ObjectAnimator.ofFloat(bulletImage, "translationX", endX);
        animation.setDuration(getBulletSpeed());
        getBulletPhysics()
                .getActivity()
                .runOnUiThread(animation::start);
        BulletPhysics.XY_POSITIONS.add(bulletImage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getBulletPhysics().removeScreen(bulletImage);
            }
        }, getBulletSpeed());
    }
}
