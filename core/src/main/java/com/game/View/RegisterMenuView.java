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
import com.game.Control.RegisterMenuController;
import com.game.Main;
import com.game.Model.Manage.FontManager;
import com.game.Model.Player.Question;

public class RegisterMenuView implements Screen {
    private Stage stage;
    private final TextField usernameField;
    private final TextField passwordField;
    private final SelectBox<String> questionsField;
    private final TextField answerField;
    private final TextButton registerButton;
    private final TextButton exitButton;
    private final TextButton loginButton;
    private final TextButton skipButton;
    private final Label errorLabel;
    public Table table;
    private float timer = 0f;
    private final RegisterMenuController controller;

    public RegisterMenuView(RegisterMenuController controller, Skin skin) {
        this.controller = controller;
        this.usernameField = new TextField("", skin);
        this.usernameField.getStyle().font = FontManager.getFont();
        this.usernameField.setMessageText("Enter your username");
        this.passwordField = new TextField("", skin);
        this.passwordField.setMessageText("Enter your password");
        this.questionsField = new SelectBox<>(skin);
        this.questionsField.getStyle().font = FontManager.getFont();
        this.questionsField.setItems(Question.Q1.getQuestion(), Question.Q2.getQuestion(), Question.Q3.getQuestion(), Question.Q4.getQuestion());
        this.answerField = new TextField("", skin);
        this.answerField.setMessageText("Enter your answer");
        this.registerButton = new TextButton("Register", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.loginButton = new TextButton("Login Menu", skin);
        this.skipButton = new TextButton("Skip", skin);
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(Color.RED);
        table = new Table();

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
        fieldTable.add(questionsField).width(480).row();
        fieldTable.add(answerField).width(480).row();
        buttonTable.add(exitButton);
        buttonTable.add(registerButton).width(loginButton.getWidth());
        buttonTable.row();
        buttonTable.add(skipButton).colspan(1).padTop(5);
        buttonTable.add(loginButton).colspan(1).padTop(5);
        table.add(fieldTable).row();
        table.add(buttonTable).padTop(5).row();
        table.add(errorLabel).padTop(15);

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.exitButtonPressed();
            }
        });

        registerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.registerButtonPressed();
            }
        });

        skipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.skipButtonPressed();
            }
        });

        loginButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.loginButtonPressed();
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
            timer += Gdx.graphics.getDeltaTime();

            if (timer >= 1f) {
                timer = 0f;
                errorLabel.setText("");
                errorLabel.setColor(Color.RED);
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

    public TextField getAnswerField() {
        return answerField;
    }

    public SelectBox<String> getQuestionsField() {
        return questionsField;
    }

    public TextButton getRegisterButton() {
        return registerButton;
    }

    public TextButton getExitButton() {
        return exitButton;
    }

    public TextButton getLoginButton() {
        return loginButton;
    }

    public TextButton getSkipButton() {
        return skipButton;
    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
