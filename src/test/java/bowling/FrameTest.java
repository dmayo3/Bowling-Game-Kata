package bowling;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.InOrder;

import bowling.ui.ScoreCollector;


public class FrameTest {
    
    public static final int STRIKE = 10;
    
    private final Player player1 = new Player("John");
    private final Player player2 = new Player("Laura");
    
    private final ScoreCollector scoreCollector = mock(ScoreCollector.class);
    
    @Test
    public void announcesBeginningOfEachPlayersTurnBeforeRecordingScores() {
        // given
        final Frame frame = new Frame(1, newArrayList(player1, player2), scoreCollector);
        // when
        frame.play();
        // then
        InOrder inOrder = inOrder(scoreCollector);
        inOrder.verify(scoreCollector).announcePlayersTurn(player1);
        inOrder.verify(scoreCollector, atLeastOnce()).takeScore(any(Frame.class), eq(player1), any(Ball.class));
        
        inOrder.verify(scoreCollector).announcePlayersTurn(player2);
        inOrder.verify(scoreCollector, atLeastOnce()).takeScore(any(Frame.class), eq(player2), any(Ball.class));
    }
    
    @Test
    public void scoresAreCollectedWhenPlayerBowlsTwoBalls() {
        // given
        final Frame frame = new Frame(1, singletonList(player1), scoreCollector);
        // when
        frame.play();
        // then
        InOrder inOrder = inOrder(scoreCollector);
        inOrder.verify(scoreCollector).takeScore(eq(frame), eq(player1), eq(Ball.ONE));
        inOrder.verify(scoreCollector).takeScore(eq(frame), eq(player1), eq(Ball.TWO), anyInt());
    }
    
    @Test
    public void notifiesScoreCollectorOfTheRemainingBowlingPins() {
        // given
        final Frame frame = new Frame(1, singletonList(player1), scoreCollector);
        
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), any(Ball.class)))
                .willReturn(6);
        
        // when
        frame.play();
        
        // then
        int remainingPins = 4;
        
        InOrder inOrder = inOrder(scoreCollector);
        inOrder.verify(scoreCollector).takeScore(any(Frame.class), any(Player.class), eq(Ball.ONE));
        inOrder.verify(scoreCollector).takeScore(any(Frame.class), any(Player.class), eq(Ball.TWO), eq(remainingPins));
    }
    
    @Test
    public void onlyOneBallIsThrownWhenPlayerGetsAStrikeOnTheFirstBall() {
        // given
        final Frame frame = new Frame(1, singletonList(player1), scoreCollector);
        
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), any(Ball.class)))
                .willReturn(STRIKE);
        
        // when
        frame.play();
        
        // then
        verify(scoreCollector).takeScore(eq(frame), eq(player1), eq(Ball.ONE));
        
        verify(scoreCollector, never()).takeScore(eq(frame), eq(player1), eq(Ball.TWO));
    }
    
    @Test
    public void threeBallsArePlayedInTheFinalFrameIfPlayerGetsAStrike() {
        // given
        final Frame finalFrame = new FinalFrame(singletonList(player1), scoreCollector);
        
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), eq(Ball.ONE)))
                .willReturn(STRIKE);
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), eq(Ball.TWO)))
                .willReturn(3);
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), eq(Ball.THREE), anyInt()))
                .willReturn(2);
        
        // when
        finalFrame.play();
        
        // then
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.ONE));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.TWO));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.THREE), eq(7));
    }
    
    @Test
    public void threeBallsArePlayedInTheFinalFrameIfPlayerGetsThreeStrikes() {
        // given
        final Frame finalFrame = new FinalFrame(singletonList(player1), scoreCollector);
        
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), any(Ball.class)))
                .willReturn(STRIKE);
        
        // when
        finalFrame.play();
        
        // then
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.ONE));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.TWO));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.THREE));
    }
    
    @Test
    public void threeBallsArePlayedInTheFinalFrameIfPlayerGetsASpare() {
        // given
        final Frame finalFrame = new FinalFrame(singletonList(player1), scoreCollector);
        
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), eq(Ball.ONE)))
                .willReturn(7);
        given(scoreCollector.takeScore(any(Frame.class), any(Player.class), eq(Ball.TWO), anyInt()))
                .willReturn(3);
        
        // when
        finalFrame.play();
        
        // then
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.ONE));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.TWO), eq(3));
        verify(scoreCollector).takeScore(eq(finalFrame), eq(player1), eq(Ball.THREE));
    }
    
}
