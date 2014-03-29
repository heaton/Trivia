package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    public static final int QUESTION_AMOUNT = 4;
    public static final String POP = "Pop";
    public static final String SCIENCE = "Science";
    public static final String SPORTS = "Sports";
    public static final String ROCK = "Rock";

    private List<Player> players = new ArrayList<Player>();

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayerIndex = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
    	}
    }

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}
	
	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean addPlayer(String playerName) {

        Player player = new Player(playerName);
        players.add(player);

        System.out.println(player.name() + " was added");
        System.out.println("They are player number " + howManyPlayers());
        return true;
    }

    public int howManyPlayers() {
		return players.size();
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
        return players.get(currentPlayerIndex);
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
		if (currentQuestionCategory() == POP)
			System.out.println(popQuestions.removeFirst());
		if (currentQuestionCategory() == SCIENCE)
			System.out.println(scienceQuestions.removeFirst());
		if (currentQuestionCategory() == SPORTS)
			System.out.println(sportsQuestions.removeFirst());
		if (currentQuestionCategory() == ROCK)
			System.out.println(rockQuestions.removeFirst());		
	}
	
	
	private String currentQuestionCategory() {
		if (currentPlayer().place() % QUESTION_AMOUNT == 0) return POP;
		if (currentPlayer().place() % QUESTION_AMOUNT == 1) return SCIENCE;
		if (currentPlayer().place() % QUESTION_AMOUNT == 2) return SPORTS;
		return ROCK;
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
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
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
		return !(currentPlayer().purse() == 6);
	}
}
