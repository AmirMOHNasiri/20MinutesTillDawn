package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.game.Main;
import com.game.Model.CollisionRect;
import com.game.Model.Enemy.Enemy;
import com.game.Model.Enemy.EnemyType;
import com.game.Model.Gun.Bullet;
import com.game.Model.Gun.Gun;
import com.game.Model.Gun.GunType;
import com.game.Model.Manage.GameAssetManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class GunController {
    private Gun gun;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private PlayerController playerController;
    private EnemyController enemyController;
    private Animation<TextureRegion> onGunReloadAnimation;

    public GunController(Gun gun, PlayerController playerController, EnemyController enemyController) {
        this.gun = gun;
        this.playerController = playerController;
        this.enemyController = enemyController;
        this.onGunReloadAnimation = GameAssetManager.getAmmoReloadAnimation();
        Bullet.loadDefaultTexture("bullet.png");
    }

    public void update(){
        if (gun.isReloading()) {
            gun.addReloadTimeElapsed(Gdx.graphics.getDeltaTime());
            gun.addAnimationStateTime(Gdx.graphics.getDeltaTime());

            float timeReloadTime = gun.getType().getTimeReload();

            if (gun.getReloadTimeElapsed() >= timeReloadTime) {
                gun.fullAmmoBox();
                gun.setReloading(false);
                gun.getSprite().setRegion(gun.getType().getTexture());
            } else {
                gun.getSprite().setRegion(onGunReloadAnimation.getKeyFrame(gun.getAnimationStateTime(),true));
            }
        }

        gun.getSprite().draw(Main.getBatch());
        updateBullets(Gdx.graphics.getDeltaTime());
    }

    public void startReload() {
        if (gun.isReloading() || gun.getCurrentAmmo() == gun.getType().getAmmo()) return;

        gun.setReloading(true);
        gun.setReloadTimeElapsed(0f);
        gun.resetAnimationStateTime();

        float frameDuration = (float) gun.getType().getTimeReload() / onGunReloadAnimation.getKeyFrames().length;
        onGunReloadAnimation.setFrameDuration(frameDuration);

    }

    public void handleGunRotation(int x, int y){
        Sprite gunSprite = gun.getSprite();

        float gunCenterX = (float) Gdx.graphics.getWidth() / 2;
        float gunCenterY = (float) Gdx.graphics.getHeight() / 2;

        float angle = (float) Math.atan2(y - gunCenterY , x - gunCenterX);

        gunSprite.setRotation((float) (3.14 - angle * MathUtils.radiansToDegrees));
    }

    public void handleWeaponShoot(int x, int y){
        if (gun.getCurrentAmmo() == 0 || gun.isReloading()) return;

        float[] spreadAngles = {-15f, -5f, 5f, 15f};

        Vector2 bulletStartPosition = playerController.getPlayerPosition().cpy();

        float screenCenterX = (float) Gdx.graphics.getWidth() / 2;
        float screenCenterY = (float) Gdx.graphics.getHeight() / 2;
        float mouseYFromButton = Gdx.graphics.getHeight() - y;

        Vector2 shotDirection = new Vector2(x - screenCenterX, mouseYFromButton - screenCenterY).nor();

        GunType currentGunType = gun.getType();
        int projectilesToFire = currentGunType.getProjectile();
        int bulletDamage = currentGunType.getDamage();

        for (int i=0; i<projectilesToFire; i++) {
            Vector2 currentBulletDirection = shotDirection.cpy();
            if (projectilesToFire > 1) {
                currentBulletDirection.rotateDeg(spreadAngles[i]);
            }

            Bullet newBullet = new Bullet(bulletStartPosition, currentBulletDirection, 700f, bulletDamage, currentGunType.getRange());
            newBullet.getSprite().setColor(Color.CYAN);

            bullets.add(newBullet);
        }

        gun.ammoDecrease();
    }

    public void updateBullets(float deltaTime){
        Iterator<Bullet> iterator = bullets.iterator();
        Vector2 playerPosition = playerController.getPlayerPosition();

        while (iterator.hasNext()){
            Bullet bullet = iterator.next();
            bullet.update(deltaTime);

            if (!bullet.isActive()) {
                iterator.remove();
                continue;
            }

            Sprite bulletSprite = bullet.getSprite();
            if (bulletSprite != null) {
                float drawX = bullet.getPosition().x - playerPosition.x + Gdx.graphics.getWidth()  / 2f - bulletSprite.getWidth() / 2f;
                float drawY = bullet.getPosition().y - playerPosition.y + Gdx.graphics.getHeight() / 2f - bulletSprite.getHeight() / 2f;
                bulletSprite.setPosition(drawX, drawY);
                bulletSprite.setRotation(bullet.getVelocity().angleDeg());
                bullet.getCollisionRect().move(drawX, drawY);
                bulletSprite.draw(Main.getBatch());
            }

            ArrayList<Enemy> currentEnemies = enemyController.getEnemies();
            if (currentEnemies != null) {
                for (Enemy enemy : currentEnemies) {
                    if (bullet.getCollisionRect().collidesWith(enemy.getEnemyRect())) {
                        enemy.triggerHitState(bullet.getDamage());
                        bullet.setActive(false);
                        break;
                    }
                }
            }
        }
    }

    public boolean isGunReloading() {
        return gun.isReloading();
    }

    public float getGunReloadProgressRatio() {
        if (!gun.isReloading()) return 0f;

        return Math.min(1f, gun.getReloadTimeElapsed() / (float) gun.getType().getTimeReload());
    }
}
