package com.game.Control;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.game.Main;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Player;
import com.game.View.LoginMenuView;
import com.game.View.MainMenuView;

public class LoginMenuController {
    private LoginMenuView view;

    public void setView(LoginMenuView view) {
        this.view = view;
    }

    public void loginButtonPressed() {
        String username = view.getUsernameField().getText();
        String password = view.getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            view.getErrorLabel().setText("Please fill in all fields");
            return;
        }
        Player player = Player.getPlayerByUsername(username);
        if (player == null) {
            view.getErrorLabel().setText("Invalid username");
            return;
        }
        if (!player.getPassword().equals(password)) {
            view.getErrorLabel().setText("Invalid password");
            return;
        }
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin(), player));
    }

    public void forgetPasswordButtonPressed() {
        String username = view.getUsernameField().getText();
        Skin skin = GameAssetManager.getGameAssetManager().getSkin();

        if (username.isEmpty()){
            view.getErrorLabel().setText("Please fill in username");
            return;
        }
        Player player = Player.getPlayerByUsername(username);
        if (player == null) {
            view.getErrorLabel().setText("Invalid username");
            return;
        }

        final TextField newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("Enter new password");
        final TextField answerField = new TextField("", skin);
        answerField.setMessageText("Answer to security question");
        final Label errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        view.setDialogLabel(errorLabel);

        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                boolean onClicked = (boolean) object;
                if (onClicked) {
                    String newPassword = newPasswordField.getText();
                    String answer = answerField.getText();

                    if (newPassword.isEmpty() || answer.isEmpty()) {
                        errorLabel.setText("Please fill in all fields");
                        cancel();
                        return;
                    }

                    boolean valid = Player.isValidPassword(newPassword, errorLabel);
                    if (!valid) {
                        cancel();
                        return;
                    }
                    if (!player.getAnswer().equals(answer)) {
                        errorLabel.setText("Wrong answer");
                        cancel();
                        return;
                    }

                    player.setPassword(newPassword);
                    player.save();
                    view.getErrorLabel().setColor(Color.BLUE);
                    view.getErrorLabel().setText("Password changed successfully");
                }
                addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(this::hide)
                ));
            }
        };

        dialog.getContentTable().add(new Label("New Password", skin)).center().row();
        dialog.getContentTable().add(newPasswordField).width(300).padBottom(10).row();
        dialog.getContentTable().add(new Label("Answer to "+player.getQuestion().getQuestion(), skin)).center().row();
        dialog.getContentTable().add(answerField).width(300).padBottom(10).row();
        dialog.getContentTable().add(errorLabel).padTop(10).row();

        dialog.button("Ok", true);
        dialog.button("Cancel", false);
        dialog.setModal(true);

        dialog.show(view.getStage());

        dialog.getColor().a = 0f;
        dialog.setTransform(true);
        dialog.setScale(0f);
        dialog.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f,1f,0.3f, Interpolation.swingOut)
            )
        ));
    }
}
