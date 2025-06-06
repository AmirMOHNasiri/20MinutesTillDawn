package com.game.Control;

import com.game.Main;
import com.game.Model.Manage.GameAssetManager;
import com.game.View.*;

public class MainMenuController {
    private MainMenuView view;

    public void setView(MainMenuView view) {this.view = view;}

    public void playButtonPressed() {
        Main.getMain().setScreen(new PreGameMenuView(new PreGameMenuController(), GameAssetManager.getGameAssetManager().getSkin(), view.getCurrentPlayer()));
    }
    public void profileButtonPressed() {
        Main.getMain().setScreen(new ProfileMenuView(new ProfileMenuController(GameAssetManager.getGameAssetManager().getSkin(),view.getCurrentPlayer()),
            GameAssetManager.getGameAssetManager().getSkin(), view.getCurrentPlayer()));
    }
    public void continueButtonPressed() {}
    public void talentButtonPressed() {}
    public void scoreboardButtonPressed() {
        Main.getMain().setScreen(new ScoreboardMenuView(
            new ScoreboardMenuController(view.getCurrentPlayer()),
            GameAssetManager.getGameAssetManager().getSkin(),
            view.getCurrentPlayer()
        ));
    }
    public void logoutButtonPressed() {
        Main.getMain().setScreen(new LoginMenuView(new LoginMenuController(), GameAssetManager.getGameAssetManager().getSkin()));
    }
    public void settingsButtonPressed() {}
}
