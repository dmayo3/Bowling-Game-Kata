package bowling.scores;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import bowling.Ball;
import bowling.Player;

public class BonusCalculatorTest {
    
    private final FrameScores twoFramesAgo = mock(FrameScores.class);
    private final FrameScores previousFrameScores = mock(FrameScores.class);
    private final FrameScores currentFrameScores = mock(FrameScores.class);
    
    private final BonusCalculator bonusCalculator = new BonusCalculator();
    
    private final Player player = new Player("Bob");
    
    @Test
    public void playerGetsBonusForNextTwoBallsWhenTheyGetAStrike() {
        // given
        given(previousFrameScores.getScore(player, Ball.ONE)).willReturn(10);
        given(currentFrameScores.getScore(player, Ball.ONE)).willReturn(2);
        given(currentFrameScores.getScore(player, Ball.TWO)).willReturn(1);
        // when
        bonusCalculator.addBonusPointsIfApplicable(player, previousFrameScores, currentFrameScores);
        // then
        verify(previousFrameScores).addBonus(player, 3);
        // Don't apply any other kind of bonus
        verify(previousFrameScores, never()).addBonus(player, 2);
        verify(currentFrameScores, never()).addBonus(any(Player.class), anyInt());
    }
    
    @Test
    public void playerGetsBonusForNextTwoBallsWhenTheyGetTwoStrikes() {
        // given
        given(twoFramesAgo.getScore(player, Ball.ONE)).willReturn(10);
        given(previousFrameScores.getScore(player, Ball.ONE)).willReturn(10);
        given(currentFrameScores.getScore(player, Ball.ONE)).willReturn(2);
        // when
        bonusCalculator.addBonusPointsIfApplicable(player, twoFramesAgo, previousFrameScores, currentFrameScores);
        // then
        verify(twoFramesAgo).addBonus(player, 12);
        // Don't apply any other kind of bonus
        verify(previousFrameScores, never()).addBonus(any(Player.class), anyInt());
        verify(currentFrameScores, never()).addBonus(any(Player.class), anyInt());
    }
    
    @Test
    public void playerGetsBonusForNextBallWhenTheyGetASpare() {
        // given
        given(previousFrameScores.getScore(player, Ball.ONE)).willReturn(5);
        given(previousFrameScores.getScore(player, Ball.TWO)).willReturn(5);
        given(currentFrameScores.getScore(player, Ball.ONE)).willReturn(2);
        given(currentFrameScores.getScore(player, Ball.TWO)).willReturn(1);
        // when
        bonusCalculator.addBonusPointsIfApplicable(player, previousFrameScores, currentFrameScores);
        // then
        verify(previousFrameScores).addBonus(player, 2);
        verify(currentFrameScores, never()).addBonus(any(Player.class), anyInt());
    }
    
    @Test
    public void playerGetsNoBonusWhenLessThanTenPinsKnockedDownInPreviousRound() {
        // given
        given(previousFrameScores.getScore(player, Ball.ONE)).willReturn(0);
        given(previousFrameScores.getScore(player, Ball.TWO)).willReturn(9);
        
        // when
        bonusCalculator.addBonusPointsIfApplicable(player, previousFrameScores, currentFrameScores);
        // then
        verify(previousFrameScores, never()).addBonus(any(Player.class), anyInt());
    }
    
    @Test
    public void playerGetsNoBonusWhenTheyDontGetStrikesInPreviousTwoRounds() {
        // given
        given(twoFramesAgo.getScore(player, Ball.ONE)).willReturn(0);
        given(previousFrameScores.getScore(player, Ball.ONE)).willReturn(0);
        given(currentFrameScores.getScore(player, Ball.ONE)).willReturn(0);
        
        // when
        bonusCalculator.addBonusPointsIfApplicable(player, previousFrameScores, currentFrameScores);
        // then
        verify(twoFramesAgo, never()).addBonus(any(Player.class), anyInt());
        verify(previousFrameScores, never()).addBonus(any(Player.class), anyInt());
    }
    
}
