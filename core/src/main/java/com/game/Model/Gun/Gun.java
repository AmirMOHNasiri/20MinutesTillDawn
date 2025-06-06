package com.game.Model.Gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import jakarta.persistence.*;

@Embeddable
public class Gun {
    @Enumerated(EnumType.STRING)
    private GunType type;
    private int currentAmmo;
    @Transient
    private Sprite sprite;

    private boolean reloading;
    private float reloadTimeElapsed = 0f;
    private float animationStateTime = 0f;

    public Gun() {}

    public Gun(GunType type) {
        this.type = type;
        this.currentAmmo = type.getAmmo();
        this.sprite = new Sprite(this.type.getTexture());
        this.sprite.setX((float) Gdx.graphics.getWidth() / 2);
        this.sprite.setY((float) Gdx.graphics.getHeight() / 2);
        this.sprite.setSize(50,50);
        this.reloading = false;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void ammoDecrease() {
        currentAmmo--;
    }

    public GunType getType() {
        return type;
    }

    public boolean isReloading() {
        return reloading;
    }

    public void setReloading(boolean reloading) {
        this.reloading = reloading;
    }

    public void addReloadTimeElapsed(float elapsedTime) {
        this.reloadTimeElapsed += elapsedTime;
    }

    public float getReloadTimeElapsed() {
        return reloadTimeElapsed;
    }

    public float getAnimationStateTime() {
        return animationStateTime;
    }

    public void addAnimationStateTime(float elapsedTime) {
        this.animationStateTime += elapsedTime;
    }

    public void fullAmmoBox() {
        this.currentAmmo = type.getAmmo();
    }

    public void setReloadTimeElapsed(float reloadTimeElapsed) {
        this.reloadTimeElapsed = reloadTimeElapsed;
    }

    public void resetAnimationStateTime() {
        this.animationStateTime = 0f;
    }
}
