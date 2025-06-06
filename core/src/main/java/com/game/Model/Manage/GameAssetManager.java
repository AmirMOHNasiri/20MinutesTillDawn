package com.game.Model.Manage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.Model.Player.Character;

public class GameAssetManager {
    private static GameAssetManager gameAssetManager;

    private Skin skin = new Skin(Gdx.files.internal("Skin/pixthulhu-ui.json"));

    private GameAssetManager() {}

    public static GameAssetManager getGameAssetManager() {
        if(gameAssetManager == null)
            gameAssetManager = new GameAssetManager();
        return gameAssetManager;
    }

    public static Animation<Texture> getCharacterIdleAnimation(String name) {
        Texture[] textures = new Texture[6];
        for (int i = 0; i < 6; i++) {
            textures[i] = new Texture(Gdx.files.internal(name + "/" + name + "_" + i + ".png"));
        }
        return new Animation<>(0.15f, textures);
    }

    public static Animation<Texture> getCharacterRunAnimation(String name) {
        Texture[] textures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            textures[i] = new Texture(Gdx.files.internal(name + "/" + "Run_" + i + ".png"));
        }
        Character character = Character.getCharacter(name);
        return new Animation<>(0.1f + (0.2f / character.getSpeed()), textures);
    }

    public static Texture getCharacterTexture(String name) {
        return new Texture(Gdx.files.internal(name + "/" + name + "_" + 0 + ".png"));
    }

    public static Animation<Texture> getTreeIdleAnimation() {
        Texture[] textures = new Texture[3];
        for (int i = 0; i < 3; i++) {
            textures[i] = new Texture(Gdx.files.internal("Tree/T_TreeMonster_" + i + ".png"));
        }
        return new Animation<>(0.25f, textures);
    }

    public static Animation<Texture> getTentacleIdleAnimation() {
        Texture[] textures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            textures[i] = new Texture(Gdx.files.internal("TentacleMonster/TentacleIdle" + i + ".png"));
        }
        return new Animation<>(0.2f, textures);
    }

    public static Animation<Texture> getEyeBatIdleAnimation() {
        Texture[] textures = new Texture[4];
        for (int i = 0; i < 4; i++) {
            textures[i] = new Texture(Gdx.files.internal("EyeBat/EyeBat_" + i + ".png"));
        }
        return new Animation<>(0.2f, textures);
    }

    public static Animation<Texture> getTentacleSpawnAnimation() {
        Texture[] textures = new Texture[3];
        for (int i = 0; i < 3; i++) {
            textures[i] = new Texture(Gdx.files.internal("TentacleMonster/TentacleSpawn" + i + ".png"));
        }
        return new Animation<>(1f, textures);
    }

    public static Animation<Texture> getTreeWalkAnimation() {
        Texture[] textures = new Texture[1];
        textures[0] = new Texture(Gdx.files.internal("Tree/T_TreeMonsterWalking.png"));
        return new Animation<>(0.3f, textures);
    }

    public static Animation<Texture> getHeartAnimation() {
        Texture[] textures = new Texture[3];
        for (int i = 0; i < 3; i++)
            textures[i] = new Texture(Gdx.files.internal("Sprite/HeartAnimation_" + i +".png"));
        return new Animation<>(0.2f, textures);
    }

    public static Animation<Texture> getDeadHeartAnimation() {
        Texture texture = new Texture(Gdx.files.internal("Sprite/HeartAnimation_3.png"));
        return new Animation<>(0.1f, texture);
    }

    public static Animation<TextureRegion> getAmmoReloadAnimation() {
        TextureRegion[] textures = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            textures[i] = new TextureRegion( new Texture(Gdx.files.internal("Sprite/T_Gun_Reload_" + i +".png")));
        }
        return new Animation<>(0.15f, textures);
    }

    public static Animation<TextureRegion> getDeathAnimation1() {
        TextureRegion[] textures = new TextureRegion[6];
        for (int i = 0; i < 6; i++) {
            textures[i] = new TextureRegion(new Texture(Gdx.files.internal("Sprite/T_FireExplosionSmall_" + i + ".png")));
        }
        return new Animation<>(0.1f, textures);
    }

    public static Animation<TextureRegion> getDeathAnimation2() {
        TextureRegion[] textures = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            textures[i] = new TextureRegion(new Texture(Gdx.files.internal("Sprite/DeathFX_" + i + ".png")));
        }
        return new Animation<>(0.08f, textures);
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
