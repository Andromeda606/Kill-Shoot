package com.androsoft.ping_pong.element;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.androsoft.ping_pong.constant.Player;
import org.jetbrains.annotations.NotNull;

public class BulletImage extends androidx.appcompat.widget.AppCompatImageView {
    private Player.Type playerType;

    public BulletImage(@NonNull @NotNull Context context) {
        super(context);
    }

    public BulletImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BulletImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Player.Type getPlayerType() {
        return playerType;
    }

    public void setPlayerType(Player.Type playerType) {
        this.playerType = playerType;
    }
}
