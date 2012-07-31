package bowling.scores;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import bowling.Ball;
import bowling.Frame;
import bowling.Player;

import com.google.common.collect.Ordering;

public class ScoreTable implements ScoreListener {
    
    private final Map<Integer, FrameScores> scoresPerFrame;
    private final BonusCalculator bonusCalculator;
    
    public ScoreTable(FrameScoresFactory factory, BonusCalculator bonusCalculator) {
        this.bonusCalculator = bonusCalculator;
        
        scoresPerFrame = newHashMap();
        
        for (int frame = 1; frame <= 10; frame++) {
            scoresPerFrame.put(frame, factory.newInstance());
        }
    }
    
    public void playerScored(Frame frame, Player player, Ball ball, int pins) {
        int frameNumber = frame.getNumber();
        FrameScores currentFrameScores = scoresPerFrame.get(frameNumber);
        
        currentFrameScores.addScore(player, ball, pins);
        
        if (frameNumber > 1 && ball == Ball.TWO) {
            FrameScores previousFrame = scoresPerFrame.get(frameNumber - 1);
            bonusCalculator.addBonusPointsIfApplicable(player, previousFrame, currentFrameScores);
        }
        if (frameNumber > 2 && ball == Ball.ONE) {
            FrameScores twoFramesAgo = scoresPerFrame.get(frameNumber - 2);
            FrameScores previousFrame = scoresPerFrame.get(frameNumber - 1);
            bonusCalculator.addBonusPointsIfApplicable(player, twoFramesAgo, previousFrame, currentFrameScores);
        }
    }
    
    public Player getWinner() {
        Entry<Player, Integer> highestPlayerScoreEntry =
                Ordering.from(SCORE_COMPARATOR).max(getPlayerScores().entrySet());
        
        return highestPlayerScoreEntry.getKey();
    }
    
    public Map<Player, Integer> getPlayerScores() {
        Map<Player, Integer> scores = newHashMap();
        
        for (FrameScores frameScores : scoresPerFrame.values()) {
            Map<Player, Integer> allPlayerScoresForFrame = frameScores.getAllPlayerScores();
            
            if (scores.isEmpty()) {
                scores.putAll(allPlayerScoresForFrame);
            } else {
                addPlayerScores(scores, allPlayerScoresForFrame);
            }
            
        }
        
        return scores;
    }
    
    public int getFrameScore(int frameNumber, Player player) {
        return scoresPerFrame.get(frameNumber).getTotalScore(player);
    }
    
    public int getTeamScore() {
        int total = 0;
        for (Integer score : getPlayerScores().values()) {
            total += score;
        }
        return total;
    }
    
    private void addPlayerScores(Map<Player, Integer> total, Map<Player, Integer> allPlayerScoresForAFrame) {
        for (Entry<Player, Integer> playerScoreForFrame : allPlayerScoresForAFrame.entrySet()) {
            int totalScore = total.get(playerScoreForFrame.getKey());
            totalScore += playerScoreForFrame.getValue();
            total.put(playerScoreForFrame.getKey(), totalScore);
        }
    }
    
    private static final Comparator<Entry<Player, Integer>> SCORE_COMPARATOR = new Comparator<Entry<Player, Integer>>() {
        
        public int compare(Entry<Player, Integer> player1Score, Entry<Player, Integer> player2Score) {
            return Integer.compare(player1Score.getValue(), player2Score.getValue());
        }
        
    };
    
}
