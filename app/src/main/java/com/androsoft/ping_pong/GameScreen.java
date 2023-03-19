package com.androsoft.ping_pong;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.androsoft.ping_pong.connection.OnMessageCaptured;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.element.PlayerImage;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.Device;
import com.androsoft.ping_pong.util.Game;
import com.androsoft.ping_pong.util.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends Fragment {
    FragmentGameScreenBinding binding;
    public int ENEMY_HEALTH = 100;
    public int CURRENT_HEALTH = 100;
    public Player.Type PLAYER_TYPE = Player.Type.PLAYER2;

    public GameScreen() {
        // Required empty public constructor
    }

    public PlayerImage getCurrentPlayer() {
        return playerToImage(PLAYER_TYPE);
    }

    public PlayerImage getEnemyPlayer() {
        return playerToOtherImage(PLAYER_TYPE);
    }

    public PlayerImage playerToImage(Player.Type playerType) {
        if (Player.Type.PLAYER2 == playerType) {
            return binding.player2;
        } else {
            return binding.player1;
        }
    }

    public PlayerImage playerToOtherImage(Player.Type playerType) {
        if (Player.Type.PLAYER2 != playerType) {
            return binding.player2;
        } else {
            return binding.player1;
        }
    }

    public void updateHealths() {
        binding.enemyHealth.setText(String.valueOf(getCurrentPlayer().getHealth()));
        binding.playerHealth.setText(String.valueOf(getEnemyPlayer().getHealth()));
    }

    private void initListeners(StreamController connectedThread) {
        PlayerImage player = getCurrentPlayer();
        binding.joystick.setOnMoveListener((angle, strength) -> {
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
            stringBuilder.append(":").append(Screen.heightToAngle(player.getY()));

            if (connectedThread != null)
                connectedThread.sendMessage(stringBuilder.toString());
        });

        binding.shoot.setOnClickListener(v -> {
            if (connectedThread != null) getCurrentPlayer().shoot(connectedThread);
            binding.shoot.setEnabled(false);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    requireActivity().runOnUiThread(() -> binding.shoot.setEnabled(true));
                }
            }, getCurrentPlayer().getCharacterInfo().getDuration());
        });
    }

    // Required activity
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameScreenBinding.inflate(inflater);
        PlayerImage player = getCurrentPlayer();
        PlayerImage enemyPlayer = getEnemyPlayer();
        binding.textView3.setText(Device.getLocalIpAddress());
        // Syncing all bullets
        BulletPhysics.syncBullets(this, binding.gameArea);
        updateHealths();

        Network network = new Network("17");
        StreamController connectedThread;
        try {
            connectedThread = network.createConnectedThread();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (connectedThread != null) {
            connectedThread.onMessageEvent(new OnMessageCaptured() {
                @Override
                public void shoot() {
                    getEnemyPlayer().shoot();
                }

                @Override
                public void xyStatus(float x, float y) {
                    ImageView enemy = getEnemyPlayer();
                    enemy.setX(Screen.angleToWidth(x));
                    enemy.setY(Device.getScreenHeight() - Screen.angleToHeight(y));
                }
            });
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Screen.pxToDp(requireContext(), 50), Screen.pxToDp(requireContext(), 50));
        layoutParams.setMargins(0, 0, Game.toAngle(Device.getScreenWidth(), 10), 0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);

        player.setBulletPhysics(new BulletPhysics(this, Player.Type.PLAYER2, Character.Type.CIRCLER, binding.gameArea));
        enemyPlayer.setBulletPhysics(new BulletPhysics(this, Player.Type.PLAYER1, Character.Type.TRINGLE, binding.gameArea));

        initListeners(connectedThread);

        return binding.getRoot();
    }
}