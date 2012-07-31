package bowling.scores;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import bowling.Ball;
import bowling.Frame;
import bowling.Player;

public class ScoreTableTest {
    
    private Frame frame1;
    private Frame frame2;
    private Frame frame3;
    private FrameScores frame1Scores;
    private FrameScores frame2Scores;
    private FrameScores frame3Scores;
    private BonusCalculator bonusCalculator;
    private ScoreTable scores;
    private Player maria;
    private Player george;
    
    @Before
    public void setUp() {
        frame1 = mock(Frame.class);
        frame2 = mock(Frame.class);
        frame3 = mock(Frame.class);
        given(frame1.getNumber()).willReturn(1);
        given(frame2.getNumber()).willReturn(2);
        given(frame3.getNumber()).willReturn(3);
        
        frame1Scores = mock(FrameScores.class, "Frame 1 Scores");
        frame2Scores = mock(FrameScores.class, "Frame 2 Scores");
        frame3Scores = mock(FrameScores.class, "Frame 3 Scores");
        // These are ignored
        FrameScores otherFrameScores = mock(FrameScores.class);
        
        FrameScoresFactory mockFrameScoresFactory = mock(FrameScoresFactory.class);
        
        given(mockFrameScoresFactory.newInstance())
                .willReturn(frame1Scores, frame2Scores, frame3Scores, otherFrameScores);
        
        bonusCalculator = mock(BonusCalculator.class);
        
        scores = new ScoreTable(mockFrameScoresFactory, bonusCalculator);
        
        maria = new Player("Maria");
        george = new Player("George");
    }
    
    @Test
    public void canRecordPerFrameScoreForEachPlayer() throws Exception {
        // when
        scores.playerScored(frame1, maria, Ball.ONE, 6);
        scores.playerScored(frame2, george, Ball.TWO, 4);
        
        // then
        verify(frame1Scores).addScore(maria, Ball.ONE, 6);
        verify(frame2Scores).addScore(george, Ball.TWO, 4);
    }
    
    @Test
    public void canCalculatePerFrameScoreForEachPlayer() throws Exception {
        // given
        given(frame1Scores.getTotalScore(maria)).willReturn(12);
        given(frame2Scores.getTotalScore(maria)).willReturn(0);
        given(frame1Scores.getTotalScore(george)).willReturn(3);
        given(frame2Scores.getTotalScore(george)).willReturn(9);
        
        // then
        assertThat(scores.getFrameScore(1, maria)).isEqualTo(12);
        assertThat(scores.getFrameScore(2, maria)).isEqualTo(0);
        assertThat(scores.getFrameScore(1, george)).isEqualTo(3);
        assertThat(scores.getFrameScore(2, george)).isEqualTo(9);
    }
    
    @Test
    public void getPlayerScoresAddsUpScoresForEachFrame() {
        // given
        Map<Player, Integer> allPlayerScoresForFrame1 = newHashMap();
        Map<Player, Integer> allPlayerScoresForFrame2 = newHashMap();
        
        allPlayerScoresForFrame1.put(maria, 5);
        allPlayerScoresForFrame2.put(maria, 0);
        allPlayerScoresForFrame1.put(george, 4);
        allPlayerScoresForFrame2.put(george, 2);
        
        given(frame1Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame1);
        given(frame2Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame2);
        
        // then
        Map<Player, Integer> expectedPlayerScores = newLinkedHashMap();
        expectedPlayerScores.put(maria, 5);
        expectedPlayerScores.put(george, 6);
        
        assertThat(scores.getPlayerScores()).isEqualTo(expectedPlayerScores);
    }
    
    @Test
    public void theWinnerIsThePlayerWithTheMostScore() {
        // given
        Map<Player, Integer> allPlayerScoresForFrame1 = newHashMap();
        Map<Player, Integer> allPlayerScoresForFrame2 = newHashMap();
        
        allPlayerScoresForFrame1.put(maria, 5);
        allPlayerScoresForFrame2.put(maria, 0);
        allPlayerScoresForFrame1.put(george, 4);
        allPlayerScoresForFrame2.put(george, 2);
        
        given(frame1Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame1);
        given(frame2Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame2);
        
        // then
        assertThat(scores.getWinner()).isEqualTo(george);
    }
    
    @Test
    public void getTeamScoreTotalsEachPlayersScore() throws Exception {
        // given
        Map<Player, Integer> allPlayerScoresForFrame1 = newHashMap();
        Map<Player, Integer> allPlayerScoresForFrame2 = newHashMap();
        
        allPlayerScoresForFrame1.put(maria, 5);
        allPlayerScoresForFrame2.put(maria, 0);
        allPlayerScoresForFrame1.put(george, 4);
        allPlayerScoresForFrame2.put(george, 2);
        
        given(frame1Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame1);
        given(frame2Scores.getAllPlayerScores()).willReturn(allPlayerScoresForFrame2);
        
        // then
        assertThat(scores.getTeamScore()).isEqualTo(11);
    }
    
    @Test
    public void addsBonusPointsToPreviousFrameIfApplicable() {
        // given
        scores.playerScored(frame1, george, Ball.ONE, 10);
        scores.playerScored(frame2, george, Ball.ONE, 3);
        
        // Not yet!
        verify(bonusCalculator, never()).addBonusPointsIfApplicable(george, frame1Scores, frame2Scores);
        
        // when
        scores.playerScored(frame2, george, Ball.TWO, 2);
        
        // then
        verify(bonusCalculator).addBonusPointsIfApplicable(george, frame1Scores, frame2Scores);
    }
    
    @Test
    public void addsBonusPointsToFrameBeforeLastIfApplicable() {
        // given
        scores.playerScored(frame1, george, Ball.ONE, 10);
        scores.playerScored(frame2, george, Ball.ONE, 10);
        
        // Not yet!
        verify(bonusCalculator, never()).addBonusPointsIfApplicable(george, frame1Scores, frame2Scores, frame3Scores);
        
        // when
        scores.playerScored(frame3, george, Ball.ONE, 3);
        
        // then
        verify(bonusCalculator).addBonusPointsIfApplicable(george, frame1Scores, frame2Scores, frame3Scores);
    }
    
}
