package com.game.Model.Gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.game.Model.CollisionRect;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private boolean isActive;
    private Sprite sprite;
    private CollisionRect collisionRect;
    private static Texture defaultTexture;
    private final Vector2 startPosition;
    private final float maxRange;

    public static void loadDefaultTexture(String path){
        if (defaultTexture == null){
            try {
                defaultTexture = new Texture(path);
            } catch (Exception e){
                Gdx.app.error("Bullet", "Failed to load default texture: " + path, e);
            }
        }
    }

    public static void disposeDefaultTexture() {
        if (defaultTexture != null) {
            defaultTexture.dispose();
            defaultTexture = null;
        }
    }

    public static Texture getDefaultTexture() {
        return defaultTexture;
    }

    public Bullet(Vector2 startPosition, Vector2 direction, float speed,
                  int damage, float maxRange) {
        this.position = new Vector2(startPosition);
        this.velocity = new Vector2(direction).nor().scl(speed);
        this.damage = damage;
        this.isActive = true;

        this.sprite = new Sprite(defaultTexture);
        float bulletWidth = 18f;
        float bulletHeight = 18f;
        this.sprite.setSize(bulletWidth, bulletHeight);
        this.sprite.setOriginCenter();
        this.collisionRect = new CollisionRect(
            startPosition.x - bulletWidth  / 2f,
            startPosition.y - bulletHeight / 2f,
            bulletWidth,
            bulletHeight
            );
        this.startPosition = new Vector2(startPosition);
        this.maxRange = maxRange;
    }

    public void update(float delta) {
        if (!isActive) return;

        position.mulAdd(velocity, delta);

        if (position.dst(startPosition) > maxRange)
            setActive(false);

        if (sprite != null) {
            sprite.setPosition(position.x - sprite.getWidth() / 2f, position.y - sprite.getHeight() / 2f);
        }
        if (collisionRect != null) {
            collisionRect.move(position.x - collisionRect.getWidth() / 2f, position.y - collisionRect.getHeight() / 2f);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getVelocity() {
        return velocity;
    }
    public int getDamage() {
        return damage;
    }
    public boolean isActive() {
        return isActive;
    }
    public Sprite getSprite() {
        return sprite;
    }
    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
