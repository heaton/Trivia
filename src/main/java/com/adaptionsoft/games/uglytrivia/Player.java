package com.adaptionsoft.games.uglytrivia;

public class Player {

    private static final int MAX_PLACE = 12;
    private static final int COINS_ON_WIN = 6;

    private String name;
    private int place;
    private int purse;
    private boolean inPenaltyBox;

    public Player(String name) {
        this.name = name;
        this.place = 0;
        this.purse = 0;
        this.inPenaltyBox = false;
    }

    public String name(){
        return name;
    }

    public void move(int step) {
        place += step;
        if (place >= MAX_PLACE) {
            place = place - MAX_PLACE;
        }
    }

    public int place() {
        return place;
    }

    public void winOneCoin() {
        purse++;
    }

    public int purse() {
        return purse;
    }

    public void goIntoPenaltyBox() {
        inPenaltyBox = true;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public boolean isWin() {
        return purse < COINS_ON_WIN;
    }

}
