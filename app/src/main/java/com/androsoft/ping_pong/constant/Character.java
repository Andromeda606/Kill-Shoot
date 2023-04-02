package com.androsoft.ping_pong.constant;

import com.androsoft.ping_pong.R;
import com.androsoft.ping_pong.connection.StreamInterface;
import com.androsoft.ping_pong.constant.character.Circler;
import com.androsoft.ping_pong.constant.character.Gunner;
import com.androsoft.ping_pong.constant.character.Tringle;
import com.androsoft.ping_pong.physics.BulletPhysics;

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
                        R.string.game_character_1_title,
                        R.string.game_character_1_description
                );
            case 2:
                return new Circler(
                        bulletPhysics,
                        150,
                        25,
                        600,
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
