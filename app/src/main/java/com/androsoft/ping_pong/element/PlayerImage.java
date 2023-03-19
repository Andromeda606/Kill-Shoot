package com.androsoft.ping_pong.element;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import com.androsoft.ping_pong.connection.StreamController;
import com.androsoft.ping_pong.constant.Character;
import com.androsoft.ping_pong.physics.BulletPhysics;
import org.jetbrains.annotations.NotNull;

public class PlayerImage extends androidx.appcompat.widget.AppCompatImageView {
    private BulletPhysics bulletPhysics;
    private int health = 100;
    public PlayerImage(@NonNull @NotNull Context context) {
        super(context);
    }

    public PlayerImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void shoot(){
        if(getBulletPhysics() != null) getBulletPhysics().shoot();
    }

    public void shoot(StreamController streamController){
        if(getBulletPhysics() != null) getBulletPhysics().shoot();
        streamController.shoot();
    }

    public BulletPhysics getBulletPhysics() {
        return bulletPhysics;
    }

    private void initCharacter(){
        Character character = getCharacterInfo();
        setImageDrawable(AppCompatResources.getDrawable(getContext(), character.getCharacterImage()));
        health = character.getHealth();
    }

    public void setBulletPhysics(BulletPhysics bulletPhysics) {
        this.bulletPhysics = bulletPhysics;
        initCharacter();
    }

    public void decraseHealth(int count){
        health -= count;
    }

    public int getHealth() {
        return health;
    }

    public Character getCharacterInfo(){
        return Character.convertTypeToCharacter(getBulletPhysics().getCharacterType());
    }
}
