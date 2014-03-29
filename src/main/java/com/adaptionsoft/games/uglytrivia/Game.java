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

        System.out.println(player.name() + " was added");
        System.out.println("They are player number " + howManyPlayers());
        return true;
    }

    public int howManyPlayers() {
		return players.count();
	}

	public void roll(int roll) {
		System.out.println(currentPlayer().name() + " is the current player");
		System.out.println("They have rolled a " + roll);
		
		if (isInPenaltyBox() && notGetoutOfPenaltyBox(roll)) {
            isGettingOutOfPenaltyBox = false;
            System.out.println(currentPlayer().name() + " is not getting out of the penalty box");
            return;
        }

        if (isInPenaltyBox()) {
            isGettingOutOfPenaltyBox = true;
            System.out.println(currentPlayer().name() + " is getting out of the penalty box");
        }

        move(roll);
        System.out.println(currentPlayer().name()
                + "'s new location is "
                + currentPlayer().place());
        System.out.println("The category is " + currentQuestionCategory());

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
        System.out.println(currentQuestion().pop());
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
        System.out.println("Answer was correct!!!!");
        increaseCoin();
        System.out.println(currentPlayer().name()
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

    public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(currentPlayer().name() + " was sent to the penalty box");
        currentPlayer().goIntoPenaltyBox();

        nextPlayer();
		return true;
	}

	private boolean didPlayerWin() {
		return currentPlayer().isWin();
	}

}
