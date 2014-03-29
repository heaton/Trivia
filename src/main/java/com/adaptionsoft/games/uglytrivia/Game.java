package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;

public class Game {

    private PlayerList players = new PlayerList();

    private QuestionStorage questions = new QuestionStorage();

    private Notifier notifier = new Notifier();

    private boolean isGettingOutOfPenaltyBox;

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
        notifier.rolled(currentPlayer().name(), number);

		if (isInPenaltyBox() && notGetoutOfPenaltyBox(number)) {
            isGettingOutOfPenaltyBox = false;
            notifier.notGettingOutOfPenaltyBox(currentPlayer().name());
            return;
        }

        if (isInPenaltyBox()) {
            isGettingOutOfPenaltyBox = true;
            notifier.gettingOutOfPenaltyBox(currentPlayer().name());
        }

        move(number);
        notifier.newLocation(currentPlayer().name(), currentPlayer().place());

        String question = currentQuestion().pop();

        notifier.newQuestion(currentQuestionCategory(), question);

	}

    private Player currentPlayer() {
        return players.currentPlayer();
    }

    private boolean notGetoutOfPenaltyBox(int roll) {
        return roll % 2 == 0;
    }

    private boolean isInPenaltyBox() {
        return currentPlayer().isInPenaltyBox();
    }

    private void move(int step) {
        currentPlayer().move(step);
    }

	private String currentQuestionCategory() {
        return currentQuestion().category();
	}

    private QuestionQueue currentQuestion(){
        return questions.get(currentPlayer().place() % questions.size());
    }

	public boolean wasCorrectlyAnswered() {
		if (stayInPenaltyBox()){
            nextPlayer();
            return true;
        }

        return correctlyAnswerAndAddCoin();
	}

    private boolean correctlyAnswerAndAddCoin() {
        increaseCoin();

        notifier.correctAndCurrentCoins(currentPlayer().name(), currentPlayer().purse());

        boolean hasWinner = didPlayerWin();
        nextPlayer();

        return hasWinner;
    }

    private void increaseCoin() {
        currentPlayer().winOneCoin();
    }

    private void nextPlayer() {
        players.next();
    }

    private boolean stayInPenaltyBox() {
        return isInPenaltyBox() && !isGettingOutOfPenaltyBox;
    }

	private boolean didPlayerWin() {
		return currentPlayer().isWin();
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
