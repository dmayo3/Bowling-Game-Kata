package bowling.scores;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Map.Entry;

import bowling.Ball;
import bowling.Player;

public class FrameScores {
    
    private final Map<Player, ScoreInfo> playerScores = newHashMap();
    
    public void addScore(Player player, Ball ball, int pins) {
        ScoreInfo scoreInfo = playerScores.get(player);
        
        if (scoreInfo == null) {
            scoreInfo = new ScoreInfo();
            playerScores.put(player, scoreInfo);
        }
        
        scoreInfo.addScore(ball, pins);
    }
    
    public void addBonus(Player player, int score) {
        playerScores.get(player).addBonus(score);
    }
    
    public int getScore(Player player, Ball ball) {
        return playerScores.get(player).getScore(ball);
    }
    
    public int getTotalScore(Player player) {
        return playerScores.get(player).getTotalScore();
    }
    
    public Map<Player, Integer> getAllPlayerScores() {
        Map<Player, Integer> allPlayerScores = newHashMap();
        for (Entry<Player, ScoreInfo> playerScoreInfo : playerScores.entrySet()) {
            allPlayerScores.put(playerScoreInfo.getKey(), playerScoreInfo.getValue().getTotalScore());
        }
        return allPlayerScores;
    }
    
    private static class ScoreInfo {
        
        private final Map<Ball, Integer> ballScores = newHashMap();
        private int bonus;
        
        public void addScore(Ball ball, int pins) {
            ballScores.put(ball, pins);
        }
        
        public void addBonus(int score) {
            bonus += score;
        }
        
        public int getScore(Ball ball) {
            Integer score = ballScores.get(ball);
            return score == null ? 0 : score;
        }
        
        public int getTotalScore() {
            int total = bonus;
            for (int score : ballScores.values()) {
                total += score;
            }
            return total;
        }
        
    }
    
}
