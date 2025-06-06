package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.GameController;
import com.game.Main;
import com.game.Model.Manage.GameAssetManager;

import java.awt.*;

public class GameView implements Screen, InputProcessor {
    private Stage stage;
    private final GameController controller;
    private Cursor customCursor;

    public GameView(GameController controller) {
        this.controller = controller;
        controller.setView(this);

        try {
            Pixmap pixmap = new Pixmap(Gdx.files.internal("Sprite/T_CursorSprite.png"));
            customCursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);
            Gdx.graphics.setCursor(customCursor);
            pixmap.dispose();
        } catch (Exception e) {
            Gdx.app.error("Cursor", "Failed to load custom cursor", e);
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Main.getBatch().begin();
        controller.update();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        if (customCursor != null) {
            customCursor.dispose();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.R) {
            controller.getGunController().startReload();
            return true;
        }
        if (i == Input.Keys.SPACE) {
            controller.getGunController().toggleAutoAim();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        controller.getGunController().handleWeaponShoot(i,i1);
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        controller.getGunController().handleGunRotation(i,i1);
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
