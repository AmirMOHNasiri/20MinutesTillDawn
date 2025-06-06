package com.game.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.Control.MainMenuController;
import com.game.Control.ScoreboardMenuController;
import com.game.Main;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Player;

import java.util.List;

public class ScoreboardMenuView implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final ScoreboardMenuController controller;
    private final Table dataTable;
    private final Player currentPlayer;

    private final Color bronzeColor = new Color(0.8f, 0.5f, 0.2f, 1f);

    public ScoreboardMenuView(ScoreboardMenuController controller, Skin skin, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.stage = new Stage(new ScreenViewport());
        this.skin = skin;
        this.controller = controller;
        this.dataTable = new Table(skin);
        controller.setView(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table mainLayout = new Table();
        mainLayout.setFillParent(true);
        mainLayout.pad(20);

        Table headerTable = new Table(skin);
        TextButton sortByNameButton = new TextButton("Username", skin, "toggle");
        TextButton sortByScoreButton = new TextButton("Score", skin, "toggle");
        TextButton sortByKillsButton = new TextButton("Kills", skin, "toggle");
        TextButton sortByTimeButton = new TextButton("Time Alive", skin, "toggle");
        headerTable.add(new Label("#", skin)).width(50);
        headerTable.add(sortByNameButton).expandX().fillX();
        headerTable.add(sortByScoreButton).width(150);
        headerTable.add(sortByKillsButton).width(120);
        headerTable.add(sortByTimeButton).width(280);
        dataTable.top();
        ScrollPane scrollPane = new ScrollPane(dataTable, skin);
        scrollPane.setFadeScrollBars(false);
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        mainLayout.add(new Label("Scoreboard", skin, "title")).padBottom(20).row();
        mainLayout.add(headerTable).expandX().fillX().padBottom(5).row();
        mainLayout.add(scrollPane).expand().fill().padBottom(20).row();
        mainLayout.add(backButton);
        stage.addActor(mainLayout);
        sortByNameButton.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { controller.sortByName(); } });
        sortByScoreButton.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { controller.sortByScore(); } });
        sortByKillsButton.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { controller.sortByKills(); } });
        sortByTimeButton.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { controller.sortByTime(); } });
        backButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                 Main.getMain().setScreen(new MainMenuView(new MainMenuController(), skin, currentPlayer));
            }
        });
    }

    public void rebuildTable(List<Player> players, Player currentPlayer) {
        dataTable.clearChildren();

        int rank = 1;
        for (Player p : players) {
            if (rank > 10) break;

            Color rowColor = Color.WHITE;
            if (p.getUsername().equals(currentPlayer.getUsername())) {
                rowColor = Color.CYAN;
            } else if (rank == 1) {
                rowColor = Color.GOLD;
            } else if (rank == 2) {
                rowColor = Color.SLATE;
            } else if (rank == 3) {
                rowColor = bronzeColor;
            }

            long minutes = (long) (p.getMostTimeAlive() / 60);
            long seconds = (long) (p.getMostTimeAlive() % 60);
            String timeAliveStr = String.format("%02d:%02d", minutes, seconds);

            Label rankLabel = new Label(String.valueOf(rank), skin);
            Label userLabel = new Label(p.getUsername(), skin);
            Label scoreLabel = new Label(String.valueOf(p.getScore()), skin);
            Label killsLabel = new Label(String.valueOf(p.getKills()), skin);
            Label timeLabel = new Label(timeAliveStr, skin);

            rankLabel.setColor(rowColor);
            userLabel.setColor(rowColor);
            scoreLabel.setColor(rowColor);
            killsLabel.setColor(rowColor);
            timeLabel.setColor(rowColor);

            dataTable.add(rankLabel).width(50);
            dataTable.add(userLabel).expandX().fillX().align(Align.left).padLeft(10);
            dataTable.add(scoreLabel).width(150);
            dataTable.add(killsLabel).width(120);
            dataTable.add(timeLabel).width(180);
            dataTable.row().padTop(5).padBottom(5);

            rank++;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
