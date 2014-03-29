package com.adaptionsoft.games.uglytrivia;

public class Messages {

    public String wasAdded(String player) {
        return player + " was added";
    }

    public String playerNumber(int playerCount) {
        return "They are player number " + playerCount;
    }

    public String currentPlayer(String player) {
        return player + " is the current player";
    }

    public String haveRolled(int number) {
        return "They have rolled a " + number;
    }

    public String notGettingOutOfPenaltyBox(String player) {
        return player + " is not getting out of the penalty box";
    }

    public String gettingOutOfPenaltyBox(String player) {
        return player  + " is getting out of the penalty box";
    }

    public String newLocation(String player, int place) {
        return player + "'s new location is " + place;
    }

    public String categoryIs(String questionCategory) {
        return "The category is " + questionCategory;
    }

    public String correctAnswer() {
        return "Answer was correct!!!!";
    }

    public String currentCoins(String player, int purse) {
        return player + " now has " + purse + " Gold Coins.";
    }

    public String incorrectAnswer() {
        return "Question was incorrectly answered";
    }

    public String sendToPenaltyBox(String player) {
        return player + " was sent to the penalty box";
    }

    public static Messages getDefault() {
        return new Messages();
    }

}
