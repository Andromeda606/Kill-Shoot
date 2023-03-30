package com.androsoft.ping_pong.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.androsoft.ping_pong.connection.BattleInterface;
import com.androsoft.ping_pong.connection.StreamInterface;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.connection.network.NetworkConnectedThread;
import com.androsoft.ping_pong.constant.BundleTags;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.dialog.CustomDialog;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.DeviceUtil;
import com.androsoft.ping_pong.util.GameUtil;
import com.androsoft.ping_pong.util.ScreenUtil;
import com.androsoft.ping_pong.view.PlayerImage;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Oyun içinde yapılacaklar
 * Ekrana girildiğinde hemen "Oyuncu ekrana geldi" diye karşı ip ye istek atacağız karşı tarafta geldiğinde oyun başlayacak <br>
 * Oyuncu ekrana gelene kadar "Karşı Takım Bekleniliyor" uyarısı alacak ve geldiğinde ise diyalog kapatılacak.<br>
 * kullanıcı ateş ettiğinde karşı tarafa istek gidecek. Zaten buralar çoktan kodlandı.
 */
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

    private void initListeners(StreamInterface connectedThread) {
        PlayerImage player = getCurrentPlayer();
        binding.joystick.setOnMoveListener((angle, strength) -> {
            int width = player.getWidth();

            float dx = (float) Math.sin(Math.toRadians(angle)) * width;
            float dy = (float) Math.cos(Math.toRadians(angle)) * player.getHeight();

            dx = player.getTranslationX() + dx * strength / 300;
            dy = player.getTranslationY() + dy * strength / 300;

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

        binding.joystickLayout.setOnDragListener((v, event) -> {
            //todo not working properly
            binding.joystick.setVisibility(View.VISIBLE);
            binding.joystick.setTranslationX(v.getX());
            binding.joystick.setTranslationY(v.getY());
            return true;
        });
        binding.joystickLayout.setOnTouchListener((v, event) -> {
            binding.joystick.setVisibility(View.VISIBLE);
            binding.joystick.setTranslationX(v.getX());
            binding.joystick.setTranslationY(v.getY());
            return true;
        });

        binding.shoot.setOnClickListener(v -> {
            if (connectedThread != null) getCurrentPlayer().shoot(connectedThread);
            binding.shoot.setEnabled(false);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    requireActivity().runOnUiThread(() -> binding.shoot.setEnabled(true));
                }
            }, getCurrentPlayer().getCharacter().getDuration());
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
        Bundle arguments = getArguments();
        if(arguments == null){
            throw new RuntimeException("argument is null");
        }
        binding.pairingActivity.setVisibility(View.VISIBLE);
        binding.gameArea.setVisibility(View.GONE);

        Network network = new Network(arguments.getString(BundleTags.IP_ADDRESS));
        StreamInterface connectedThread;
        try {
            connectedThread = network.createConnectedThread();
        } catch (Exception e) {
            new CustomDialog(requireContext())
                    .setMessage("Karşı oyuncuya bağlanılamadı!")
                    .setTitle("HATA")
                    .setPositiveButton("Çık", null)
                    .show();
            return binding.getRoot();
        }
        Timer pairer = new Timer();
        pairer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectedThread.sendPaired();
            }
        }, 250, 250);

        NetworkConnectedThread.setOnGameProcess(new BattleInterface.OnGameProcess() {
            @Override
            public void shoot() {
                getEnemyPlayer().shoot();
            }


            @Override
            public void xyStatus(float x, float y) {
                requireActivity().runOnUiThread(() -> {
                    ImageView enemy = getEnemyPlayer();
                    enemy.setX(ScreenUtil.angleToWidth(x));
                    enemy.setY(DeviceUtil.getScreenHeight() - ScreenUtil.angleToHeight(y));
                });
            }

            @Override
            public void paired() {
                requireActivity().runOnUiThread(() -> {
                    binding.pairingActivity.setVisibility(View.GONE);
                    binding.gameArea.setVisibility(View.VISIBLE);
                    connectedThread.sendPairedSuccess();
                });
            }

            @Override
            public void pairedSuccessfull() {
                pairer.cancel();
                pairer.purge();
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtil.pxToDp(requireContext(), 50), ScreenUtil.pxToDp(requireContext(), 50));
        layoutParams.setMargins(0, 0, GameUtil.toAngle(DeviceUtil.getScreenWidth(), 10), 0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);
        int type = Integer.parseInt(arguments.get(BundleTags.CHARACTER_TYPE).toString());
        Character playerType = Character.getCharacter(type, new BulletPhysics(GameScreenFragment.this, Player.Type.PLAYER2, binding.gameArea));
        int enemy = Integer.parseInt(arguments.get(BundleTags.ENEMY_TYPE).toString());
        Character enemyType = Character.getCharacter(enemy, new BulletPhysics(GameScreenFragment.this, Player.Type.PLAYER1, binding.gameArea));
        player.setCharacter(playerType);
        enemyPlayer.setCharacter(enemyType);
        initListeners(connectedThread);


        return binding.getRoot();
    }
}