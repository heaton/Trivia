package com.adaptionsoft.games.uglytrivia;

public class Notifier {

    private Messages messages;

    public Notifier() {
        messages = Messages.getDefault();
    }

    public void playerAdded(String player, int currentPlayerCount){
        sendMessage(messages.wasAdded(player));
        sendMessage(messages.playerNumber(currentPlayerCount));
    }

    private void sendMessage(String message){
        System.out.println(message);
    }

    public void rolled(String player, int number) {
        sendMessage(messages.currentPlayer(player));
        sendMessage(messages.haveRolled(number));
    }

    public void notGettingOutOfPenaltyBox(String player) {
        sendMessage(messages.notGettingOutOfPenaltyBox(player));
    }

    public void gettingOutOfPenaltyBox(String player) {
        sendMessage(messages.gettingOutOfPenaltyBox(player));
    }

    public void newLocation(String player, int place) {
        sendMessage(messages.newLocation(player, place));
    }

    public void newQuestion(String questionCategory, String question) {
        sendMessage(messages.categoryIs(questionCategory));
        sendMessage(question);
    }

    public void correctAndCurrentCoins(String player, int coins) {
        sendMessage(messages.correctAnswer());
        sendMessage(messages.currentCoins(player, coins));
    }

    public void incorrectAndSendToPenaltyBox(String player) {
        sendMessage(messages.incorrectAnswer());
        sendMessage(messages.sendToPenaltyBox(player));
    }

}
