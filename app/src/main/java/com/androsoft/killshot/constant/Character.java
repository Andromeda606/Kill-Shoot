package com.androsoft.killshot.constant;

import com.androsoft.killshot.R;
import com.androsoft.killshot.connection.StreamInterface;
import com.androsoft.killshot.constant.character.Circler;
import com.androsoft.killshot.constant.character.Gunner;
import com.androsoft.killshot.constant.character.Tringle;
import com.androsoft.killshot.physics.BulletPhysics;

public abstract class Character {
    private final BulletPhysics bulletPhysics;
    private final int health, damage;
    private final int duration; // Duration ateş etme başına geçen süredir.
    private final int characterImage, bulletImage;
    private final int title, description;

    public Character(
            BulletPhysics bulletPhysics,
            int health,
            int damage,
            int duration,
            int characterImage,
            int bulletImage,
            int title,
            int description
    ) {
        this.bulletPhysics = bulletPhysics;
        this.health = health;
        this.damage = damage;
        this.duration = duration;
        this.characterImage = characterImage;
        this.bulletImage = bulletImage;
        this.title = title;
        this.description = description;
    }

    public static Character getCharacter(int type, BulletPhysics bulletPhysics){
        switch (type){
            case 0:
                return new Gunner(
                        bulletPhysics,
                        100,
                        10,
                        500,
                        R.drawable.ic_game_character1,
                        R.drawable.ic_game_bullet1,
                        1000,
                        R.string.game_character_1_title,
                        R.string.game_character_1_description
                );
            case 1:
                return new Tringle(
                        bulletPhysics,
                        80,
                        5,
                        600,
                        R.drawable.ic_game_character2,
                        R.drawable.ic_game_bullet2,
                        1500,
                        1000,
                        R.string.game_character_2_title,
                        R.string.game_character_2_description
                );
            case 2:
                return new Circler(
                        bulletPhysics,
                        150,
                        25,
                        1500,
                        R.drawable.ic_game_character3,
                        R.drawable.ic_game_bullet3,
                        2500,
                        2000,
                        R.string.game_character_3_title,
                        R.string.game_character_3_description
                );
            default:
                return null;
        }
    }

    public static String getCharacterName(int type){
        switch (type){
            case 0:
                return "Gunner";
            case 1:
                return "Tringle";
            case 2:
                return "Circler";
            default:
                return null;
        }
    }

    public BulletPhysics getBulletPhysics() {
        return bulletPhysics;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getDuration() {
        return duration;
    }

    public int getCharacterImage() {
        return characterImage;
    }

    public int getBulletImage() {
        return bulletImage;
    }

    public Player.Type getCharacterType() {
        return getBulletPhysics().getPlayerType();
    }

    public abstract void shoot();

    public void shoot(StreamInterface streamInterface){
        shoot();
        streamInterface.shoot();
    }

    public int getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }
}
