package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.game.Main;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Player;
import com.game.View.GameView;

public class GameController {
    private GameView view;
    private PlayerController playerController;
    private WorldController worldController;
    private GunController gunController;
    private EnemyController enemyController;
    private BitmapFont font;
    private final float totalGameTime;
    private float elapsedTime;
    private Texture ammoIconTexture;
    private Texture reloadBarBgTexture, reloadBarFgTexture;
    private final ShapeRenderer shapeRenderer;
    private final Texture vignetteTexture;

    public GameController(Player currentPlayer, float gameTime,
                          Texture texture,
                          Animation<Texture> idleAnimation,
                          Animation<Texture> runingAnimation) {
        this.totalGameTime = gameTime * 60;
        this.elapsedTime = 0f;

        Texture backgroundTexture = new Texture("background.png");
        this.ammoIconTexture = new Texture("Sprite/T_AmmoIcon.png");
        this.reloadBarBgTexture = new Texture("Sprite/T_ReloadBar_0.png");
        this.reloadBarFgTexture = new Texture("Sprite/T_ReloadBar_1.png");
        this.playerController = new PlayerController(currentPlayer,texture,idleAnimation,
            runingAnimation,backgroundTexture.getWidth(),backgroundTexture.getHeight());
        this.worldController = new WorldController(this.playerController);
        this.enemyController = new EnemyController(this.playerController, gameTime);
        this.playerController.setEnemyController(this.enemyController);
        this.gunController = new GunController(currentPlayer.getGun(),this.playerController,this.enemyController);
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
        this.vignetteTexture = new Texture("vignette.png");
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void update() {

        elapsedTime += Gdx.graphics.getDeltaTime();

        if (view != null) {
            worldController.update();
            playerController.update();
            enemyController.update();
            gunController.update();
        }

        if (elapsedTime / totalGameTime >= 0.25f)
            enemyController.setEyebatSpawned();

        if (elapsedTime / totalGameTime >= 0.5f)
            enemyController.setElderSpawned();

        SpriteBatch batch = Main.getBatch();

        Matrix4 oldProjectionMatrix = batch.getProjectionMatrix().cpy();

        Matrix4 uiProjectionMatrix = new Matrix4();
        uiProjectionMatrix.setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.setProjectionMatrix(uiProjectionMatrix);

        batch.setColor(1,1,1,1.5f);
        batch.draw(vignetteTexture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.setColor(1,1,1,1);

        float textX = 10f;
        float textY = Gdx.graphics.getHeight() - 20f;

        float remainingTime = totalGameTime - elapsedTime;
        if (remainingTime < 0)
            remainingTime = 0;

        int minutes = (int) (remainingTime / 60);
        int seconds = (int) (remainingTime % 60);
        String timeText = String.format("%02d:%02d", minutes, seconds);
        font.draw(batch, timeText, textX, textY);

        int maxHp = playerController.getCurrentPlayer().getMaxHp();
        int currentHp = playerController.getCurrentPlayer().getCurrentHp();

        float heartSize = 50f;
        float heartSpacing = 4f;
        float heartsStartX = 5f;
        float heartsStartY = textY - font.getLineHeight() - 10f - 50f;

        for (int i = 0; i < maxHp; i++) {
            Texture currentHeartFrameTexture;
            if (i < currentHp) {
                currentHeartFrameTexture = GameAssetManager.getHeartAnimation().getKeyFrame(elapsedTime,true);
            } else {
                currentHeartFrameTexture = GameAssetManager.getDeadHeartAnimation().getKeyFrame(elapsedTime,false);
            }

            float currentHeartX = heartsStartX + i * (heartSize + heartSpacing);
            if (currentHeartFrameTexture != null) {
                batch.draw(currentHeartFrameTexture, currentHeartX, heartsStartY, heartSize, heartSize);
            }
        }

        int currentGunAmmo = playerController.getCurrentPlayer().getGun().getCurrentAmmo();
        int maxGunAmmo = playerController.getCurrentPlayer().getGun().getType().getAmmo();

        int fixedHeartCountForWidth = 2;
        float availableWidthForAmmoIcons = (fixedHeartCountForWidth * heartSize) + ((fixedHeartCountForWidth - 1) * heartSpacing);

        float ammoIconsLayoutStartX = heartsStartX;
        float firstAmmoRowBaseY = heartsStartY - 10f;

        int numCols = 1;
        float ammoIconRenderWidth = 16f;
        float ammoIconRenderHeight = 26f;
        float ammoIconSpacingX = 2f;
        float ammoIconSpacingY = 2f;

        if (maxGunAmmo == 2)
            numCols = 2;
        else if (maxGunAmmo == 6)
            numCols = 3;
        else if (maxGunAmmo == 24)
            numCols = 6;

        ammoIconRenderWidth = (availableWidthForAmmoIcons - (numCols > 1 ? (numCols - 1) * ammoIconSpacingX : 0)) / numCols;
        ammoIconRenderHeight = ammoIconRenderWidth * ((float) ammoIconTexture.getHeight() / ammoIconTexture.getWidth());

        if (ammoIconRenderWidth < 5f) ammoIconRenderWidth = 5f;
        if (ammoIconRenderHeight < 5f) ammoIconRenderHeight = 5f;

        for (int i = 0; i < currentGunAmmo; i++) {
            int col = i % numCols;
            int row = i / numCols;

            float iconX = ammoIconsLayoutStartX + col * (ammoIconRenderWidth + ammoIconSpacingX);
            float iconY = firstAmmoRowBaseY - (row * (ammoIconRenderHeight + ammoIconSpacingY)) - ammoIconRenderHeight;

            batch.draw(ammoIconTexture, iconX, iconY, ammoIconRenderWidth, ammoIconRenderHeight);
        }

        if (gunController.isGunReloading()) {
            float reloadProgressRatio = gunController.getGunReloadProgressRatio();

            float barWidth = 150f;
            float barHeight = 20f;

            float barX = (Gdx.graphics.getWidth() - barWidth) / 2f;
            float barY = (heartsStartY - 50f);

            batch.draw(reloadBarBgTexture, barX, barY, barWidth, barHeight);

            batch.draw(reloadBarFgTexture, barX, barY,
                barWidth * reloadProgressRatio, barHeight,
                0,0,(int)(reloadBarFgTexture.getWidth() * reloadProgressRatio),
                reloadBarFgTexture.getHeight(),
                false,false);
        }

        Player currentPlayer = playerController.getCurrentPlayer();

        float xpRatio = (float)currentPlayer.getCurrentXp() / currentPlayer.getXpForNextLevel();
        String xpText = "LVL " + currentPlayer.getLevel() + " (" + currentPlayer.getCurrentXp() + "/" + currentPlayer.getXpForNextLevel() + ")";

        float xpBarWidth = 200f;
        float xpBarHeight = 15f;
        float xpBarX = Gdx.graphics.getWidth() / 2f - xpBarWidth / 2f;
        float xpBarY = 15f;

        batch.draw(reloadBarBgTexture, xpBarX, xpBarY, xpBarWidth, xpBarHeight);
        batch.draw(reloadBarFgTexture, xpBarX, xpBarY, xpBarWidth * xpRatio, xpBarHeight);

        font.draw(batch, xpText, xpBarX, xpBarY + xpBarHeight + 15);

        batch.setProjectionMatrix(oldProjectionMatrix);
    }

    public WorldController getWorldController() {
        return worldController;
    }

    public GunController getGunController() {
        return gunController;
    }
}
