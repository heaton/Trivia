package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;

public class Term {

    private Player player;
    private int rollNumber;
    private boolean endTerm = false;

    private Notifier notifier;

    public Term(Player player, int number, Notifier notifier) {
        this.player = player;
        this.rollNumber = number;
        this.notifier = notifier;
    }

    public void notifier(){
        notifier.rolled(player.name(), rollNumber);
    }

    public void checkRollAndAskQuestion(QuestionStorage questions) {
        if (stayInPenaltyBox()) {
            notOutOfPenaltyBox();
            return;
        }

        outPenaltyBoxIfNeed();

        move(rollNumber);
        askQuestion(currentQuestion(questions));
    }

    private void notOutOfPenaltyBox() {
        notifier.notGettingOutOfPenaltyBox(player.name());
    }

    private void outPenaltyBoxIfNeed() {
        if (player.isInPenaltyBox()) {
            notifier.gettingOutOfPenaltyBox(player.name());
        }
    }

    private boolean stayInPenaltyBox() {
        return player.isInPenaltyBox() && notGetoutOfPenaltyBox();
    }

    private boolean notGetoutOfPenaltyBox() {
        return rollNumber % 2 == 0;
    }

    private void move(int number) {
        player.move(number);
        notifier.newLocation(player.name(), player.place());
    }

    private void askQuestion(QuestionQueue questionQueue) {
        String question = questionQueue.pop();
        notifier.newQuestion(questionQueue.category(), question);
    }

    private QuestionQueue currentQuestion(QuestionStorage questions){
        int questionIndex = player.place() % questions.size();
        return questions.get(questionIndex);
    }

    public void correctAnswer() {
        if (stayInPenaltyBox()){
            return;
        }
        awardOneCoin();
    }

    private void awardOneCoin() {
        player.winOneCoin();
        notifier.correctAndCurrentCoins(player.name(), player.purse());

        endTerm = player.isWin();
    }

    public void wrongAnswer() {
        if(stayInPenaltyBox()){
            return;
        }
        sendToPenaltyBox();
    }

    private void sendToPenaltyBox() {
        player.goIntoPenaltyBox();
        notifier.incorrectAndSendToPenaltyBox(player.name());
    }

    public boolean isEndGame() {
        return endTerm;
    }

}
