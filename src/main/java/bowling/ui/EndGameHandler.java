package bowling.ui;

import static java.lang.String.format;

import java.util.Map.Entry;

import bowling.Player;
import bowling.scores.ScoreTable;


public class EndGameHandler {
    
    public void gameOver(ScoreTable scoreTable) {
        System.out.println("Game Over!");
        System.out.println(format("%s won!", scoreTable.getWinner().getName()));
        
        for (Entry<Player, Integer> playerScore : scoreTable.getPlayerScores().entrySet()) {
            Player player = playerScore.getKey();
            Integer points = playerScore.getValue();
            
            System.out.println(format("%s got %d points", player.getName(), points));
        }
        
        System.out.println(format("The team got %d points", scoreTable.getTeamScore()));
    }
    
}
