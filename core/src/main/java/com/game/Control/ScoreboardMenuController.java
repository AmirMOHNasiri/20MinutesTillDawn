package com.game.Control;

import com.game.Model.Player.Player;
import com.game.View.ScoreboardMenuView;

import java.util.Comparator;
import java.util.List;

public class ScoreboardMenuController {
    private ScoreboardMenuView view;
    private final Player currentPlayer;
    private List<Player> allPlayers;

    private enum SortBy { SCORE, KILLS, TIME, NAME }
    private SortBy currentSort = SortBy.SCORE;

    public ScoreboardMenuController(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.allPlayers = Player.getAllPlayers();
        sortAndNotifyView();
    }

    public void setView(ScoreboardMenuView view) {
        this.view = view;
        view.rebuildTable(allPlayers, currentPlayer); // نمایش جدول برای اولین بار
    }

    public void sortByScore() {
        currentSort = SortBy.SCORE;
        sortAndNotifyView();
    }
    public void sortByKills() {
        currentSort = SortBy.KILLS;
        sortAndNotifyView();
    }
    public void sortByTime() {
        currentSort = SortBy.TIME;
        sortAndNotifyView();
    }
    public void sortByName() {
        currentSort = SortBy.NAME;
        sortAndNotifyView();
    }

    private void sortAndNotifyView() {

        switch (currentSort) {
            case SCORE:
                allPlayers.sort(Comparator.comparingInt(Player::getScoreAsInteger).reversed());
                break;
            case KILLS:
                allPlayers.sort(Comparator.comparingInt(Player::getKills).reversed());
                break;
            case TIME:
                allPlayers.sort(Comparator.comparingDouble(Player::getMostTimeAlive).reversed());
                break;
            case NAME:
                allPlayers.sort(Comparator.comparing(Player::getUsername));
                break;
        }
        // به View اطلاع بده تا جدول را دوباره بسازد
        if (view != null) {
            view.rebuildTable(allPlayers, currentPlayer);
        }
    }
}
