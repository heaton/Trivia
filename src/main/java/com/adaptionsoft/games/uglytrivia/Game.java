package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;

public class Game {

    private PlayerList players = new PlayerList();

    private QuestionStorage questions = new QuestionStorage();

    private Notifier notifier = new Notifier();

    private int currentRoll;

    public Game(){
        questions.init();
    }

	public boolean addPlayer(String playerName) {
        players.add(playerName);
        notifier.playerAdded(playerName, howManyPlayers());
        return true;
    }

    public boolean isPlayable() {
        return howManyPlayers() >= 2;
    }

    public int howManyPlayers() {
		return players.count();
	}

	public void roll(int number) {
        hold(number);

		if (stayInPenaltyBox()) {
            notOutOfPenaltyBox();
            return;
        }

        outPenaltyBoxIfNeed();

        move(number);
        askQuestion();

	}

    private void notOutOfPenaltyBox() {
        notifier.notGettingOutOfPenaltyBox(currentPlayer().name());
    }

    private void outPenaltyBoxIfNeed() {
        if (currentPlayer().isInPenaltyBox()) {
            notifier.gettingOutOfPenaltyBox(currentPlayer().name());
        }
    }

    private void hold(int number) {
        currentRoll = number;
        notifier.rolled(currentPlayer().name(), number);
    }

    private Player currentPlayer() {
        return players.currentPlayer();
    }

    private boolean stayInPenaltyBox() {
        return currentPlayer().isInPenaltyBox() && notGetoutOfPenaltyBox();
    }

    private boolean notGetoutOfPenaltyBox() {
        return currentRoll % 2 == 0;
    }

    private void move(int number) {
        currentPlayer().move(number);
        notifier.newLocation(currentPlayer().name(), currentPlayer().place());
    }

    private void askQuestion() {
        String question = currentQuestion().pop();
        notifier.newQuestion(currentQuestion().category(), question);
    }

    private QuestionQueue currentQuestion(){
        int questionIndex = currentPlayer().place() % questions.size();
        return questions.get(questionIndex);
    }

	public boolean wasCorrectlyAnswered() {
		if (stayInPenaltyBox()){
            nextPlayer();
            return true;
        }

        return correctlyAnswerAndAddCoin();
	}

    private boolean correctlyAnswerAndAddCoin() {
        Player currentPlayer = currentPlayer();
        awardOneCoin(currentPlayer);

        nextPlayer();

        return currentPlayer.isWin();
    }

    private void awardOneCoin(Player player) {
        player.winOneCoin();
        notifier.correctAndCurrentCoins(player.name(), player.purse());
    }

    private void nextPlayer() {
        players.next();
    }

    public boolean wrongAnswer() {
        sendToPenaltyBox();

        nextPlayer();
        return true;
    }

    private void sendToPenaltyBox() {
        if(stayInPenaltyBox()) return;
        currentPlayer().goIntoPenaltyBox();
        notifier.incorrectAndSendToPenaltyBox(currentPlayer().name());
    }

}
