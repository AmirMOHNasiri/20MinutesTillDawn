package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.game.Main;
import com.game.Model.CollisionRect;
import com.game.Model.Enemy.Enemy;
import com.game.Model.Gun.Bullet;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Player;

import java.awt.*;

public class PlayerController {
    private final Player currentPlayer;
    private final Texture texture;
    private final Sprite sprite;
    private float time;
    private final Animation<Texture> idleAnimation;
    private final Animation<Texture> runningAnimation;
    private final float bkgX, bkgY;
    private EnemyController enemyController;

    public PlayerController(Player currentPlayer, Texture texture,
                            Animation<Texture> idleAnimation,
                            Animation<Texture> runingAnimation, float bkgX, float bkgY) {
        this.currentPlayer = currentPlayer;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.bkgX = bkgX;
        this.bkgY = bkgY;
        this.time = 0f;
        this.idleAnimation = idleAnimation;
        this.runningAnimation = runingAnimation;

        sprite.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        sprite.setSize(this.texture.getWidth() * 3, this.texture.getHeight() * 3);
        this.currentPlayer.setRect(new CollisionRect((float) Gdx.graphics.getWidth() / 2,
            (float) Gdx.graphics.getHeight() / 2,
            this.texture.getWidth() * 3, this.texture.getHeight() * 3));
    }

    public void update() {
        currentPlayer.update(Gdx.graphics.getDeltaTime());

        handlePlayerInput();
        handleCollisionWithEnemies();

        if (currentPlayer.isPlayerIdle())
            idle();
        else
            run();

        if (currentPlayer.isInvincible()) {
            if (((int)(currentPlayer.getInvincibleTimer() * 10)) % 2 == 0)
                sprite.draw(Main.getBatch());
        } else
            sprite.draw(Main.getBatch());
    }

    private void handleCollisionWithEnemies() {

        if (currentPlayer.isInvincible()) return;

        CollisionRect playerRect = currentPlayer.getRect();
        for (Enemy enemy : enemyController.getEnemies()) {
            if (enemy.isActive()) {
                if (playerRect.collidesWith(enemy.getEnemyRect())) {
                    currentPlayer.takeDamage(1);
                    break;
                }
            }
        }
    }

    private void idle(){
        sprite.setRegion(idleAnimation.getKeyFrame(time,true));
        time += Gdx.graphics.getDeltaTime();
    }

    private void run(){
        sprite.setRegion(runningAnimation.getKeyFrame(time,true));
        time += Gdx.graphics.getDeltaTime();
    }

    private void handlePlayerInput(){
        currentPlayer.setPlayerIdle(true);

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            currentPlayer.getPosition().y -= currentPlayer.getSpeed();
            currentPlayer.setPlayerIdle(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            currentPlayer.getPosition().x -= currentPlayer.getSpeed();
            currentPlayer.setPlayerIdle(false);
            if (sprite.isFlipX()) sprite.flip(true,false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            currentPlayer.getPosition().y += currentPlayer.getSpeed();
            currentPlayer.setPlayerIdle(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            currentPlayer.getPosition().x += currentPlayer.getSpeed();
            currentPlayer.setPlayerIdle(false);
            if (!sprite.isFlipX()) sprite.flip(true, false);
        }

        Vector2 pos = currentPlayer.getPosition();

        pos.x = Math.min(pos.x, 960);
        pos.x = Math.max(pos.x, -2750);
        pos.y = Math.max(pos.y, -2060);
        pos.y = Math.min(pos.y, 540);

        currentPlayer.setPosition(pos);

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Vector2 getPlayerPosition() {
        Vector2 pos = currentPlayer.getPosition().cpy();
        pos.x *= -1; pos.y *= -1;
        pos.x += Gdx.graphics.getWidth() / 2f; pos.y += Gdx.graphics.getHeight() / 2f;
        return pos;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setEnemyController(EnemyController enemyController) {
        this.enemyController = enemyController;
    }

    public Texture getTexture() {
        return texture;
    }
}
