package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.trivia.mock.TextConsole;
import com.adaptionsoft.games.uglytrivia.Game;
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
        game.addPlayer("Petter");
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
        game.addPlayer("Petter");
        game.wrongAnswer();
        console.clear();

        rollAndVerifyOutOfPenaltyBox(3, 3, "Rock", 0);
        rollAndVerifyOutOfPenaltyBox(11, 2, "Sports", 0);
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
        game.addPlayer("Petter");
        game.wrongAnswer();
        console.clear();

        game.roll(4);
        verifyOutputAndClean("Petter is the current player", "They have rolled a 4",
                "Petter is not getting out of the penalty box");
    }

    @Test
    public void wrong_answer_than_send_to_penalty_box() {
        game.addPlayer("Petter");
        game.addPlayer("Harry");
        console.clear();

        wrongAndVerify("Petter");
        wrongAndVerify("Harry");
        wrongAndVerify("Petter");

    }

    private void wrongAndVerify(String currentPlayer) {
        boolean noWinner = game.wrongAnswer();
        assertTrue(noWinner);
        verifyOutputAndClean("Question was incorrectly answered", currentPlayer + " was sent to the penalty box");
    }

    @Test
    public void correct_and_go_on() {
        game.addPlayer("Petter");
        game.addPlayer("Harry");
        console.clear();

        correctAndVerify("Petter", 1);
        correctAndVerify("Harry", 1);
        correctAndVerify("Petter", 2);
    }

    private void correctAndVerify(String currentPlayer, int coins) {
        boolean noWinner = game.wasCorrectlyAnswered();
        assertTrue(noWinner);
        verifyOutputAndClean("Answer was correct!!!!", currentPlayer + " now has " + coins + " Gold Coins.");
    }

    @Test
    public void correct_until_win() {
        game.addPlayer("Petter");
        console.clear();

        boolean noWinner = true;
        for(int i=0; i<6; i++) {
            noWinner = game.wasCorrectlyAnswered();
        }
        assertFalse(noWinner);
    }

    @Test
    public void correct_in_penalty_box_then_nothing_happened() {
        game.addPlayer("Petter");
        game.addPlayer("Harry");
        game.wrongAnswer();
        game.wasCorrectlyAnswered();
        game.roll(2);
        console.clear();

        correctInPenaltyBoxAndVerify();
        correctAndVerify("Harry", 2);
    }

    private void correctInPenaltyBoxAndVerify() {
        boolean noWinner = game.wasCorrectlyAnswered();
        assertTrue(noWinner);
        verifyOutputAndClean();
    }

    @Test
    public void correct_in_penalty_box_and_out() {
        game.addPlayer("Petter");
        game.addPlayer("Harry");
        game.wrongAnswer();
        game.wrongAnswer();
        game.roll(1);
        console.clear();

        correctOutPenaltyboxAndVerify("Petter", 1);

        game.roll(2);
        console.clear();

        correctInPenaltyBoxAndVerify();

        game.wrongAnswer();
        game.roll(3);
        console.clear();

        correctOutPenaltyboxAndVerify("Harry", 1);

        game.roll(3);
        console.clear();

        correctOutPenaltyboxAndVerify("Petter", 2);
        correctOutPenaltyboxAndVerify("Harry", 2);
        correctOutPenaltyboxAndVerify("Petter", 3);

        game.roll(2);
        console.clear();

        correctInPenaltyBoxAndVerify();
    }

    private void correctOutPenaltyboxAndVerify(String currentPlayer, int coins) {
        boolean noWinner = game.wasCorrectlyAnswered();
        assertTrue(noWinner);
        verifyOutputAndClean("Answer was correct!!!!", currentPlayer + " now has " + coins + " Gold Coins.");
    }

    @Test
    public void one_player_is_not_playable() {
        game.addPlayer("P1");
        assertFalse(game.isPlayable());
    }

    @Test
    public void two_player_is_playable() {
        game.addPlayer("P1");
        game.addPlayer("P2");
        assertTrue(game.isPlayable());
    }

    @Test
    public void test_players_limit_expection_if_6() {
        game.addPlayer("P1");
        game.addPlayer("P2");
        game.addPlayer("P3");
        game.addPlayer("P4");
        game.addPlayer("P5");
        game.addPlayer("P6");
        assertEquals(6, game.howManyPlayers());
    }
}
