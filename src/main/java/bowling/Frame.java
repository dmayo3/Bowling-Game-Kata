package bowling;

import java.util.List;

import bowling.ui.ScoreCollector;

public class Frame {
    
    private final int frameNumber;
    private final List<Player> players;
    
    protected final ScoreCollector scoreCollector;
    
    public Frame(int frameNumber, List<Player> players, ScoreCollector scoreCollector) {
        this.frameNumber = frameNumber;
        this.players = players;
        this.scoreCollector = scoreCollector;
    }
    
    public int getNumber() {
        return frameNumber;
    }
    
    public void play() {
        for (Player player : players) {
            scoreCollector.announcePlayersTurn(player);
            
            takePlayersTurn(player);
        }
    }
    
    protected void takePlayersTurn(Player player) {
        int score = scoreCollector.takeScore(this, player, Ball.ONE);
        
        int remainingPins = 10 - score;
        
        if (remainingPins > 0) {
            scoreCollector.takeScore(this, player, Ball.TWO, remainingPins);
        }
    }
    
    @Override
    public String toString() {
        return "Frame [" + frameNumber + "]";
    }
    
}
