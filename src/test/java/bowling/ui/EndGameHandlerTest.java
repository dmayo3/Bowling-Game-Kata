package bowling.ui;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import bowling.Player;
import bowling.scores.ScoreTable;
import bowling.testutil.StdoutReader;


public class EndGameHandlerTest {
    
    private StdoutReader stdout;
    
    private ScoreTable scoreTable;
    private EndGameHandler handler;
    private Player rob;
    private Player natalie;
    
    @Before
    public void setUp() throws IOException {
        stdout = new StdoutReader();
        
        scoreTable = mock(ScoreTable.class);
        handler = new EndGameHandler();
        rob = new Player("Rob");
        natalie = new Player("Natalie");
    }
    
    @Test
    public void announcesWinner() throws Exception {
        // given
        given(scoreTable.getWinner()).willReturn(rob);
        // when
        handler.gameOver(scoreTable);
        // then
        assertThat(stdout.readLine()).isEqualTo("Game Over!");
        assertThat(stdout.readLine()).isEqualTo("Rob won!");
    }
    
    @Test
    public void printsScoresForEachPlayer() throws Exception {
        // given
        Map<Player, Integer> playerScores = newLinkedHashMap();
        playerScores.put(natalie, 6);
        playerScores.put(rob, 5);
        
        given(scoreTable.getWinner()).willReturn(natalie);
        given(scoreTable.getPlayerScores()).willReturn(playerScores);
        
        // when
        handler.gameOver(scoreTable);
        
        // then
        assertThat(stdout.readLines(4))
                .contains("Natalie got 6 points")
                .contains("Rob got 5 points");
    }
    
    @Test
    public void printsCombinedTeamScore() throws Exception {
        // given
        given(scoreTable.getWinner()).willReturn(natalie);
        given(scoreTable.getTeamScore()).willReturn(11);
        
        // when
        handler.gameOver(scoreTable);
        
        // then
        assertThat(stdout.readLines(3)).contains("The team got 11 points");
    }
    
}
