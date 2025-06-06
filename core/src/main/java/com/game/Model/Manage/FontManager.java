package com.game.Model.Manage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontManager {
    private static BitmapFont font;

    static {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Skin/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontParameter();
        param.size = 24;
        font = generator.generateFont(param);
        generator.dispose();
    }

    public static BitmapFont getFont() {
        return font;
    }
}
