package com.androsoft.killshot.physics;

import android.app.Activity;
import android.graphics.Rect;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androsoft.killshot.constant.Player;
import com.androsoft.killshot.fragment.GameScreenFragment;
import com.androsoft.killshot.util.DeviceUtil;
import com.androsoft.killshot.view.BulletImage;
import com.androsoft.killshot.view.PlayerImage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BulletPhysics {
    public static ArrayList<BulletImage> XY_POSITIONS = new ArrayList<>();
    GameScreenFragment gameScreenFragment;
    FrameLayout rootLayout;
    Player.Type playerType;

    public BulletPhysics(GameScreenFragment gameScreenFragment, Player.Type playerType, FrameLayout rootLayout) {
        this.gameScreenFragment = gameScreenFragment;
        this.rootLayout = rootLayout;
        this.playerType = playerType;
    }

    public FrameLayout getRootLayout() {
        return rootLayout;
    }

    public Player.Type getPlayerType() {
        return playerType;
    }


    public BulletImage createBullet(int bulletImageResId) {
        BulletImage bulletImage = new BulletImage(gameScreenFragment.requireActivity());
        bulletImage.setImageResource(bulletImageResId);
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

    public Activity getActivity() {
        return gameScreenFragment.requireActivity();
    }

    public void removeScreen(BulletImage bulletImage) {
        FrameLayout gameArea = getRootLayout();
        bulletImage.setStatus(false);
        gameScreenFragment.requireActivity().runOnUiThread(() -> gameArea.removeView(bulletImage));
    }

    public PlayerImage getEnemy(Player.Type playerType){
        return gameScreenFragment.playerToOtherImage(playerType);
    }

    /**
     * Syncing all bullets <br>
     * Timer working 10 ms and conflicts find
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

                    for (int i = 0; i < XY_POSITIONS.size(); i++) {
                        BulletImage bulletImage = XY_POSITIONS.get(i);
                        if (!bulletImage.getStatus()) {
                            XY_POSITIONS.remove(i--);
                            continue;
                        }

                        Rect bulletRect = new Rect();
                        bulletImage.getHitRect(bulletRect);
                        if (bulletImage.getPlayerType() == Player.Type.PLAYER1 && Rect.intersects(currentPlayerRect, bulletRect)) {
                            // is player
                            XY_POSITIONS.remove(i--);
                            rootLayout.removeView(bulletImage);
                            currentPlayer.decraseHealth(currentPlayer.getCharacter().getDamage());
                            DeviceUtil.vibrate(gameScreenFragment.requireContext(), 500);
                        } else if (bulletImage.getPlayerType() == Player.Type.PLAYER2 && Rect.intersects(enemyPlayerRect, bulletRect)) {
                            // is enemy
                            XY_POSITIONS.remove(i--);
                            rootLayout.removeView(bulletImage);
                            enemyPlayer.decraseHealth(enemyPlayer.getCharacter().getDamage());
                        }
                        gameScreenFragment.updateHealths();
                    }
                });
            }
        }, 10, 10);
    }

}

