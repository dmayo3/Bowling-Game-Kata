package bowling.testutil;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

public class StdinWriter {
    
    private final PrintWriter inputWriter;
    
    public StdinWriter() throws IOException {
        // Simulate command line input
        PipedInputStream inputStream = new PipedInputStream();
        inputWriter = new PrintWriter(new PipedOutputStream(inputStream));
        
        System.setIn(inputStream);
    }
    
    public void sendLine(String input) throws InterruptedException {
        inputWriter.append(input + "\n");
        inputWriter.flush();
    }
    
}
