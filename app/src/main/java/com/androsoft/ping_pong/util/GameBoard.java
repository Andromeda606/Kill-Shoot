package com.androsoft.ping_pong.util;

import android.widget.ImageView;
import android.widget.TextView;
import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.constant.Player;

import static com.androsoft.ping_pong.util.Game.CURRENT_VIEW;

public class GameBoard {

    public static TextView getCurrentHealth(){
        if (Player.Type.PLAYER2 == Game.PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.player2);
        } else {
            return CURRENT_VIEW.findViewById(R.id.player1);
        }
    }

    public static TextView getEnemyHealth(){
        if (Player.Type.PLAYER2 != Game.PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.enemyHealth);
        } else {
            return CURRENT_VIEW.findViewById(R.id.playerHealth);
        }
    }

    public static void updateHealths(){
        getEnemyHealth().setText(String.valueOf(Game.ENEMY_HEALTH));
        getCurrentPlayer().setText(String.valueOf(Game.PLAYER_HEALTH));
    }

    public static TextView getCurrentPlayer() {
        if (Player.Type.PLAYER2 == Game.PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.enemyHealth);
        } else {
            return CURRENT_VIEW.findViewById(R.id.playerHealth);
        }
    }

    public static ImageView getEnemyPlayer() {
        if (Player.Type.PLAYER2 != Game.PLAYER_TYPE) {
            return CURRENT_VIEW.findViewById(R.id.player2);
        } else {
            return CURRENT_VIEW.findViewById(R.id.player1);
        }
    }

}
