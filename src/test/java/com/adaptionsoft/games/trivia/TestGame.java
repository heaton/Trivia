package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.mock.TextConsole;
import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.exception.MissRollException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestGame {

    private Game game;
    private TextConsole console;

    @Before
    public void setUp() throws Exception {
        console = new TextConsole();
        System.setOut(console);
        game = new Game();
    }

    @Test
    public void testAdd() {
        addPlayerAndVerify("Petter", 1);
        addPlayerAndVerify("Harry", 2);
    }

    private void addPlayerAndVerify(String name, int index) {
        game.addPlayer(name);
        assertEquals(index, game.howManyPlayers());
        verifyOutputAndClean(name + " was added", "They are player number " + index);
    }

    private void verifyOutputAndClean(String... expecteds) {
        List<String> expected = expectedShow(expecteds);
        assertEquals(expected, console.lines());
        console.clear();
    }

    private List<String> expectedShow(String... results) {
        return Arrays.asList(results);
    }

    @Test
    public void roll_and_verify_categories() {
        addPetter();
        console.clear();

        rollAndVerify(1, 1, "Science", 0);
        rollAndVerify(1, 2, "Sports", 0);
        rollAndVerify(1, 3, "Rock", 0);
        rollAndVerify(1, 4, "Pop", 0);
        rollAndVerify(2, 6, "Sports", 1);
        rollAndVerify(1, 7, "Rock", 1);
        rollAndVerify(1, 8, "Pop", 1);
        rollAndVerify(1, 9, "Science", 1);
        rollAndVerify(1, 10, "Sports", 2);
        rollAndVerify(1, 11, "Rock", 2);
        rollAndVerify(1, 0, "Pop", 2);
        rollAndVerify(5, 5, "Science", 2);

    }

    private void rollAndVerify(int rollNum, int location, String category, int questionIndex){
        game.roll(rollNum);
        verifyOutputAndClean("Petter is the current player", "They have rolled a " + rollNum,
                "Petter's new location is " + location, "The category is " + category,
                category + " Question " + questionIndex);
    }

    @Test
    public void roll_and_verify_out_of_penalty_box() {
        onePlayerWithOneWrongAndCleanConsole();

        rollAndVerifyOutOfPenaltyBox(3, 3, "Rock", 0);
        rollAndVerifyOutOfPenaltyBox(11, 2, "Sports", 0);
    }

    private void onePlayerWithOneWrongAndCleanConsole() {
        addPetter();
        oneWrongTerm();
        console.clear();
    }

    private void addPetter() {
        game.addPlayer("Petter");
    }

    private boolean oneWrongTerm() {
        rollAndCleanConsole(0);
        return game.wrongAnswer();
    }

    private void rollAndCleanConsole(int number) {
        game.roll(number);
        console.clear();
    }

    private void rollAndVerifyOutOfPenaltyBox(int rollNum, int location, String category, int questionIndex){
        game.roll(rollNum);
        verifyOutputAndClean("Petter is the current player", "They have rolled a " + rollNum,
                "Petter is getting out of the penalty box",
                "Petter's new location is " + location, "The category is " + category,
                category + " Question " + questionIndex);
    }

    @Test
    public void roll_and_verify_not_out_of_penalty_box() {
        onePlayerWithOneWrongAndCleanConsole();

        game.roll(4);
        verifyOutputAndClean("Petter is the current player", "They have rolled a 4",
                "Petter is not getting out of the penalty box");
    }

    @Test
    public void wrong_answer_than_send_to_penalty_box() {
        addPetter();
        addHarry();

        wrongAndVerify("Petter");
        wrongAndVerify("Harry");
        wrongAndVerify("Petter");

    }

    private void wrongAndVerify(String currentPlayer) {
        rollAndCleanConsole(1);
        boolean noWinner = game.wrongAnswer();
        assertTrue(noWinner);
        verifyOutputAndClean("Question was incorrectly answered", currentPlayer + " was sent to the penalty box");
    }

    @Test
    public void correct_and_go_on() {
        addPetter();
        addHarry();
        console.clear();

        correctAndVerify("Petter", 1);
        correctAndVerify("Harry", 1);
        correctAndVerify("Petter", 2);
    }

    private void addHarry() {
        game.addPlayer("Harry");
    }

    private void correctAndVerify(String currentPlayer, int coins) {
        oneCorrectTerm();
        verifyOutputAndClean("Answer was correct!!!!", currentPlayer + " now has " + coins + " Gold Coins.");
    }

    private boolean oneCorrectTerm() {
        rollAndCleanConsole(0);
        return game.correctlyAnswer();
    }

    @Test
    public void correct_until_win() {
        addPetter();

        boolean noWinner;
        for(int i=0; i<5; i++) {
            noWinner = oneCorrectTerm();
            assertTrue(noWinner);
        }
        noWinner = oneCorrectTerm();
        assertFalse(noWinner);
    }

    @Test
    public void correct_in_penalty_box_then_nothing_happened() {
        addPetter();
        addHarry();
        oneWrongTerm(); // Petter
        oneCorrectTerm(); // Harry

        rollAndCleanConsole(2);
        correctInPenaltyBoxAndVerify();

        correctAndVerify("Harry", 2);
    }

    private void correctInPenaltyBoxAndVerify() {
        boolean noWinner = oneCorrectTerm();
        assertTrue(noWinner);
        verifyNothingOutput();
    }

    private void verifyNothingOutput() {
        verifyOutputAndClean();
    }

    @Test
    public void correct_in_penalty_box_and_out() {
        addPetter();
        oneWrongTerm();

        rollAndCleanConsole(1);
        correctOutPenaltyboxAndVerify("Petter", 1);

        rollAndCleanConsole(3);
        correctOutPenaltyboxAndVerify("Petter", 2);

    }

    private void correctOutPenaltyboxAndVerify(String currentPlayer, int coins) {
        boolean noWinner = game.correctlyAnswer();
        assertTrue(noWinner);
        verifyOutputAndClean("Answer was correct!!!!", currentPlayer + " now has " + coins + " Gold Coins.");
    }

    @Test
    public void one_player_is_not_playable() {
        addMorePlayers(1);
        boolean playable = game.isPlayable();
        assertFalse(playable);
    }

    private void addMorePlayers(int number) {
        for (int i = 0; i < number; i++) {
            game.addPlayer("P" + i);
        }
    }

    @Test
    public void two_player_is_playable() {
        addMorePlayers(2);
        boolean playable = game.isPlayable();
        assertTrue(playable);
    }

    @Test
    public void players_limit_rather_than_5() {
        addMorePlayers(6);
        int playersCount = game.howManyPlayers();
        assertEquals(6, playersCount);
    }

    @Test
    public void wrong_answer_stay_in_penalty_box_should_no_message() {
        addPetter();
        oneWrongTerm();

        rollAndCleanConsole(2);
        game.wrongAnswer();

        verifyNothingOutput();
    }

    @Test(expected = MissRollException.class)
    public void must_roll_before_answer() {
        addPetter();
        game.correctlyAnswer();
    }

}
