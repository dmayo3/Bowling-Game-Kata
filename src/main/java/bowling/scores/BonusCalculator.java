package bowling.scores;

import bowling.Ball;
import bowling.Player;

public class BonusCalculator {
    
    public void addBonusPointsIfApplicable(Player player, FrameScores previousFrame, FrameScores currentFrame) {
        
        if (playerGotStrike(player, previousFrame)) {
            int bonus = currentFrame.getScore(player, Ball.ONE) + currentFrame.getScore(player, Ball.TWO);
            
            previousFrame.addBonus(player, bonus);
        }
        if (playerGotSpare(player, previousFrame)) {
            int bonus = currentFrame.getScore(player, Ball.ONE);
            
            previousFrame.addBonus(player, bonus);
        }
    }
    
    public void addBonusPointsIfApplicable(Player player, FrameScores twoFramesAgo, FrameScores previousFrame,
            FrameScores currentFrame) {
        
        if (playerGotStrike(player, twoFramesAgo) && playerGotStrike(player, previousFrame)) {
            int bonus = currentFrame.getScore(player, Ball.ONE) + previousFrame.getScore(player, Ball.ONE);
            
            twoFramesAgo.addBonus(player, bonus);
        }
    }
    
    private boolean playerGotStrike(Player player, FrameScores frameScores) {
        return frameScores.getScore(player, Ball.ONE) == 10;
    }
    
    private boolean playerGotSpare(Player player, FrameScores frameScores) {
        int one = frameScores.getScore(player, Ball.ONE);
        int two = frameScores.getScore(player, Ball.TWO);
        
        return one != 10 && one + two == 10;
    }
    
}
