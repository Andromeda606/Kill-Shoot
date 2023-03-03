package com.androsoft.ping_pong;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androsoft.ping_pong.constant.Joystick;
import com.androsoft.ping_pong.databinding.FragmentGameScreenBinding;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.constant.Player;
import com.androsoft.ping_pong.physics.BulletPhysics;
import com.androsoft.ping_pong.util.Game;
import io.github.controlwear.virtual.joystick.android.JoystickView;

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
        // Inflate the layout for this fragment
        FragmentGameScreenBinding binding = FragmentGameScreenBinding.inflate(inflater);
        BulletPhysics bulletPhysics = new BulletPhysics(getActivity(), Player.Type.PLAYER2, Character.Type.GUNNER, binding.gameArea);
        JoystickView joystick = binding.joystick;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0, Game.toAngle(Game.SCREEN_WIDTH, 10),0);
        layoutParams.gravity = Gravity.CENTER | Gravity.END;
        binding.player2.setLayoutParams(layoutParams);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                int width = binding.player2.getWidth(); // ImageView'in genişliği

                float dx = (float) Math.sin(Math.toRadians(angle)) * width; // 270 derecelik açının etkisiyle x koordinatındaki hareketi hesapla
                float dy = (float) Math.cos(Math.toRadians(angle)) * binding.player2.getHeight(); // 270 derecelik açının etkisiyle y koordinatındaki hareketi hesapla

                dx = binding.player2.getTranslationX() + dx * strength / 500;
                dy = binding.player2.getTranslationY() + dy * strength / 500;

                float currentX = binding.player2.getX() + dx;
                float currentY = binding.player2.getY() + dy;
                int wall = 10;
                int widthLimit = Game.SCREEN_WIDTH;
                int heightLimit = Game.SCREEN_HEIGHT;
                Log.wtf("currentX", currentX + ": currentY" + currentY + " - " + Game.toAngle(heightLimit, wall) + ":" + Game.toAngle(widthLimit, wall));
                if (
                        currentX <= widthLimit - Game.toAngle(widthLimit, wall)
                                && currentX >= Game.toAngle(widthLimit, wall)
                ) {
                    binding.player2.setTranslationX(dx);
                }

                if (
                        currentY <= heightLimit - Game.toAngle(heightLimit, wall)
                                 && currentY >= Game.toAngle(heightLimit, wall) * -5 //todo bunu ayarla, tabletlerde ciddi sorun yaratabilir
                ) {
                    binding.player2.setTranslationY(dy);

                }

            }
        });

        binding.shoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bulletPhysics.shoot();
            }
        });
        //View view = inflater.inflate(R.layout.fragment_game_screen, container, false);

        return binding.getRoot();
    }
}