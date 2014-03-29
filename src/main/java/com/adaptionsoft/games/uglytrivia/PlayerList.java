package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class PlayerList {

    private List<Player> players = new ArrayList<Player>();

    int currentPlayerIndex = 0;

    public int count() {
        return players.size();
    }

    public void add(Player player) {
        players.add(player);
    }

    public Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void next() {
        currentPlayerIndex++;
        if (currentPlayerIndex == count()) currentPlayerIndex = 0;
    }
}
