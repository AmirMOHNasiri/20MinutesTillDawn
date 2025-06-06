package com.game.Control;

import com.game.Main;
import com.game.Model.CollisionRect;
import com.game.Model.Gun.Gun;
import com.game.Model.Gun.GunType;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Avatar;
import com.game.Model.Player.Character;
import com.game.Model.Player.Player;
import com.game.Model.Player.Question;
import com.game.View.LoginMenuView;
import com.game.View.MainMenuView;
import com.game.View.RegisterMenuView;

public class RegisterMenuController {
    private RegisterMenuView view;

    public void setView(RegisterMenuView view) {
        this.view = view;
    }

    public void loginButtonPressed() {
        Main.getMain().setScreen(new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    public void registerButtonPressed() {
        String username = view.getUsernameField().getText();
        String password = view.getPasswordField().getText();
        String answer   = view.getAnswerField().getText();
        String question = view.getQuestionsField().getSelected();
        boolean valid;
        if (username.isEmpty() || password.isEmpty() || answer.isEmpty()) {
            view.getErrorLabel().setText("Please fill in all fields");
            return;
        }
        if (Player.getPlayerByUsername(username)!=null){
            view.getErrorLabel().setText("Username is already in use");
            return;
        }
        valid = Player.isValidPassword(password, view.getErrorLabel());
        if (!valid)
            return;
        Avatar avatar = Avatar.getRandomAvatar();
        Player player = new Player(username, password, avatar, Question.getQuestion(question), answer);
        player.setGun(new Gun(GunType.Revolver));
        player.setRect(new CollisionRect(0,0,0,0));
        player.save();
        Main.getMain().setScreen(new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }

    public void exitButtonPressed() {
        System.exit(0);
    }

    public void skipButtonPressed() {
        Player player = new Player("Guest", "", Avatar.getRandomAvatar(), null, null);
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.getGameAssetManager().getSkin(), player));
    }
}
