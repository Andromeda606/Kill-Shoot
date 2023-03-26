package com.androsoft.ping_pong.physics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androsoft.ping_pong.fragment.GameScreenFragment;
import com.androsoft.ping_pong.connection.StreamInterface;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.view.BulletImage;
import com.androsoft.ping_pong.view.PlayerImage;
import com.androsoft.ping_pong.util.DeviceUtil;
import com.androsoft.ping_pong.util.GameUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BulletPhysics {
    public static ArrayList<BulletImage> xyPositions = new ArrayList<>();
    GameScreenFragment gameScreenFragment;
    FrameLayout rootLayout;
    Player.Type playerType;
    Character.Type characterType;

    public BulletPhysics(GameScreenFragment gameScreenFragment, Player.Type playerType, Character.Type characterType, FrameLayout rootLayout) {
        this.gameScreenFragment = gameScreenFragment;
        this.rootLayout = rootLayout;
        this.playerType = playerType;
        this.characterType = characterType;
    }

    public GameScreenFragment getGameScreen() {
        return gameScreenFragment;
    }

    public void setGameScreen(GameScreenFragment gameScreenFragment) {
        this.gameScreenFragment = gameScreenFragment;
    }

    public FrameLayout getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(FrameLayout rootLayout) {
        this.rootLayout = rootLayout;
    }

    public Player.Type getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Player.Type playerType) {
        this.playerType = playerType;
    }

    public Character.Type getCharacterType() {
        return characterType;
    }

    public void setCharacterType(Character.Type characterType) {
        this.characterType = characterType;
    }

    public void shoot() {
        Character character = Character.convertTypeToCharacter(characterType);
        switch (characterType) {
            case GUNNER:
                shootGunner(character.getMovementSpeed());
                break;
            case TRINGLE:
                shootTring(character.getMovementSpeed(), character.getSeperatorSpeed());
                break;
            case CIRCLER:
                shootCircler(character.getMovementSpeed(), character.getSeperatorSpeed());
                break;
            default:
                break;
        }

        Log.wtf("SHOOT", "VİEW ADD YAPILDI");
    }

    public void shoot(StreamInterface streamInterface) {
        streamInterface.sendMessage("SHOOT");
        shoot();
    }


    private BulletImage createBullet() {
        Character character = Character.convertTypeToCharacter(characterType);
        BulletImage bulletImage = new BulletImage(gameScreenFragment.requireActivity());
        bulletImage.setImageResource(character.getBulletImage());
        bulletImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        if (Player.Type.PLAYER2 == playerType)
            bulletImage.setRotation(180);
        bulletImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bulletImage.setPlayerType(playerType);
        ImageView player = gameScreenFragment.playerToImage(playerType);
        float startX = (float) (player.getX() + player.getWidth() / 4.0);
        float startY = (float) (player.getY() + (player.getHeight() / 4.0));
        bulletImage.setTranslationX(startX);
        bulletImage.setTranslationY(startY);
        addScreen(bulletImage);
        return bulletImage;
    }

    private void addScreen(BulletImage bulletImage) {
        FrameLayout gameArea = getRootLayout();
        if (bulletImage.getParent() != null) {
            removeScreen(bulletImage);
        }
        gameScreenFragment.requireActivity().runOnUiThread(() -> gameArea.addView(bulletImage));

    }

    private void removeScreen(BulletImage bulletImage) {
        FrameLayout gameArea = getRootLayout();
        bulletImage.setStatus(false);
        gameScreenFragment.requireActivity().runOnUiThread(() -> gameArea.removeView(bulletImage));
    }

    protected void shootGunner(int speed) {
        BulletImage bulletImage = createBullet();
        int endX = GameUtil.toEnd();

        if (playerType == Player.Type.PLAYER2) {
            endX = GameUtil.toStart();
        }

        ObjectAnimator animation = ObjectAnimator.ofFloat(bulletImage, "translationX", endX);
        animation.setDuration(speed);
        gameScreenFragment.requireActivity().runOnUiThread(animation::start);
        xyPositions.add(bulletImage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeScreen(bulletImage);
            }
        }, speed);
    }

    protected void shootTring(int movementSpeed, int seperatorSecond) {
        Timer times = new Timer();
        final int[] timeCount = {0}; // final variable, java is trash
        final AnimatorSet[] animatior = {null}; // another final variable, java is trash
        BulletImage bullet = createBullet();
        ImageView enemyImage = gameScreenFragment.playerToOtherImage(playerType);
        times.schedule(new TimerTask() {
            @Override
            public void run() {
                gameScreenFragment.requireActivity().runOnUiThread(() -> {
                    if (timeCount[0]++ == 3) {
                        times.purge();
                        times.cancel();
                        if (animatior[0] != null) {
                            animatior[0].cancel();
                            animatior[0].end();
                        }
                        removeScreen(bullet);
                        bullet.setStatus(false);
                        return;
                    }
                    if (animatior[0] != null) {
                        animatior[0].cancel();
                        animatior[0].end();
                    }
                    ObjectAnimator animX = ObjectAnimator.ofFloat(bullet, "translationX", enemyImage.getX());
                    ObjectAnimator animY = ObjectAnimator.ofFloat(bullet, "translationY", enemyImage.getY());
                    animX.setDuration(movementSpeed);
                    double angle = Math.toDegrees(Math.atan2(bullet.getY() - enemyImage.getY(), bullet.getX() - enemyImage.getX()));
                    Log.wtf("angle", String.valueOf(angle));
                    ObjectAnimator animZ = ObjectAnimator.ofFloat(bullet, "rotation", (int) (180 - angle));
                    animY.setDuration(movementSpeed);
                    animZ.setDuration(200);
                    animatior[0] = new AnimatorSet();
                    animatior[0].playTogether(animX, animY, animZ);
                    animatior[0].start();
                });

            }
        }, 0, seperatorSecond);
        xyPositions.add(bullet);
    }


    protected void shootCircler(int movementSpeed, int waitMs) {
        BulletImage bullet = createBullet();
        float bulletLastLocation = bullet.getX();
        if (playerType == Player.Type.PLAYER2) {
            bulletLastLocation -= (float) DeviceUtil.getScreenWidth() / 2;
        } else {
            bulletLastLocation += (float) DeviceUtil.getScreenWidth() / 2;
        }
        ObjectAnimator animation = ObjectAnimator.ofFloat(bullet, "translationX", bulletLastLocation);
        animation.setDuration(movementSpeed);
        animation.start();
        xyPositions.add(bullet);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeScreen(bullet);
            }
        }, waitMs + movementSpeed);
    }

    /**
     * Syncing all bullets <br>
     * Timer working 10 ms and collactions find
     */
    public static void syncBullets(GameScreenFragment gameScreenFragment, FrameLayout rootLayout) {
        PlayerImage enemyPlayer = gameScreenFragment.getEnemyPlayer();
        PlayerImage currentPlayer = gameScreenFragment.getCurrentPlayer();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameScreenFragment.requireActivity().runOnUiThread(() -> {
                    Rect enemyPlayerRect = new Rect();
                    Rect currentPlayerRect = new Rect();
                    enemyPlayer.getHitRect(enemyPlayerRect);
                    currentPlayer.getHitRect(currentPlayerRect);

                    for (int i = 0; i < xyPositions.size(); i++) {
                        BulletImage bulletImage = xyPositions.get(i);
                        if (!bulletImage.getStatus()) {
                            xyPositions.remove(i--);
                            continue;
                        }

                        Rect bulletRect = new Rect();
                        bulletImage.getHitRect(bulletRect);
                        if (bulletImage.getPlayerType() == Player.Type.PLAYER1 && Rect.intersects(currentPlayerRect, bulletRect)) {
                            // is player
                            xyPositions.remove(i--);
                            rootLayout.removeView(bulletImage);
                            currentPlayer.decraseHealth(10);
                            DeviceUtil.vibrate(gameScreenFragment.requireContext(), 500);
                        } else if (bulletImage.getPlayerType() == Player.Type.PLAYER2 && Rect.intersects(enemyPlayerRect, bulletRect)) {
                            // is enemy
                            xyPositions.remove(i--);
                            rootLayout.removeView(bulletImage);
                            enemyPlayer.decraseHealth(10);
                        }
                        gameScreenFragment.updateHealths();
                    }
                });
            }
        }, 10, 10);
    }

}

