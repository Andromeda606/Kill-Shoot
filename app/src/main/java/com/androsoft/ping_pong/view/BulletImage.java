package com.androsoft.ping_pong.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.androsoft.ping_pong.constant.Player;
import org.jetbrains.annotations.NotNull;

public class BulletImage extends androidx.appcompat.widget.AppCompatImageView {
    private Player.Type playerType;
    private Boolean status = true;

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
