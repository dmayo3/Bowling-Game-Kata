package bowling.endtoend;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import bowling.BowlingGame;

public class GameRunner {
    
    private final BowlingGame game;
    private final ExecutorService executor;
    
    private Future<?> future;
    
    public GameRunner() {
        game = new BowlingGame();
        
        executor = Executors.newSingleThreadExecutor();
    }
    
    public void start() {
        Callable<?> runGameTask = new Callable<Object>() {
            public Object call() throws Exception {
                game.play();
                return null;
            }
        };
        
        future = executor.submit(runGameTask);
    }
    
    public void stop() throws Exception {
        future.get(1, TimeUnit.SECONDS);
    }
    
}
