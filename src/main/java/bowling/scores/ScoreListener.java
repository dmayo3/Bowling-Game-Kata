package bowling.scores;

import bowling.Ball;
import bowling.Frame;
import bowling.Player;

public interface ScoreListener {
    
    void playerScored(Frame frame, Player player, Ball ball, int pins);
    
}
