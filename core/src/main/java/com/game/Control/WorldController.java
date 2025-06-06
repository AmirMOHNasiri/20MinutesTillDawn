package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.game.Main;

public class WorldController {
    private PlayerController playerController;
    private Texture backgroundTexture;
    private float backgroundX = 0, backgroundY = 0;

    public WorldController(PlayerController playerController) {
        this.playerController = playerController;
        this.backgroundTexture = new Texture("background.png");
    }

    public void update() {
        backgroundX = playerController.getCurrentPlayer().getPosition().x;
        backgroundY = playerController.getCurrentPlayer().getPosition().y;
        Main.getBatch().draw(backgroundTexture, backgroundX, backgroundY);
    }
}
