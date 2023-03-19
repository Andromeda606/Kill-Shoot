package com.androsoft.ping_pong.constant;

import com.androsoft.ping_pong.R;

public class Character {
    private int movementSpeed, seperatorSpeed;
    private int characterImage, bulletImage;
    private int health, damage;
    private int duration;
    public enum Type{
        GUNNER,
        TRINGLE,
        CIRCLER
    }

    public static Character convertTypeToCharacter(Character.Type type){
        Character character = new Character();
        int movementSpeed, seperatorSpeed;
        int bulletImage, characterImage;
        int health, damage;
        int duration;
        switch (type){
            case GUNNER:
                movementSpeed = 1000;
                seperatorSpeed = 500;
                bulletImage = R.drawable.ic_game_bullet1;
                characterImage = R.drawable.ic_game_character1;
                health = 100;
                damage = 10;
                duration = 250;
                break;
            case TRINGLE:
                movementSpeed = 1500;
                seperatorSpeed = movementSpeed;
                bulletImage = R.drawable.ic_game_bullet2;
                characterImage = R.drawable.ic_game_character2;
                health = 80;
                damage = 5;
                duration = 900;
                break;
            case CIRCLER:
                movementSpeed = 3000;
                seperatorSpeed = 5000;
                bulletImage = R.drawable.ic_game_bullet3;
                characterImage = R.drawable.ic_game_character3;
                health = 150;
                damage = 20;
                duration = 700;
                break;
            default:
                movementSpeed = 2000;
                seperatorSpeed = 1500;
                bulletImage = R.drawable.ic_game_bullet1;
                characterImage = R.drawable.ic_game_character1;
                health = 100;
                damage = 5;
                duration = 9999;
                break;
        }
        character.setMovementSpeed(movementSpeed);
        character.setSeperatorSpeed(seperatorSpeed);
        character.setBulletImage(bulletImage);
        character.setCharacterImage(characterImage);
        character.setHealth(health);
        character.setDamage(damage);
        character.setDuration(duration);
        return character;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public int getSeperatorSpeed() {
        return seperatorSpeed;
    }

    public void setSeperatorSpeed(int seperatorSpeed) {
        this.seperatorSpeed = seperatorSpeed;
    }

    public int getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(int characterImage) {
        this.characterImage = characterImage;
    }

    public int getBulletImage() {
        return bulletImage;
    }

    public void setBulletImage(int bulletImage) {
        this.bulletImage = bulletImage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
