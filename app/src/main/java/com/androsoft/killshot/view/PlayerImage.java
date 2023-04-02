package com.androsoft.killshot.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import com.androsoft.killshot.connection.StreamInterface;
import com.androsoft.killshot.constant.Character;
import org.jetbrains.annotations.NotNull;

public class PlayerImage extends androidx.appcompat.widget.AppCompatImageView {
    private Character character;
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
        if(getCharacter() != null) getCharacter().shoot();
    }

    public void shoot(StreamInterface streamInterface){
        shoot();
        streamInterface.shoot();
    }


    private void initCharacter(){
        Character character = getCharacter();
        setImageDrawable(AppCompatResources.getDrawable(getContext(), character.getCharacterImage()));
        health = character.getHealth();
    }

    public void decraseHealth(int count){
        health -= count;
    }

    public int getHealth() {
        return health;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        initCharacter();
    }
}
