package bowling.endtoend;

import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bowling.testutil.StdinWriter;
import bowling.testutil.StdoutReader;

public class EndToEndTest {
    
    private StdinWriter gameInput;
    private GameRunner game;
    private StdoutReader gameOutput;
    
    @Before
    public void setUpGame() throws IOException {
        gameInput = new StdinWriter();
        game = new GameRunner();
        gameOutput = new StdoutReader();
    }
    
    @After
    public void stopGame() throws Exception {
        game.stop();
    }
    
    @Test
    public void canPlayBowlingGame() throws Exception {
        game.start();
        
        assertThat(gameOutput.readLine()).isEqualTo("How many players?");
        
        gameInput.sendLine("2");
        
        assertThat(gameOutput.readLine()).isEqualTo("Enter name for Player 1:");
        gameInput.sendLine("Sarah");
        
        assertThat(gameOutput.readLine()).isEqualTo("Enter name for Player 2:");
        gameInput.sendLine("Bob");
        
        for (int frame = 1; frame <= 10; frame++) {
            assertThat(gameOutput.readLine()).isEqualTo("Sarah's turn!");
            enterFrameScores(frame, 5, 3);
            
            assertThat(gameOutput.readLine()).isEqualTo("Bob's turn!");
            enterFrameScores(frame, 0, 6);
        }
        
        assertThat(gameOutput.readLine()).isEqualTo("Game Over!");
        assertThat(gameOutput.readLine()).isEqualTo(format("Sarah won!"));
        
        int expectedPlayer1Score = (5 + 3) * 10;
        int expectedPlayer2Score = 6 * 10;
        int expectedTeamScore = expectedPlayer1Score + expectedPlayer2Score;
        
        assertThat(gameOutput.readLines(3))
                .contains(format("Sarah got %d points", expectedPlayer1Score))
                .contains(format("Bob got %d points", expectedPlayer2Score))
                .contains(format("The team got %d points", expectedTeamScore));
    }
    
    @Test
    public void perfectGameTest() throws Exception {
        game.start();
        
        gameInput.sendLine("1");
        gameInput.sendLine("Bob");
        // Ignore
        gameOutput.readLines(2);
        
        for (int frame = 1; frame <= 10; frame++) {
            gameInput.sendLine("10");
            
            assertThat(gameOutput.readLines(2)).doesNotContain("Game Over!");
        }
        
        gameInput.sendLine("10");
        gameInput.sendLine("10");
        
        assertThat(gameOutput.readLines(2))
                .contains("Enter score for Frame 10, Ball 2:")
                .contains("Enter score for Frame 10, Ball 3:");
        
        assertThat(gameOutput.readLines(3))
                .contains("Game Over!")
                .contains("Bob got 300 points");
    }
    
    private void enterFrameScores(int frame, int ball1, int ball2) throws Exception {
        assertThat(gameOutput.readLine()).contains(format("Enter score for Frame %d, Ball 1:", frame));
        gameInput.sendLine(Integer.toString(ball1));
        
        assertThat(gameOutput.readLine()).contains(format("Enter score for Frame %d, Ball 2:", frame));
        gameInput.sendLine(Integer.toString(ball2));
    }
    
}
