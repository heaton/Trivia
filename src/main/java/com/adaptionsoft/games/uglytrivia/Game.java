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
        currentRoll = number;
        notifier.rolled(currentPlayer().name(), number);

		if (stayInPenaltyBox()) {
            notifier.notGettingOutOfPenaltyBox(currentPlayer().name());
            return;
        }

        if (currentPlayer().isInPenaltyBox()) {
            notifier.gettingOutOfPenaltyBox(currentPlayer().name());
        }

        moveAndAskQuestion(number);

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

    private void moveAndAskQuestion(int number) {
        currentPlayer().move(number);
        notifier.newLocation(currentPlayer().name(), currentPlayer().place());

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
        currentPlayer().winOneCoin();

        notifier.correctAndCurrentCoins(currentPlayer().name(), currentPlayer().purse());

        boolean hasWinner = currentPlayer().isWin();
        nextPlayer();

        return hasWinner;
    }

    private void nextPlayer() {
        players.next();
    }

    public boolean wrongAnswer() {
        if(!stayInPenaltyBox()){
            notifier.incorrectAndSendToPenaltyBox(currentPlayer().name());
            currentPlayer().goIntoPenaltyBox();
        }

        nextPlayer();
        return true;
    }

}
