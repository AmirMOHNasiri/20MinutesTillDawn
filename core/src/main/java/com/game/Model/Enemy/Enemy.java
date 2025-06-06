package com.game.Model.Enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.game.Model.CollisionRect;

public class Enemy {

    public enum EnemyMovementState {
        Idle,
        Walking,
        Spawning,
        Dying,
        Aiming,
        Shooting,
        PreparingDash,
        Dashing
    }

    private EnemyMovementState movementState = EnemyMovementState.Idle;
    private EnemyMovementState previousState;
    private final EnemyType type;
    private int health;
    private CollisionRect enemyRect;
    private Sprite sprite;
    private Vector2 enemyPosition;
    private Vector2 dashTargetPosition;
    private Vector2 startPosition;
    private boolean active;

    private boolean isIdle;
    private float timeElapsedInCurrentMove = 0f;
    private float actionTimer = 0f;

    public Enemy(EnemyType type) {
        this.type = type;
        this.health = type.getHP();
        this.active = true;
    }

    public EnemyType getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void healthDecrease(int damage) {
        this.health -= damage;
    }

    public CollisionRect getEnemyRect() {
        return enemyRect;
    }

    public Vector2 getEnemyPosition() {
        return enemyPosition;
    }

    public void setEnemyRect(CollisionRect enemyRect) {
        this.enemyRect = enemyRect;
    }

    public void setEnemyPosition(Vector2 enemyPosition) {
        this.enemyPosition = enemyPosition;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public float getTimeElapsedInCurrentMove() {
        return timeElapsedInCurrentMove;
    }

    public void addToStateTime(float deltaTime) {
        this.timeElapsedInCurrentMove += deltaTime;
    }

    public void setTimeElapsedInCurrentMove(float timeElapsedInCurrentMove) {
        this.timeElapsedInCurrentMove = timeElapsedInCurrentMove;
    }

    public EnemyMovementState getMovementState() {
        return movementState;
    }
    public EnemyMovementState getPreviousState() { return previousState; }

    public void setMovementState(EnemyMovementState movementState) {
        this.movementState = movementState;
    }
    public void setPreviousState(EnemyMovementState movementState) { this.previousState = movementState; }

    public void triggerHitState(int damage) {
        if (movementState == EnemyMovementState.Dying) return;

        healthDecrease(damage);

        if (getType() != EnemyType.Tree && getHealth() <= 0) {
            setMovementState(EnemyMovementState.Dying);
            setTimeElapsedInCurrentMove(0f);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getActionTimer() {
        return actionTimer;
    }
    public void addToActionTimer(float delta) {
        this.actionTimer += delta;
    }
    public void resetActionTimer() {
        this.actionTimer = 0f;
    }

    public Vector2 getDashTargetPosition() {
        return dashTargetPosition;
    }

    public void setDashTargetPosition(Vector2 dashTargetPosition) {
        this.dashTargetPosition = dashTargetPosition;
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vector2 startPosition) {
        this.startPosition = startPosition;
    }
}
