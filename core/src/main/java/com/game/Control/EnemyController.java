package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.game.Model.Manage.GameAssetManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public class EnemyController {
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private PlayerController playerController;
    private float timeSinceLastTentacleSpawn = 0f;
    private float timeSinceLastEyeBaSpawn = 0f;
    private float elapsedTime = 0f;
    private final float totalGameTime;
    private boolean eyebatSpawned = false;
    private boolean elderSpawned = false;
    private boolean elderFinishedSpawned = false;

    public EnemyController(PlayerController playerController, float gameTime) {
        this.playerController = playerController;
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();
        Bullet.loadDefaultTexture("bullet.png");
        this.totalGameTime = gameTime * 60;
        spawnTrees(gameTime);
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        elapsedTime += deltaTime;

        timeSinceLastTentacleSpawn += deltaTime;
        if (timeSinceLastTentacleSpawn >= 3f) {
            timeSinceLastTentacleSpawn -= 3f;
            int numberOfTentacles = (int) elapsedTime / 30;
            spawnTentacles(numberOfTentacles);
        }

        timeSinceLastEyeBaSpawn += deltaTime;
        if (eyebatSpawned && timeSinceLastEyeBaSpawn >= 10f) {
            timeSinceLastEyeBaSpawn -= 10f;
            int numberOfEyeBats = (int) (4*elapsedTime) - (int) totalGameTime + 30;
            numberOfEyeBats /= 30;
            spawnEyeBats(numberOfEyeBats);
        }

        if (elderSpawned && !elderFinishedSpawned) {
            elderFinishedSpawned = true;
            spawnElder();
        }

        updateEnemies(deltaTime);
        updateEnemyBullets(deltaTime);
    }

    private void spawnElder() {
        Enemy elder = new Enemy(EnemyType.Elder);
        Vector2 playerPosition = playerController.getPlayerPosition();
        float spawnX = playerPosition.x + Gdx.graphics.getWidth();
        float spawnY = playerPosition.y;

        elder.setEnemyPosition(new Vector2(spawnX, spawnY));
        elder.setSprite(new Sprite(elder.getType().getTexture()));
        elder.setEnemyRect(new CollisionRect(
            spawnX,
            spawnY,
            elder.getType().getTexture().getWidth(),
            elder.getType().getTexture().getHeight()
        ));
        elder.setMovementState(Enemy.EnemyMovementState.Walking);

        enemies.add(elder);
    }

    private void updateEnemies(float deltaTime) {
        Vector2 playerPosition = playerController.getPlayerPosition();

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            if (!enemy.isActive()) {
                enemyIterator.remove();
                continue;
            }

            if (enemy.getType() == EnemyType.EyeBat) handleEyeBatBehavior(enemy, deltaTime);
            if (enemy.getType() == EnemyType.Elder) handleElderBehavior(enemy, deltaTime);

            if (enemy.getMovementState() == Enemy.EnemyMovementState.Walking) {

                float speed = enemy.getType() == EnemyType.Elder ? 10f : 30f;

                Vector2 enemyPosition = enemy.getEnemyPosition();

                Vector2 directionToPlayer = playerPosition.cpy().sub(enemyPosition);

                if (directionToPlayer.len2() > 0.0001f) {
                    directionToPlayer.nor();
                    Vector2 movementThisFrame = directionToPlayer.scl(speed * deltaTime);
                    enemy.setEnemyPosition(enemyPosition.cpy().add(movementThisFrame));
                }
            }

            if (enemy.getMovementState() == Enemy.EnemyMovementState.Dashing) {
                float dashAlpha = enemy.getActionTimer() / 0.2f;
                Vector2 newPos = enemy.getStartPosition().cpy().lerp(enemy.getDashTargetPosition(), dashAlpha);
                enemy.setEnemyPosition(newPos);
            }

            float distanceToPlayer = playerPosition.dst(enemy.getEnemyPosition());

            float drawX = enemy.getEnemyPosition().x + playerController.getCurrentPlayer().getPosition().x;
            float drawY = enemy.getEnemyPosition().y + playerController.getCurrentPlayer().getPosition().y;

            enemy.getSprite().setPosition(drawX, drawY);
            enemy.getEnemyRect().move(drawX, drawY);

            if (distanceToPlayer <= 500f || enemy.getMovementState() == Enemy.EnemyMovementState.Dying) {
                handleEnemyAnimation(enemy);
                enemy.getSprite().draw(Main.getBatch());
            } else {
                Texture emTexture = enemy.getType().getEmTexture();
                if (emTexture != null) {
                    Main.getBatch().draw(emTexture, drawX, drawY, emTexture.getWidth() * 1.5f, emTexture.getHeight() * 1.5f);
                }
            }
        }
    }

    private void handleElderBehavior(Enemy elder, float deltaTime) {
        elder.addToActionTimer(deltaTime);
        float dashCooldown = 4f;
        float prepareDuration = 0.5f;
        float dashDuration = 0.2f;

        switch (elder.getMovementState()) {
            case Walking:
                if (elder.getActionTimer() >= dashCooldown) {
                    elder.setMovementState(Enemy.EnemyMovementState.PreparingDash);
                    elder.resetActionTimer();
                }
                break;
            case PreparingDash:
                if (elder.getActionTimer() >= prepareDuration) {
                    Vector2 playerPosition = playerController.getPlayerPosition();
                    Vector2 startPosition = elder.getEnemyPosition();
                    Vector2 direction = playerPosition.cpy().sub(startPosition).nor();
                    float dashDistance = 120f;

                    elder.setStartPosition(startPosition.cpy());
                    elder.setDashTargetPosition(startPosition.cpy().mulAdd(direction, dashDistance));

                    elder.setMovementState(Enemy.EnemyMovementState.Dashing);
                    elder.resetActionTimer();
                }
                break;
            case Dashing:
                if (elder.getActionTimer() >= dashDuration) {
                    elder.setMovementState(Enemy.EnemyMovementState.Walking);
                    elder.resetActionTimer();
                }
                break;
        }
    }

    private void handleEyeBatBehavior(Enemy eyeBat, float deltaTime) {
        eyeBat.addToActionTimer(deltaTime);
        float shootCooldown = 3f;
        float aimDuration = 1f;

        switch (eyeBat.getMovementState()) {
            case Walking:
                if (eyeBat.getActionTimer() >= shootCooldown) {
                    eyeBat.setMovementState(Enemy.EnemyMovementState.Aiming);
                    eyeBat.resetActionTimer();
                }
                break;
            case Aiming:
                if (eyeBat.getActionTimer() >= aimDuration) {
                    eyeBat.setMovementState(Enemy.EnemyMovementState.Shooting);
                    eyeBat.resetActionTimer();
                }
                break;
            case Shooting:
                fireEyeBatBullet(eyeBat);
                eyeBat.setMovementState(Enemy.EnemyMovementState.Walking);
                break;
        }
    }

    private void fireEyeBatBullet(Enemy eyeBat) {
        Vector2 startPos = eyeBat.getEnemyPosition().cpy();
        Vector2 playerPosition = playerController.getPlayerPosition();
        Vector2 direction = playerPosition.cpy().sub(startPos).nor();

        float speed = 250f;
        int damage = 1;
        bullets.add(new Bullet(startPos, direction, speed, damage, 750f));
    }

    private void updateEnemyBullets(float deltaTime) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update(deltaTime);

            if (!bullet.isActive()) {
                bulletIterator.remove();
                continue;
            }

            Vector2 playerPosition = playerController.getPlayerPosition();
            CollisionRect collisionRect = new CollisionRect(
                playerPosition.x,
                playerPosition.y,
                playerController.getTexture().getWidth() * 3,
                playerController.getTexture().getHeight() * 3
            );

            if (bullet.getCollisionRect().collidesWith(collisionRect)) {
                playerController.getCurrentPlayer().takeDamage(bullet.getDamage());
                bullet.setActive(false);
            }

            Sprite bulletSprite = bullet.getSprite();

            float drawX = bullet.getPosition().x - playerPosition.x + Gdx.graphics.getWidth()  / 2f;
            float drawY = bullet.getPosition().y - playerPosition.y + Gdx.graphics.getHeight() / 2f;
            bulletSprite.setPosition(
                drawX - bulletSprite.getWidth()  / 2f,
                drawY - bulletSprite.getHeight() / 2f
            );
            bullet.getCollisionRect().move(
                drawX - bullet.getCollisionRect().width / 2f,
                drawY - bullet.getCollisionRect().height / 2f
            );

            bulletSprite.draw(Main.getBatch());
        }
    }

    private void handleEnemyAnimation(Enemy enemy) {
        Animation<Texture> animationToPlay = null;
        Animation<TextureRegion> animation = null;
        boolean loopAnimation = true;

        enemy.setIdle(enemy.getMovementState() == Enemy.EnemyMovementState.Idle);

        switch (enemy.getMovementState()) {
            case Spawning:
                animationToPlay = enemy.getType().getSpawnAnimation();
                loopAnimation = false;
                if (animationToPlay.isAnimationFinished(enemy.getTimeElapsedInCurrentMove()))
                    enemy.setMovementState(Enemy.EnemyMovementState.Walking);
                break;
            case Idle:
                animationToPlay = enemy.getType().getIdleAnimation();
                break;
            case Walking:
                animationToPlay = enemy.getType().getWalkAnimation();
                break;
            case Aiming:
                animationToPlay = enemy.getType().getIdleAnimation();
                break;
            case Shooting:
                animationToPlay = enemy.getType().getIdleAnimation();
                break;
            case Dying:
                animation = GameAssetManager.getDeathAnimation1();
                loopAnimation = false;
                if (animation.isAnimationFinished(enemy.getTimeElapsedInCurrentMove()))
                    enemy.setActive(false);
                break;
            case PreparingDash:
                animationToPlay = enemy.getType().getWalkAnimation();
                break;
            case Dashing:
                animationToPlay = enemy.getType().getWalkAnimation();
                break;
        }

        if (animationToPlay != null)
            enemy.getSprite().setRegion(animationToPlay.getKeyFrame(enemy.getTimeElapsedInCurrentMove(), loopAnimation));
        else
            enemy.getSprite().setRegion(animation.getKeyFrame(enemy.getTimeElapsedInCurrentMove(), loopAnimation));
        enemy.addToStateTime(Gdx.graphics.getDeltaTime());
    }

    private void spawnTrees(float gameTime) {
        int baseTreeCount = 5;
        int treeToSpawn = baseTreeCount;

        switch ((int) gameTime) {
            case 5:
                treeToSpawn = baseTreeCount * 2;
                break;
            case 10:
                treeToSpawn = baseTreeCount * 3;
                break;
            case 20:
                treeToSpawn = baseTreeCount * 4;
        }

        for (int i = 0; i < treeToSpawn; i++) {
            Enemy tree = new Enemy(EnemyType.Tree);

            setEnemyNor(tree);

            enemies.add(tree);
        }
    }

    private void spawnTentacles(int numberOfSpawnedEnemies) {
        for (int i = 0; i < numberOfSpawnedEnemies; i++) {
            Enemy tentacle = new Enemy(EnemyType.TentacleMonster);

            setEnemyNor(tentacle);
            tentacle.setMovementState(Enemy.EnemyMovementState.Spawning);

            tentacle.getType().getSpawnAnimation().setPlayMode(Animation.PlayMode.NORMAL);

            enemies.add(tentacle);
        }
    }

    private void spawnEyeBats(int numberOfSpawnedEnemies) {
        for (int i=0; i < numberOfSpawnedEnemies; i++) {
            Enemy eyeBat = new Enemy(EnemyType.EyeBat);

            setEnemyNor(eyeBat);
            eyeBat.setMovementState(Enemy.EnemyMovementState.Walking);

            enemies.add(eyeBat);
        }
    }

    private void setEnemyNor(Enemy enemy) {
        float x = MathUtils.random(0f, 3710f);
        float y = MathUtils.random(0f, 2600f);

        enemy.setEnemyPosition(new Vector2(x, y));
        enemy.setSprite(new Sprite(enemy.getType().getTexture()));
        enemy.setEnemyRect(new CollisionRect(
            x,
            y,
            enemy.getType().getTexture().getWidth(),
            enemy.getType().getTexture().getHeight()
        ));
    }

    public void setEyebatSpawned() {
        this.eyebatSpawned = true;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void setElderSpawned() {
        this.elderSpawned = true;
    }
}
