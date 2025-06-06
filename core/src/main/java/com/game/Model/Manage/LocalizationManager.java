package com.game.Model.Manage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import com.game.Model.Language;

import java.util.Locale;

public class LocalizationManager {
    private I18NBundle bundle;

    public void load(Language language) {
        Locale locale = new Locale(language.getCode());
        bundle = I18NBundle.createBundle(Gdx.files.internal("i18n/i18n"), locale);
    }

    public String get(String key) {
        return bundle.get(key);
    }
}
