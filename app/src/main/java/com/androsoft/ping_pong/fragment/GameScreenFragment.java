package com.androsoft.ping_pong.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.androsoft.ping_pong.connection.OnGameProcess;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.connection.network.NetworkConnectedThread;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.view.PlayerImage;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.DeviceUtil;
import com.androsoft.ping_pong.util.GameUtil;
import com.androsoft.ping_pong.util.ScreenUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class GameScreenFragment extends Fragment {
    FragmentGameScreenBinding binding;
    public Player.Type PLAYER_TYPE = Player.Type.PLAYER2;

    public GameScreenFragment() {
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

            dx = player.getTranslationX() + dx * strength / 150;
            dy = player.getTranslationY() + dy * strength / 150;

            float currentX = player.getX() + dx;
            float currentY = player.getY() + dy;
            int wall = 10;
            if (
                    currentX <= DeviceUtil.getScreenWidth() - GameUtil.toAngle(DeviceUtil.getScreenWidth(), 5)
                            && currentX >= GameUtil.toAngle(DeviceUtil.getScreenWidth(), wall)
            ) {
                player.setTranslationX(dx);
            }
            if (
                    currentY <= DeviceUtil.getScreenHeight() - GameUtil.toAngle(DeviceUtil.getScreenHeight(), wall)
                            && currentY >= GameUtil.toAngle(DeviceUtil.getScreenHeight(), wall)
            ) {
                player.setTranslationY(dy);
            }

            if (connectedThread != null)
                connectedThread.sendLocation(ScreenUtil.widthToAngle(player.getX()), ScreenUtil.heightToAngle(player.getY()));
        });

        binding.joystickLayout.setOnTouchListener((v, event) -> {
            //todo not working properly
            binding.joystick.setVisibility(View.VISIBLE);
            binding.joystick.setTranslationX(v.getX());
            binding.joystick.setTranslationY(v.getY());
            return false;
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
        binding.textView3.setText(DeviceUtil.getLocalIpAddress());
        // Syncing all bullets
        BulletPhysics.syncBullets(this, binding.gameArea);
        updateHealths();
        Bundle datas = getArguments();

        Network network = new Network(datas.getString("ipAddress"));
        StreamController connectedThread;
        try {
            connectedThread = network.createConnectedThread();
        } catch (Exception e) {
            new AlertDialog.Builder(this.getContext())
                    .setMessage("Karşı oyuncuya bağlanılamadı!")
                    .setTitle("HATA")
                    .setPositiveButton("Çık", null)
                    .create()
                    .show();
            return binding.getRoot();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtil.pxToDp(requireContext(), 50), ScreenUtil.pxToDp(requireContext(), 50));
        layoutParams.setMargins(0, 0, GameUtil.toAngle(DeviceUtil.getScreenWidth(), 10), 0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);
        int type = (int) datas.get("characterType");
        Character.Type playerType = Character.intToCharacter(type);
        int enemy = (int) datas.get("enemyType");
        Character.Type enemyType = Character.intToCharacter(enemy);
        player.setBulletPhysics(new BulletPhysics(GameScreenFragment.this, Player.Type.PLAYER2, playerType, binding.gameArea));
        enemyPlayer.setBulletPhysics(new BulletPhysics(GameScreenFragment.this, Player.Type.PLAYER1, enemyType, binding.gameArea));

        initListeners(connectedThread);
        if (connectedThread != null) {
            NetworkConnectedThread.setOnGameProcess(new OnGameProcess() {
                @Override
                public void shoot() {
                    getEnemyPlayer().shoot();
                }


                @Override
                public void xyStatus(float x, float y) {
                    ImageView enemy = getEnemyPlayer();
                    enemy.setX(ScreenUtil.angleToWidth(x));
                    enemy.setY(DeviceUtil.getScreenHeight() - ScreenUtil.angleToHeight(y));
                }
            });
        }


        return binding.getRoot();
    }
}