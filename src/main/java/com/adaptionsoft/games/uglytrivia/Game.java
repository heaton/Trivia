package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;

public class Game {

    private PlayerList players = new PlayerList();

    private QuestionStorage questions = new QuestionStorage();

    private boolean isGettingOutOfPenaltyBox;

    private Messages messages;

    public Game(){
        questions.init();
        messages = Messages.getDefault();
    }

	public boolean addPlayer(String playerName) {

        Player player = new Player(playerName);
        players.add(player);

        sendMessage(messages.wasAdded(playerName));
        sendMessage(messages.playerNumber(howManyPlayers()));
        return true;
    }

    private void sendMessage(String message){
        System.out.println(message);
    }

    public boolean isPlayable() {
        return howManyPlayers() >= 2;
    }

    public int howManyPlayers() {
		return players.count();
	}

	public void roll(int number) {
		sendMessage(messages.currentPlayer(currentPlayer().name()));
		sendMessage(messages.haveRolled(number));
		
		if (isInPenaltyBox() && notGetoutOfPenaltyBox(number)) {
            isGettingOutOfPenaltyBox = false;
            sendMessage(messages.notGettingOutOfPenaltyBox(currentPlayer().name()));
            return;
        }

        if (isInPenaltyBox()) {
            isGettingOutOfPenaltyBox = true;
            sendMessage(messages.gettingOutOfPenaltyBox(currentPlayer().name()));
        }

        move(number);
        sendMessage(messages.newLocation(currentPlayer().name(), currentPlayer().place()));
        sendMessage(messages.categoryIs(currentQuestionCategory()));

        askQuestion();

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

    private void askQuestion() {
        sendMessage(currentQuestion().pop());
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
        sendMessage(messages.correctAnswer());
        increaseCoin();
        sendMessage(messages.currentCoins(currentPlayer().name(), currentPlayer().purse()));

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

    public boolean wrongAnswer(){
        sendMessage(messages.incorrectAnswer());
        sendMessage(messages.sendToPenaltyBox(currentPlayer().name()));
        currentPlayer().goIntoPenaltyBox();

        nextPlayer();
        return true;
    }

}
