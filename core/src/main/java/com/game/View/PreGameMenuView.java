package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.PreGameMenuController;
import com.game.Main;
import com.game.Model.Player.Player;

public class PreGameMenuView implements Screen {
    private Stage stage;
    private final Player currentPlayer;
    private final PreGameMenuController controller;
    private final TextButton heroButton;
    private final TextButton gunButton;
    private final TextButton timeButton;
    private final TextButton playButton;
    private final Label errorLabel;
    private float time = 0f;
    public Table table;


    public PreGameMenuView(PreGameMenuController controller, Skin skin, Player currentPlayer) {
        this.controller = controller;
        this.currentPlayer = currentPlayer;
        this.heroButton = new TextButton("Hero", skin);
        this.gunButton = new TextButton("Gun", skin);
        this.timeButton = new TextButton("Time", skin);
        this.playButton = new TextButton("Play", skin);
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(Color.RED);

        this.table = new Table();

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table buttonTable = new Table();
        stage.addActor(table);

        table.setFillParent(true);
        table.center();
        buttonTable.add(heroButton).row();
        buttonTable.add(gunButton).row();
        buttonTable.add(timeButton).row();
        buttonTable.add(playButton).row();
        table.add(buttonTable).row();
        table.add(errorLabel).padTop(10);

        heroButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.heroButtonPressed();
            }
        });

        gunButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.GunButtonPressed();
            }
        });

        timeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.timeButtonPressed();
            }
        });

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.playButtonPressed();
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0,0,0,1);
        Main.getBatch().begin();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (!errorLabel.getText().isEmpty()){
            time += Gdx.graphics.getDeltaTime();

            if (time >= 1f){
                time = 0f;
                errorLabel.setText("");
            }
        }
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

    }

    @Override
    public void dispose() {

    }

    public Stage getStage() {
        return stage;
    }

    public TextButton getPlayButton() {
        return playButton;
    }

    public TextButton getTimeButton() {
        return timeButton;
    }

    public TextButton getGunButton() {
        return gunButton;
    }

    public TextButton getHeroButton() {
        return heroButton;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
