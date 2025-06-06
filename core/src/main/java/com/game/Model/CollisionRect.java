package com.game.Model;

import jakarta.persistence.Embeddable;

@Embeddable
public class CollisionRect {
    public float x, y;
    public float width, height;

    public CollisionRect() {}

    public CollisionRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collidesWith(CollisionRect other) {
        return x < other.x + other.width && y < other.y + other.height && x + width > other.x && y + height > other.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
