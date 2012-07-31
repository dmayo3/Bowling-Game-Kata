package bowling;

import java.util.List;

import bowling.ui.ScoreCollector;

public class FinalFrame extends Frame {
    
    public FinalFrame(List<Player> players, ScoreCollector scoreCollector) {
        super(10, players, scoreCollector);
    }
    
    @Override
    protected void takePlayersTurn(Player player) {
        int score = scoreCollector.takeScore(this, player, Ball.ONE);
        
        int remainingPins = 10 - score;
        
        if (remainingPins == 0) {
            playExtraTwoBallsForAStrike(player);
        } else {
            playAnExtraBallIfPlayerGetsASpare(player, remainingPins);
        }
    }
    
    private void playAnExtraBallIfPlayerGetsASpare(Player player, int remainingPins) {
        int score = scoreCollector.takeScore(this, player, Ball.TWO, remainingPins);
        
        boolean spare = (remainingPins - score == 0);
        
        if (spare) {
            scoreCollector.takeScore(this, player, Ball.THREE);
        }
    }
    
    private void playExtraTwoBallsForAStrike(Player player) {
        int score = scoreCollector.takeScore(this, player, Ball.TWO);
        
        int remainingPins = 10 - score;
        
        if (remainingPins == 0) {
            scoreCollector.takeScore(this, player, Ball.THREE);
        } else {
            scoreCollector.takeScore(this, player, Ball.THREE, remainingPins);
        }
    }
    
}
