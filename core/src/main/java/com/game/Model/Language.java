package com.game.Model;

public enum Language {
    English("en"),
    French("fr");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
