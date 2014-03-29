package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;

public class Game {

    private PlayerList players = new PlayerList();

    private QuestionStorage questions = new QuestionStorage();

    private boolean isGettingOutOfPenaltyBox;
    
    public Game(){
        questions.init();
    }

	public boolean isPlayable() {
        return howManyPlayers() >= 2;
    }

	public boolean addPlayer(String playerName) {

        Player player = new Player(playerName);
        players.add(player);

        sendMessage(player.name() + " was added");
        sendMessage("They are player number " + howManyPlayers());
        return true;
    }

    private void sendMessage(String message){
        System.out.println(message);
    }

    public int howManyPlayers() {
		return players.count();
	}

	public void roll(int roll) {
		sendMessage(currentPlayer().name() + " is the current player");
		sendMessage("They have rolled a " + roll);
		
		if (isInPenaltyBox() && notGetoutOfPenaltyBox(roll)) {
            isGettingOutOfPenaltyBox = false;
            sendMessage(currentPlayer().name() + " is not getting out of the penalty box");
            return;
        }

        if (isInPenaltyBox()) {
            isGettingOutOfPenaltyBox = true;
            sendMessage(currentPlayer().name() + " is getting out of the penalty box");
        }

        move(roll);
        sendMessage(currentPlayer().name()
                + "'s new location is "
                + currentPlayer().place());
        sendMessage("The category is " + currentQuestionCategory());

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
        sendMessage("Answer was correct!!!!");
        increaseCoin();
        sendMessage(currentPlayer().name()
                + " now has "
                + currentPlayer().purse()
                + " Gold Coins.");

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
        sendMessage("Question was incorrectly answered");
        sendMessage(currentPlayer().name() + " was sent to the penalty box");
        currentPlayer().goIntoPenaltyBox();

        nextPlayer();
        return true;
    }

}
