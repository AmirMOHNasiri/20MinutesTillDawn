package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.MainMenuController;
import com.game.Main;
import com.game.Model.Player.Player;

public class MainMenuView implements Screen {
    private Stage stage;
    private final Player currentPlayer;
    private final Image avatarImage;
    private final Label usernameLabel;
    private final Label scoreLabel;
    private final TextButton playButton;
    private final TextButton settingsButton;
    private final TextButton profileButton;
    private final TextButton scoreboardButton;
    private final TextButton talentButton;
    private final TextButton continueButton;
    private final TextButton logoutButton;
    public Table table;
    private final MainMenuController controller;

    public MainMenuView(MainMenuController controller, Skin skin, Player player) {
        this.controller = controller;
        this.currentPlayer = player;
        avatarImage = new Image(this.currentPlayer.getAvatar().getPortrait());
        this.usernameLabel = new Label(this.currentPlayer.getUsername(), skin);
        this.scoreLabel = new Label("Score: "+this.currentPlayer.getScore(), skin);
        this.playButton = new TextButton(      "play", skin);
        this.settingsButton = new TextButton(  "settings", skin);
        this.profileButton = new TextButton(   "profile", skin);
        this.scoreboardButton = new TextButton("scoreboard", skin);
        this.talentButton = new TextButton(    "talent", skin);
        this.continueButton = new TextButton(  "continue", skin);
        this.logoutButton = new TextButton(    "logout", skin);
        this.table = new Table();

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table playerTable = new Table();
        Table buttonTable = new Table();
        stage.addActor(table);

        table.setFillParent(true);
        table.center();

        float widthI = avatarImage.getWidth(), heightI = avatarImage.getHeight();
        playerTable.add(avatarImage).left().padRight(80).width(widthI*0.5f).height(heightI*0.5f);
        playerTable.add(usernameLabel).center();
        playerTable.add(scoreLabel).right().padLeft(80);
        buttonTable.add(playButton).width(scoreboardButton.getWidth());
        buttonTable.add(profileButton).row();
        buttonTable.add(continueButton).width(scoreboardButton.getWidth());
        buttonTable.add(talentButton).width(profileButton.getWidth()).row();
        buttonTable.add(scoreboardButton);
        buttonTable.add(logoutButton).width(profileButton.getWidth()).row();
        buttonTable.add(settingsButton).width(scoreboardButton.getWidth()+profileButton.getWidth()).colspan(2).row();
        table.add(playerTable).row();
        table.add(buttonTable).row();

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.playButtonPressed();
            }
        });

        profileButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentPlayer.getPassword().isEmpty()) return;
                controller.profileButtonPressed();
            }
        });

        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.continueButtonPressed();
            }
        });

        talentButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.talentButtonPressed();
            }
        });

        scoreboardButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.scoreboardButtonPressed();
            }
        });

        logoutButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.logoutButtonPressed();
            }
        });

        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.settingsButtonPressed();
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

    public TextButton getLogoutButton() {
        return logoutButton;
    }

    public TextButton getContinueButton() {
        return continueButton;
    }

    public TextButton getTalentButton() {
        return talentButton;
    }

    public TextButton getScoreboardButton() {
        return scoreboardButton;
    }

    public TextButton getProfileButton() {
        return profileButton;
    }

    public TextButton getSettingsButton() {
        return settingsButton;
    }

    public TextButton getPlayButton() {
        return playButton;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
