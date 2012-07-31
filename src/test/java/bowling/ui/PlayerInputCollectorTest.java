package bowling.ui;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bowling.Player;
import bowling.testutil.StdinWriter;
import bowling.testutil.StdoutReader;


public class PlayerInputCollectorTest {
    
    private StdinWriter stdin;
    private StdoutReader stdout;
    
    private PlayerInputCollector collector;
    
    @Before
    public void setUp() throws IOException {
        stdin = new StdinWriter();
        stdout = new StdoutReader();
        collector = new PlayerInputCollector();
    }
    
    @Test
    public void retrievesThePlayerNamesFromInput() throws Exception {
        // when
        stdin.sendLine("3");
        stdin.sendLine("Adam");
        stdin.sendLine("Jane");
        stdin.sendLine("Carol");
        List<Player> players = collector.getPlayers();
        // then
        assertThat(stdout.readLine()).isEqualTo("How many players?");
        
        assertThat(stdout.readLines(3))
                .contains("Enter name for Player 1:")
                .contains("Enter name for Player 2:")
                .contains("Enter name for Player 3:");
        
        assertThat(players).onProperty("name").containsExactly("Adam", "Jane", "Carol");
    }
    
    @Test
    public void rejectsInvalidNumberOfPlayers() throws Exception {
        // when
        stdin.sendLine("-1");
        stdin.sendLine("0");
        stdin.sendLine("7");
        stdin.sendLine("Not A Number");
        
        // Valid
        stdin.sendLine("1");
        stdin.sendLine("Adam");
        
        collector.getPlayers();
        
        // then
        assertThat(stdout.readLines(5))
                .contains("Invalid number of players, must be between 1 and 6. Please try again:");
    }
    
    @Test
    public void rejectsInvalidPlayerNames() throws Exception {
        // when
        stdin.sendLine("1");
        stdin.sendLine("No Spaces Allowed");
        stdin.sendLine("12345");
        stdin.sendLine("s@%&ggh");
        // Valid
        stdin.sendLine("Jessica");
        
        collector.getPlayers();
        
        // then
        assertThat(stdout.readLines(5))
                .contains("Invalid player name, only upper and lower case letters are allowed [a-z]. Please try again:");
    }
    
    @Test
    public void rejectsNonUniquePlayerNames() throws Exception {
        // when
        stdin.sendLine("2");
        stdin.sendLine("Jim");
        stdin.sendLine("Jim");
        stdin.sendLine("Alice");
        
        collector.getPlayers();
        
        // then
        assertThat(stdout.readLines(4))
                .contains("Name is already taken. Please enter a unique name:");
    }
    
}
