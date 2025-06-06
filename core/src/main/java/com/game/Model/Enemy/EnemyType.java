package com.game.Model.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.game.Model.Manage.GameAssetManager;

public enum EnemyType {
    Tree(0, new Texture(Gdx.files.internal("Tree/T_TreeMonster_0.png")),
        null, GameAssetManager.getTreeIdleAnimation(), GameAssetManager.getTreeWalkAnimation(),
        null),
    TentacleMonster(25, new Texture(Gdx.files.internal("TentacleMonster/TentacleIdle0.png")),
        GameAssetManager.getTentacleSpawnAnimation(), GameAssetManager.getTentacleIdleAnimation(),
        GameAssetManager.getTentacleIdleAnimation(),null),
    EyeBat(50, new Texture(Gdx.files.internal("EyeBat/EyeBat_0.png")),
        null, GameAssetManager.getEyeBatIdleAnimation(), GameAssetManager.getEyeBatIdleAnimation(),
        new Texture(Gdx.files.internal("EyeBat/EyeBat_EM.png"))),
    Elder(400,new Texture(Gdx.files.internal("Elder/Elder_0.png")),null,null,
        new Animation<>(0.15f, new Texture(Gdx.files.internal("Elder/Elder_0.png"))),
        new Texture(Gdx.files.internal("Elder/Elder_EM.png"))),;

    private final int HP;
    private final Texture texture;
    private final Animation<Texture> spawnAnimation;
    private final Animation<Texture> idleAnimation;
    private final Animation<Texture> walkAnimation;
    private final Texture emTexture;

    EnemyType(int hp, Texture texture, Animation<Texture> spawnAnimation, Animation<Texture> idleAnimation, Animation<Texture> walkAnimation, Texture emTexture) {
        HP = hp;
        this.texture = texture;
        this.spawnAnimation = spawnAnimation;
        this.idleAnimation = idleAnimation;
        this.walkAnimation = walkAnimation;
        this.emTexture = emTexture;
    }

    public int getHP() {
        return HP;
    }

    public Texture getTexture() {
        return texture;
    }

    public Animation<Texture> getSpawnAnimation() {
        return spawnAnimation;
    }

    public Animation<Texture> getIdleAnimation() {
        return idleAnimation;
    }

    public Animation<Texture> getWalkAnimation() {
        return walkAnimation;
    }

    public Texture getEmTexture() {
        return emTexture;
    }
}
