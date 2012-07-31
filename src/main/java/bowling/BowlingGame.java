package bowling;

import java.util.ArrayList;
import java.util.List;

import bowling.scores.BonusCalculator;
import bowling.scores.FrameScoresFactory;
import bowling.scores.ScoreTable;
import bowling.ui.EndGameHandler;
import bowling.ui.PlayerInputCollector;
import bowling.ui.ScoreCollector;


public class BowlingGame {
    
    public static void main(String[] args) throws Exception {
        new BowlingGame().play();
    }
    
    private final PlayerInputCollector playerInputCollector;
    private final ScoreCollector scoreCollector;
    private final EndGameHandler endGameHandler;
    
    public BowlingGame() {
        playerInputCollector = new PlayerInputCollector();
        scoreCollector = new ScoreCollector();
        endGameHandler = new EndGameHandler();
    }
    
    public void play() throws Exception {
        ScoreTable scoreTable = new ScoreTable(new FrameScoresFactory(), new BonusCalculator());
        List<Player> players = playerInputCollector.getPlayers();
        List<Frame> frames = createFrames(players);
        scoreCollector.addScoreListener(scoreTable);
        
        for (Frame frame : frames) {
            frame.play();
        }
        
        endGameHandler.gameOver(scoreTable);
    }
    
    private List<Frame> createFrames(List<Player> players) {
        List<Frame> frames = new ArrayList<Frame>();
        
        for (int frame = 1; frame <= 9; frame++) {
            frames.add(new Frame(frame, players, scoreCollector));
        }
        
        frames.add(new FinalFrame(players, scoreCollector));
        
        return frames;
    }
    
}
