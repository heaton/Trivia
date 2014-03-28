package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    public static final int QUESTION_AMOUNT = 4;
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    
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

	public boolean add(String playerName) {
		
		
	    players.add(playerName);
	    places[howManyPlayers()] = 0;
	    purses[howManyPlayers()] = 0;
	    inPenaltyBox[howManyPlayers()] = false;
	    
	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
		return true;
	}
	
	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayerIndex) + " is the current player");
		System.out.println("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayerIndex]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				
				System.out.println(players.get(currentPlayerIndex) + " is getting out of the penalty box");
				places[currentPlayerIndex] = places[currentPlayerIndex] + roll;
				if (places[currentPlayerIndex] > 11) places[currentPlayerIndex] = places[currentPlayerIndex] - 12;
				
				System.out.println(players.get(currentPlayerIndex)
						+ "'s new location is " 
						+ places[currentPlayerIndex]);
				System.out.println("The category is " + currentCategory());
				askQuestion();
			} else {
				System.out.println(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {
		
			places[currentPlayerIndex] = places[currentPlayerIndex] + roll;
			if (places[currentPlayerIndex] > 11) places[currentPlayerIndex] = places[currentPlayerIndex] - 12;
			
			System.out.println(players.get(currentPlayerIndex)
					+ "'s new location is " 
					+ places[currentPlayerIndex]);
			System.out.println("The category is " + currentCategory());
			askQuestion();
		}
		
	}

	private void askQuestion() {
		if (currentCategory() == "Pop")
			System.out.println(popQuestions.removeFirst());
		if (currentCategory() == "Science")
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory() == "Sports")
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory() == "Rock")
			System.out.println(rockQuestions.removeFirst());		
	}
	
	
	private String currentCategory() {
		if (places[currentPlayerIndex] % QUESTION_AMOUNT == 0) return "Pop";
		if (places[currentPlayerIndex] % QUESTION_AMOUNT == 1) return "Science";
		if (places[currentPlayerIndex] % QUESTION_AMOUNT == 2) return "Sports";
		return "Rock";
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
        System.out.println(players.get(currentPlayerIndex)
                + " now has "
                + purses[currentPlayerIndex]
                + " Gold Coins.");

        boolean winner = didPlayerWin();
        nextPlayer();

        return winner;
    }

    private void increaseCoin() {
        purses[currentPlayerIndex]++;
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
    }

    private boolean stayInPenaltyBox() {
        return inPenaltyBox[currentPlayerIndex] && !isGettingOutOfPenaltyBox;
    }

    public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayerIndex)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayerIndex] = true;

        nextPlayer();
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayerIndex] == 6);
	}
}
