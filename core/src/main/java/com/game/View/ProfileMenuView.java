package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.ProfileMenuController;
import com.game.Model.Player.Player;
import org.w3c.dom.Text;

public class ProfileMenuView implements Screen {
    private Stage stage;
    private final Player currentPlayer;
    private final TextButton changeUsernameButton;
    private final TextButton changePasswordButton;
    private final TextButton deleteAccountButton;
    private final TextButton changeAvatarButton;
    private final TextButton backButton;
    private final ProfileMenuController controller;
    private final Label successMessageLabel;
    private Label usernameLabel;
    private Image avatarImage;
    public Table table;

    public ProfileMenuView(ProfileMenuController controller, Skin skin, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.controller = controller;
        this.usernameLabel = new Label("Profile: " + currentPlayer.getUsername(), skin, "title");
        this.avatarImage = new Image(this.currentPlayer.getAvatar().getPortrait());
        this.changeUsernameButton = new TextButton("Change Username", skin);
        this.changePasswordButton = new TextButton("Change Password", skin);
        this.deleteAccountButton = new TextButton("Delete Account", skin);
        this.changeAvatarButton = new TextButton("Change Avatar", skin);
        this.backButton = new TextButton("Back", skin);
        this.successMessageLabel = new Label("", skin);
        this.successMessageLabel.setColor(Color.GREEN);
        this.table = new Table();

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        Table buttonsTable = new Table();
        buttonsTable.add(changeUsernameButton).pad(10).width(450).row();
        buttonsTable.add(changePasswordButton).pad(10).width(450).row();
        buttonsTable.add(changeAvatarButton).pad(10).width(450).row();
        buttonsTable.add(deleteAccountButton).pad(40, 10, 10, 10).width(450).row();
        buttonsTable.add(backButton).pad(10).row();

        mainTable.add(usernameLabel).padBottom(20).row();
        mainTable.add(avatarImage).width(128).height(128).padBottom(20).row();
        mainTable.add(buttonsTable).row();
        mainTable.add(successMessageLabel).padTop(20);

        stage.addActor(mainTable);

        changeUsernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleChangeUsername();
            }
        });

        changePasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleChangePassword();
            }
        });

        changeAvatarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleChangeAvatar();
            }
        });

        deleteAccountButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleDeleteAccount();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.handleBack();
            }
        });
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    public void updateAvatarDisplay() {
        avatarImage.setDrawable(new Image(currentPlayer.getAvatar().getPortrait()).getDrawable());
        usernameLabel.setText("Profile: " + currentPlayer.getUsername());
    }

    public void showSuccessMessage(String message) {
        successMessageLabel.setText(message);
        successMessageLabel.clearActions();
        successMessageLabel.addAction(Actions.sequence(
            Actions.fadeIn(0.3f),
            Actions.delay(2f),
            Actions.fadeOut(0.5f)
        ));
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Stage getStage() {
        return stage;
    }

    public void handleDroppedFile(String fileName) {
        controller.processNewAvatarFile(fileName);
    }

    public void showErrorMessage(String message) {
        successMessageLabel.setText(message);
        successMessageLabel.setColor(Color.RED);
        successMessageLabel.clearActions();
        successMessageLabel.addAction(Actions.sequence(
            Actions.fadeIn(0.3f),
            Actions.delay(2.5f),
            Actions.fadeOut(0.5f)
        ));
    }
}
