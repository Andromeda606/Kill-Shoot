package com.androsoft.ping_pong;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.Device;
import com.androsoft.ping_pong.util.Game;
import com.androsoft.ping_pong.util.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends Fragment {
    private BulletPhysics current, enemy;
    FragmentGameScreenBinding binding;
    public int ENEMY_HEALTH = 100;
    public int CURRENT_HEALTH = 100;
    public Player.Type PLAYER_TYPE = Player.Type.PLAYER2;

    public GameScreen() {
        // Required empty public constructor
    }

    public ImageView getCurrentPlayer() {
        return playerToImage(PLAYER_TYPE);
    }

    public ImageView getEnemyPlayer() {
        return playerToOtherImage(PLAYER_TYPE);
    }

    public ImageView playerToImage(Player.Type playerType) {
        if (Player.Type.PLAYER2 == playerType) {
            return binding.player2;
        } else {
            return binding.player1;
        }
    }

    public ImageView playerToOtherImage(Player.Type playerType) {
        if (Player.Type.PLAYER2 != playerType) {
            return binding.player2;
        } else {
            return binding.player1;
        }
    }

    public void updateHealths() {
        binding.enemyHealth.setText(String.valueOf(ENEMY_HEALTH));
        binding.playerHealth.setText(String.valueOf(CURRENT_HEALTH));
    }

    // Required activity
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameScreenBinding.inflate(inflater);
        Activity gameActivity = requireActivity();

        Network network = new Network("17");
        binding.textView3.setText(Device.getLocalIpAddress());
        StreamController connectedThread;
        //todo içeriye playerimage koyalım ve bu playerimage içerisinden shoot yapılsın.
        current = new BulletPhysics(this, Player.Type.PLAYER2, Character.Type.GUNNER, binding.gameArea);
        enemy = new BulletPhysics(this, Player.Type.PLAYER1, Character.Type.GUNNER, binding.gameArea);
        try {
            connectedThread = network.createConnectedThread();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (connectedThread != null) {
            connectedThread.onMessageEvent(data -> {
                Log.d("Data", data);
                //todo bunu burada ayarlama, connectedthread içerisinde onXY ve onShoot kısmını ekle.
                gameActivity.runOnUiThread(() -> {
                    if (data.equals("SHOOT")) {
                        enemy.shoot();
                        return;
                    }
                    // find x,y
                    String[] xy = data.split(",");
                    if (xy.length != 2) {
                        Log.wtf("LOGGER", "Data is wrong: " + data);
                        return;
                    }
                    float x, y;
                    x = Float.parseFloat(xy[0]);
                    y = Float.parseFloat(xy[1]);
                    ImageView enemy = getEnemyPlayer();
                    enemy.setX(Screen.angleToWidth(x));
                    enemy.setY(Device.getScreenHeight() - Screen.angleToHeight(y));
                });

            });
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, Game.toAngle(Device.getScreenWidth(), 10), 0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);
        BulletPhysics.syncBullets(this);

        StreamController finalConnectedThread = connectedThread;
        binding.joystick.setOnMoveListener((angle, strength) -> {
            ImageView player = getCurrentPlayer();
            int width = player.getWidth();

            float dx = (float) Math.sin(Math.toRadians(angle)) * width;
            float dy = (float) Math.cos(Math.toRadians(angle)) * player.getHeight();

            dx = player.getTranslationX() + dx * strength / 250;
            dy = player.getTranslationY() + dy * strength / 250;

            float currentX = player.getX() + dx;
            float currentY = player.getY() + dy;
            int wall = 10;
            StringBuilder stringBuilder = new StringBuilder();
            if (
                    currentX <= Device.getScreenWidth() - Game.toAngle(Device.getScreenWidth(), 5)
                            && currentX >= Game.toAngle(Device.getScreenWidth(), wall)
            ) {
                player.setTranslationX(dx);
            }
            stringBuilder.append(Screen.widthToAngle(player.getX()));
            if (
                    currentY <= Device.getScreenHeight() - Game.toAngle(Device.getScreenHeight(), wall)
                            && currentY >= Game.toAngle(Device.getScreenHeight(), wall)
            ) {
                player.setTranslationY(dy);
            }
            stringBuilder.append(",").append(Screen.heightToAngle(player.getY()));

            if (finalConnectedThread != null)
                finalConnectedThread.sendMessage(stringBuilder.toString());
        });

        StreamController finalConnectedThread1 = connectedThread;
        binding.shoot.setOnClickListener(v -> {
            if (finalConnectedThread1 != null) current.shoot(finalConnectedThread1);
            binding.shoot.setEnabled(false);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    gameActivity.runOnUiThread(() -> binding.shoot.setEnabled(true));
                }
            }, 500);
        });

        return binding.getRoot();
    }
}