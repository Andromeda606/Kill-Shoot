package com.androsoft.ping_pong.physics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androsoft.ping_pong.GameScreen;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.constant.Bullet;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.element.BulletImage;
import com.androsoft.ping_pong.util.Game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BulletPhysics {
    public static ArrayList<BulletImage> xyPositions = new ArrayList<>();
    GameScreen gameScreen;
    View rootLayout;
    Player.Type playerType;
    Character.Type characterType;

    public BulletPhysics(GameScreen gameScreen, Player.Type playerType, Character.Type characterType, View rootLayout) {
        this.gameScreen = gameScreen;
        this.rootLayout = rootLayout;
        this.playerType = playerType;
        this.characterType = characterType;
    }

    public void shoot() {
        //shootTring(2000, 1000);
shootGunner(1000);

        Log.wtf("SHOOT", "VİEW ADD YAPILDI");
    }

    public void shoot(StreamController streamController) {
        streamController.sendMessage("SHOOT");
        shoot();
    }


    private BulletImage createBullet() {
        BulletImage bulletImage = new BulletImage(gameScreen.requireActivity());
        bulletImage.setImageResource(R.drawable.ic_game_bullet1);
        bulletImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        if (Player.Type.PLAYER2 == playerType)
            bulletImage.setRotation(180);
        bulletImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bulletImage.setPlayerType(playerType);
        ImageView player = gameScreen.playerToImage(playerType);
        float startX = (float) (player.getX() + player.getWidth() / 4.0);
        float startY = (float) (player.getY() + (player.getHeight() / 4.0));
        bulletImage.setTranslationX(startX);
        bulletImage.setTranslationY(startY);
        return bulletImage;
    }

    protected void shootGunner(int speed) {
        FrameLayout gameArea = (FrameLayout) rootLayout;
        BulletImage bulletImage = createBullet();
        gameArea.addView(bulletImage);

        if (bulletImage.getParent() != null) {
            ((ViewGroup) bulletImage.getParent()).removeView(bulletImage);
        }
        gameArea.addView(bulletImage);
        int endX = Game.toEnd();

        if (playerType == Player.Type.PLAYER2) {
            endX = Game.toStart();
        }

        ObjectAnimator animation = ObjectAnimator.ofFloat(bulletImage, "translationX", endX);
        animation.setDuration(speed);
        animation.start();
        xyPositions.add(bulletImage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameScreen.requireActivity().runOnUiThread(() -> {
                    bulletImage.setStatus(false);
                    ((FrameLayout) rootLayout).removeView(bulletImage);
                });
            }
        }, speed);
    }

    protected void shootTring(int movementSpeed, int seperatorSecond) {
        Timer times = new Timer();
        final int[] timeCount = {0}; // final variable, java is trash
        final AnimatorSet[] animatior = {null}; // another final variable, java is trash
        BulletImage bullet = createBullet();
        ImageView enemyImage = gameScreen.playerToOtherImage(playerType);
        ((FrameLayout) rootLayout).addView(bullet);
        times.schedule(new TimerTask() {
            @Override
            public void run() {
                gameScreen.requireActivity().runOnUiThread(() -> {
                    if (timeCount[0]++ == 3) {
                        times.purge();
                        times.cancel();
                        if (animatior[0] != null) {
                            animatior[0].cancel();
                            animatior[0].end();
                        }
                        ((FrameLayout) rootLayout).removeView(bullet);
                        return;
                    }
                    if (animatior[0] != null) {
                        animatior[0].cancel();
                        animatior[0].end();
                    }
                    ObjectAnimator animX = ObjectAnimator.ofFloat(bullet, "translationX", enemyImage.getX());
                    ObjectAnimator animY = ObjectAnimator.ofFloat(bullet, "translationY", enemyImage.getY());
                    animatior[0] = new AnimatorSet();
                    animatior[0].playTogether(animX, animY);
                    animatior[0].setDuration(movementSpeed);
                    animatior[0].start();
                });

            }
        }, 0, seperatorSecond);

        times.schedule(new TimerTask() {
            @Override
            public void run() {

            }
        }, 10, 10);
    }

    /**
     * Syncing all bullets <br>
     * Timer working 10 ms and collactions find
     */
    public static void syncBullets(GameScreen gameScreen) {
        ImageView enemyPlayer = gameScreen.getEnemyPlayer();
        ImageView currentPlayer = gameScreen.getCurrentPlayer();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameScreen.requireActivity().runOnUiThread(() -> {
                    Rect enemyPlayerRect = new Rect();
                    Rect currentPlayerRect = new Rect();
                    enemyPlayer.getHitRect(enemyPlayerRect);
                    currentPlayer.getHitRect(currentPlayerRect);

                    for (int i = 0; i < xyPositions.size(); i++) {
                        BulletImage bulletImage = xyPositions.get(i);
                        if (!bulletImage.getStatus()) {
                            xyPositions.remove(i--);
                        }

                        Rect bulletRect = new Rect();
                        bulletImage.getHitRect(bulletRect);
                        if (bulletImage.getPlayerType() == Player.Type.PLAYER1 && Rect.intersects(currentPlayerRect, bulletRect)) {
                            // is enemy
                            xyPositions.remove(i--);
                            gameScreen.CURRENT_HEALTH -= 10;
                        } else if (bulletImage.getPlayerType() == Player.Type.PLAYER2 && Rect.intersects(enemyPlayerRect, bulletRect)) {
                            // is player
                            xyPositions.remove(i--);
                            gameScreen.ENEMY_HEALTH -= 10;
                        }
                        gameScreen.updateHealths();
                    }
                });
            }
        }, 10, 10);
    }

}

