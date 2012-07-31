package bowling.testutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class StdoutReader {
    
    private final BufferedReader bufferedReader;
    
    public StdoutReader() throws IOException {
        bufferedReader = captureStdout();
    }
    
    public String readLines(int numberOfLines) throws Exception {
        StringBuilder lines = new StringBuilder();
        for (int i = 0; i < numberOfLines; i++) {
            lines.append(readLine() + System.lineSeparator());
        }
        return lines.toString();
    }
    
    public String readLine() throws Exception {
        // Attempt to read a line of output
        for (int i = 0; i < 5; i++) {
            if (bufferedReader.ready()) {
                break;
            } else {
                Thread.sleep(10);
            }
        }
        
        String line;
        
        if (bufferedReader.ready()) {
            line = bufferedReader.readLine();
        } else {
            line = "ERROR: Could not read a line from stdout within a reasonable time frame";
        }
        
        return line;
    }
    
    private BufferedReader captureStdout() throws IOException {
        PipedOutputStream outputStream = new PipedOutputStream();
        System.setOut(new PrintStream(outputStream));
        // Pipe the output stream into an input stream that we can read from
        PipedInputStream outputToInputStream = new PipedInputStream(outputStream);
        return new BufferedReader(new InputStreamReader(outputToInputStream));
    }
    
}
