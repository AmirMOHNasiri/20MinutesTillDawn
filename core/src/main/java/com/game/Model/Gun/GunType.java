package com.game.Model.Gun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public enum GunType {
    Revolver(new Texture("Gun/Revolver.png"),20,1,1,6,1200f),
    Shotgun( new Texture("Gun/Shotgun.png"), 10,4,1,2,500f),
    SMGsDual(new Texture("Gun/SMGsDual.png"),8, 1,2,24,800f);

    private final int damage;
    private final int projectile;
    private final int timeReload;
    private final int ammo;
    private final Texture texture;
    private final float range;

    GunType(Texture texture, int damage, int projectile, int timeReload, int ammo, float range) {
        this.texture = texture;
        this.damage = damage;
        this.projectile = projectile;
        this.timeReload = timeReload;
        this.ammo = ammo;
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public int getProjectile() {
        return projectile;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public int getAmmo() {
        return ammo;
    }

    public static GunType getGunType(String type) {
        return GunType.valueOf(type);
    }

    public Texture getTexture() {
        return texture;
    }

    public float getRange() {
        return range;
    }
}
