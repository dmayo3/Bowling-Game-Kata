package bowling.scores;

import static com.google.common.collect.Maps.newHashMap;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import bowling.Ball;
import bowling.Player;

public class FrameScoresTest {
    
    private final FrameScores scores = new FrameScores();
    private final Player player1 = new Player("Dave");
    private final Player player2 = new Player("Alice");
    
    @Test
    public void canAddAndGetScoreForPlayer() {
        // when
        scores.addScore(player1, Ball.ONE, 10);
        scores.addScore(player1, Ball.TWO, 5);
        scores.addScore(player1, Ball.THREE, 5);
        // then
        assertThat(scores.getTotalScore(player1)).isEqualTo(10 + 5 + 5);
    }
    
    @Test
    public void canAddAndGetScoresForTwoPlayers() {
        // when
        scores.addScore(player1, Ball.ONE, 1);
        scores.addScore(player2, Ball.ONE, 2);
        // then
        assertThat(scores.getTotalScore(player1)).isEqualTo(1);
        assertThat(scores.getTotalScore(player2)).isEqualTo(2);
    }
    
    @Test
    public void canAddBonusPoints() {
        // when
        scores.addScore(player1, Ball.ONE, 10);
        scores.addScore(player2, Ball.ONE, 10);
        scores.addBonus(player1, 1);
        scores.addBonus(player2, 2);
        // then
        assertThat(scores.getTotalScore(player1)).isEqualTo(11);
        assertThat(scores.getTotalScore(player2)).isEqualTo(12);
    }
    
    @Test
    public void canGetScoresForAllPlayers() {
        // when
        scores.addScore(player1, Ball.ONE, 5);
        scores.addScore(player2, Ball.ONE, 10);
        
        // then
        Map<Player, Integer> expected = newHashMap();
        expected.put(player1, 5);
        expected.put(player2, 10);
        
        assertThat(scores.getAllPlayerScores()).isEqualTo(expected);
    }
    
    @Test
    public void canGetScoreForPlayerAndBall() {
        // when
        scores.addScore(player1, Ball.ONE, 1);
        scores.addScore(player1, Ball.TWO, 2);
        scores.addScore(player1, Ball.THREE, 3);
        // then
        assertThat(scores.getScore(player1, Ball.TWO)).isEqualTo(2);
    }
    
    @Test
    public void scoreForBallThatHasntBeenThrownIsZero() {
        // when
        scores.addScore(player1, Ball.ONE, 10);
        // then
        assertThat(scores.getScore(player1, Ball.TWO)).isEqualTo(0);
    }
    
}
