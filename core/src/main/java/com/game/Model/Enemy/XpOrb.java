package com.game.Model.Enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.game.Model.CollisionRect;
import com.badlogic.gdx.math.Vector2;
import com.game.Model.Gun.Bullet;

public class XpOrb {
    private Vector2 position;
    private Sprite sprite;
    private CollisionRect rect;
    private boolean isActive = true;
    public static final int XP_VALUE = 3;

    public XpOrb(Vector2 startPosition) {
        this.position = startPosition;
        this.sprite = new Sprite(Bullet.getDefaultTexture());
        this.sprite.setColor(Color.WHITE);
        this.sprite.setSize(10, 10);
        this.rect = new CollisionRect(startPosition.x, startPosition.y, 10, 10);
    }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public Vector2 getPosition() { return position; }
    public CollisionRect getRect() { return rect; }
    public Sprite getSprite() { return sprite; }
}
