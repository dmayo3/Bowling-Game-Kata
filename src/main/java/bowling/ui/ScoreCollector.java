package bowling.ui;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import bowling.Ball;
import bowling.Frame;
import bowling.Player;
import bowling.scores.ScoreListener;


public class ScoreCollector {
    
    private final BufferedReader inputReader;
    private final List<ScoreListener> listeners;
    
    public ScoreCollector() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        listeners = newArrayList();
    }
    
    public void addScoreListener(ScoreListener listener) {
        listeners.add(listener);
    }
    
    public void announcePlayersTurn(Player player) {
        System.out.println(format("%s's turn!", player.getName()));
    }
    
    public int takeScore(Frame frame, Player player, Ball ball) {
        return takeScore(frame, player, ball, 10);
    }
    
    public int takeScore(Frame frame, Player player, Ball ball, int remainingPins) {
        System.out.println(format("Enter score for Frame %d, Ball %d:", frame.getNumber(), ball.getNumber()));
        
        int pins = getNumberOfPinsScored(remainingPins);
        
        for (ScoreListener listener : listeners) {
            listener.playerScored(frame, player, ball, pins);
        }
        
        return pins;
    }
    
    private int getNumberOfPinsScored(int remainingPins) {
        Integer pins = null;
        while (pins == null) {
            int numberOfPins = getNumber();
            
            if (numberOfPins >= 0 && numberOfPins <= remainingPins) {
                pins = numberOfPins;
            } else {
                System.out.println(format("Not a valid number of pins, "
                        + "please enter a number between 1 and %d:", remainingPins));
            }
        }
        return pins;
    }
    
    private int getNumber() {
        Integer number;
        try {
            number = Integer.parseInt(inputReader.readLine().trim());
        } catch (Exception e) {
            number = -1;
        }
        return number;
    }
    
}
