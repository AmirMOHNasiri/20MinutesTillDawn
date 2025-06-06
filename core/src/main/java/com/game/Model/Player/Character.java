package com.game.Model.Player;

public enum Character {
    Dasher(  2,10),
    Diamond( 7,1),
    Lilith(  5,3),
    Scarlett(3,5),
    Shana(   4,4);

    private final int hp;
    private final int speed;

    Character(int hp, int speed) {
        this.hp = hp;
        this.speed = speed;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public static Character getCharacter(String characterName) {
        return Character.valueOf(characterName);
    }
}
