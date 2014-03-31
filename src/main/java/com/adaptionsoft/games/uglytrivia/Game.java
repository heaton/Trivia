package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.exception.MissRollException;

public class Game {

    private PlayerList players = new PlayerList();

    private QuestionStorage questions = new QuestionStorage();

    private Notifier notifier = new Notifier();

    private Term currentTerm;

    public Game() {
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
        newTerm(number);

        currentTerm.checkRollAndAskQuestion(questions);
    }

    private void nextPlayer() {
        players.next();
    }

    private void newTerm(int number) {
        nextPlayer();
        currentTerm = new Term(players.currentPlayer(), number, notifier);
        currentTerm.notifier();
    }

    public boolean correctlyAnswer() {
        checkRollBeforeAnswer();
        currentTerm.correctAnswer();
        return currentTerm.isEndGame();
    }

    private void checkRollBeforeAnswer() {
        if (currentTerm == null) {
            throw new MissRollException("Must roll before answer!");
        }
    }

    public boolean wrongAnswer() {
        checkRollBeforeAnswer();
        currentTerm.wrongAnswer();
        return currentTerm.isEndGame();
    }

}
