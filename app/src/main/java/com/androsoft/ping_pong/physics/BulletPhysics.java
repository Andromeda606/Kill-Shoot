package com.androsoft.ping_pong.physics;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.bluetooth.ConnectedThread;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.element.BulletImage;
import com.androsoft.ping_pong.util.Bullet;
import com.androsoft.ping_pong.util.Game;
import com.androsoft.ping_pong.util.GameBoard;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BulletPhysics {
    public static ArrayList<BulletImage> xyPositions = new ArrayList<>();
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
    public void shoot(StreamController streamController) {
        streamController.sendMessage("SHOOT");
        shoot();
    }


    private BulletImage createBullet() {
        BulletImage bulletImage = new BulletImage(activity);
        bulletImage.setImageResource(R.drawable.ic_android_black_24dp);
        bulletImage.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        bulletImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bulletImage.setPlayerType(playerType);
        return bulletImage;
    }

    protected void shootGunner(int speed) {
        BulletImage bulletImage = createBullet();
        ((FrameLayout) rootLayout).addView(bulletImage);

        if (bulletImage.getParent() != null) {
            ((ViewGroup) bulletImage.getParent()).removeView(bulletImage);
        }
        ((FrameLayout) rootLayout).addView(bulletImage);
        ImageView player1 = rootLayout.findViewById(R.id.player1);
        ImageView player2 = rootLayout.findViewById(R.id.player2);
        float startX = (float) (player1.getX() + player1.getWidth() / 4.0);
        int endX = Game.toEnd();
        float startY = (float) (player1.getY() + (player1.getHeight() / 4.0));

        if (playerType == Player.Type.PLAYER2) {
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
        xyPositions.add(bulletImage);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    bulletImage.setStatus(false);
                    ((FrameLayout) rootLayout).removeView(bulletImage);
                });
            }
        }, speedTime);
    }

    public void syncBullets(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    ImageView enemyPlayer = Game.getEnemyPlayer();
                    ImageView currentPlayer = Game.getCurrentPlayer();
                    //Log.wtf("dsdas", String.valueOf(player.getX() + (player.getWidth() / 4f)));
                    Rect enemyPlayerRect = new Rect();
                    Rect currentPlayerRect = new Rect();
                    enemyPlayer.getHitRect(enemyPlayerRect);
                    currentPlayer.getHitRect(currentPlayerRect);

                    for(int i = 0; i < xyPositions.size();i++){
                        BulletImage bulletImage = xyPositions.get(i);
                        if(!bulletImage.getStatus()){
                            xyPositions.remove(i--);
                        }

                        Rect bulletRect = new Rect();
                        bulletImage.getHitRect(bulletRect);
                        Log.wtf("bulletImage.getPlayerType() - Rect.intersects(enemyPlayerRect, bulletRect): ",  bulletImage.getPlayerType() + "-" + Rect.intersects(enemyPlayerRect, bulletRect) + ":" + Rect.intersects(currentPlayerRect, bulletRect));
                        if(bulletImage.getPlayerType() == Player.Type.PLAYER1 && Rect.intersects(currentPlayerRect, bulletRect)){
                            // is enemy
                            xyPositions.remove(i--);
                            Game.PLAYER_HEALTH -= 10;
                        }else if(bulletImage.getPlayerType() == Player.Type.PLAYER2 && Rect.intersects(enemyPlayerRect, bulletRect)){
                            // is player
                            xyPositions.remove(i--);
                            Game.ENEMY_HEALTH -= 10;
                        }
                        /*else if(bulletImage.getPlayerType() != Game.PLAYER_TYPE && Rect.intersects(currentPlayerRect, bulletRect)){
                            xyPositions.remove(i--);
                            if(Game.PLAYER_HEALTH == 10){
                                return;
                            }
                            Game.PLAYER_HEALTH -= 10;
                        }*/
                        GameBoard.updateHealths();
                    }
                });
            }
        },10,10);

    }


}

