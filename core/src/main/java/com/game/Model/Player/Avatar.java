package com.game.Model.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.game.Model.Manage.GameAssetManager;

public enum Avatar {
    Dasher( "Avatar/T_Dasher_Portrait.png", false),
    Diamond("Avatar/T_Diamond_Portrait.png",false),
    Lilith( "Avatar/T_Lilith_Portrait.png",false),
    Scarlett("Avatar/T_Scarlett_Portrait.png",false),
    Shana(   "Avatar/T_Shana_Portrait.png",false),
    Pc("custom_avatar.png",true);

    private final String filePath;
    private final boolean isCustom;

    Avatar(String filePath, boolean isCustom) {
        this.filePath = filePath;
        this.isCustom = isCustom;
    }

    public Texture getPortrait() {
        try {
            if (isCustom) {
                FileHandle file = Gdx.files.local(this.filePath);
                if (file.exists()) {
                    return new Texture(file);
                } else {
                    return new Texture("Avatar/T_Dasher_Portrait.png");
                }
            } else {
                return new Texture(this.filePath);
            }
        } catch (Exception e) {
            Gdx.app.error("Avatar", "Could not load texture for: " + this.name(), e);
            return new Texture("Avatar/T_Dasher_Portrait.png");
        }
    }

    public static Avatar getRandomAvatar() {
        return Avatar.values()[(int)(Math.random()*Avatar.values().length)];
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isCustom() {
        return isCustom;
    }
}
