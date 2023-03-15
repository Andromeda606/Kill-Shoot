package com.androsoft.ping_pong;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androsoft.ping_pong.connection.OnMessageCaptured;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.connection.bluetooth.Bluetooth;
import com.androsoft.ping_pong.connection.bluetooth.ConnectedThread;
import com.androsoft.ping_pong.connection.network.Network;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.Game;
import com.androsoft.ping_pong.util.Screen;
import io.github.controlwear.virtual.joystick.android.JoystickView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static GameScreen newInstance(String param1, String param2) {
        GameScreen fragment = new GameScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentGameScreenBinding binding = FragmentGameScreenBinding.inflate(inflater);
        Game.DEVICE_NAME = Settings.Global.getString(getActivity().getContentResolver(), Settings.Global.DEVICE_NAME);
        Network bluetooth = new Network("17");
        Context context = requireContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        binding.textView3.setText(ip);
        StreamController connectedThread = null;
        BulletPhysics bulletPhysics = new BulletPhysics(getActivity(), Player.Type.PLAYER2, Character.Type.GUNNER, binding.gameArea);
        BulletPhysics bulletPhysicsEnemy = new BulletPhysics(getActivity(), Player.Type.PLAYER1, Character.Type.GUNNER, binding.gameArea);
        try {
            connectedThread = bluetooth.createConnectedThread();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (connectedThread != null) {
            //connectedThread.sendMessage(Settings.Global.getString(getActivity().getContentResolver(), Settings.Global.DEVICE_NAME));
            connectedThread.onMessageEvent(data -> {
                Log.wtf("Data", data);
                getActivity().runOnUiThread(() -> {
                    if(data.equals("SHOOT")){
                        bulletPhysicsEnemy.shoot();
                        return;
                    }
                    float x, y;
                    String[] xy = data.split(",");
                    if(xy.length == 2){
                        x = Float.parseFloat(xy[0]);
                        y = Float.parseFloat(xy[1]);
                        Log.wtf("OHHHHHAAA", x + ":" + y);
                        ImageView enemy = Game.getEnemyPlayer();
                        Log.wtf("Screen.angleToWidth(x)", String.valueOf(Screen.angleToWidth(x)));
                        enemy.setX(Game.SCREEN_WIDTH - Screen.angleToWidth(x));
                        enemy.setY(Screen.angleToHeight(y));
                    }
                });

            });
        }
        JoystickView joystick = binding.joystick;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, Game.toAngle(Game.SCREEN_WIDTH, 10), 0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);
        Game.CURRENT_VIEW = binding.gameArea;
        Game.PLAYER_TYPE = Player.Type.PLAYER2;
        bulletPhysics.syncBullets();

        StreamController finalConnectedThread = connectedThread;
        joystick.setOnMoveListener((angle, strength) -> {
            ImageView player = Game.getCurrentPlayer();
            int width = player.getWidth();

            float dx = (float) Math.sin(Math.toRadians(angle)) * width;
            float dy = (float) Math.cos(Math.toRadians(angle)) * player.getHeight();

            dx = player.getTranslationX() + dx * strength / 500;
            dy = player.getTranslationY() + dy * strength / 500;

            float currentX = player.getX() + dx;
            float currentY = player.getY() + dy;
            int wall = 10;
            int widthLimit = Game.SCREEN_WIDTH;
            int heightLimit = Game.SCREEN_HEIGHT;
            //Log.wtf("currentX", currentX + ": currentY" + currentY + " - " + Game.toAngle(heightLimit, wall) + ":" + Game.toAngle(widthLimit, wall));
            StringBuilder stringBuilder = new StringBuilder();
            if (
                    currentX <= widthLimit - Game.toAngle(widthLimit, wall)
                            && currentX >= Game.toAngle(widthLimit, 25)
            ) {
                player.setTranslationX(dx);
            }
            stringBuilder.append(Screen.widthToAngle(player.getX()));
            Log.wtf("Game.toAngle(heightLimit, 1)", currentY + "::" + Game.toAngle(heightLimit, wall));
            if (
                    currentY <= heightLimit - Game.toAngle(heightLimit, 1)
                            && currentY >= Game.toAngle(heightLimit, 1) //todo bunu ayarla, tabletlerde ciddi sorun yaratabilir
            ) {
                player.setTranslationY(dy);
            }
            stringBuilder.append(",").append(Screen.heightToAngle(player.getY()));

            if(finalConnectedThread != null)
                finalConnectedThread.sendMessage(stringBuilder.toString());
        });

        StreamController finalConnectedThread1 = connectedThread;
        binding.shoot.setOnClickListener(v -> {
            if(finalConnectedThread1 != null) bulletPhysics.shoot(finalConnectedThread1);
            binding.shoot.setEnabled(false);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Activity activity = getActivity();
                    if(activity != null){
                        activity.runOnUiThread(() -> binding.shoot.setEnabled(true));
                    }
                }
            }, 1500);
        });
        //View view = inflater.inflate(R.layout.fragment_game_screen, container, false);

        return binding.getRoot();
    }
}