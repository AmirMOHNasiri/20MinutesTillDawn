package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.LoginMenuController;
import com.game.Main;
import com.game.Model.Manage.FontManager;

public class LoginMenuView implements Screen {
    private Stage stage;
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton loginButton;
    private final TextButton forgetPasswordButton;
    private final Label errorLabel;
    private Label dialogLabel;
    public Table table;
    private float timer = 0f;
    private final LoginMenuController controller;

    public LoginMenuView(LoginMenuController controller, Skin skin) {
        this.controller = controller;
        this.usernameField = new TextField("", skin);
        this.usernameField.getStyle().font = FontManager.getFont();
        this.usernameField.setMessageText("Enter your username");
        this.passwordField = new TextField("", skin);
        this.passwordField.setMessageText("Enter your password");
        this.loginButton = new TextButton("Login", skin);
        this.forgetPasswordButton = new TextButton("Forget Pass", skin);
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(Color.RED);

        this.table = new Table();

        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table fieldTable = new Table();
        Table buttonTable = new Table();
        stage.addActor(table);

        table.setFillParent(true);
        table.center();
        fieldTable.add(usernameField).width(480).row();
        fieldTable.add(passwordField).width(480).row();
        buttonTable.add(forgetPasswordButton).width(320);
        buttonTable.add(loginButton).width(160);
        table.add(fieldTable).row();
        table.add(buttonTable).padTop(10).row();
        table.add(errorLabel).padTop(15);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.loginButtonPressed();
            }
        });

        forgetPasswordButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.forgetPasswordButtonPressed();
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

        if (!errorLabel.getText().isEmpty() || (dialogLabel!=null && !dialogLabel.getText().isEmpty())) {
            timer += Gdx.graphics.getDeltaTime();

            if (timer >= 1f) {
                timer = 0f;
                errorLabel.setText("");
                errorLabel.setColor(Color.RED);
                if (dialogLabel != null) {
                    dialogLabel.setText("");
                    dialogLabel.setColor(Color.RED);
                }
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

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public TextButton getForgetPasswordButton() {
        return forgetPasswordButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }

    public Stage getStage() {
        return stage;
    }

    public void setDialogLabel(Label dialogLabel) {
        this.dialogLabel = dialogLabel;
    }
}
