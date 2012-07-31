package bowling.ui;

import static java.lang.String.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bowling.Player;

public class PlayerInputCollector {
    
    private final BufferedReader reader;
    
    public PlayerInputCollector() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public List<Player> getPlayers() throws Exception {
        System.out.println("How many players?");
        
        int numberOfPlayers = getValidNumberOfPlayers();
        
        List<Player> players = new ArrayList<Player>();
        
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(format("Enter name for Player %d:", i + 1));
            String playerName = getUniquePlayerName(players);
            players.add(new Player(playerName));
        }
        
        return players;
    }
    
    private String getUniquePlayerName(List<Player> existingPlayers) throws IOException {
        String uniquePlayerName = null;
        
        while (uniquePlayerName == null) {
            String playerName = getValidPlayerName();
            
            if (isNameUnique(existingPlayers, playerName)) {
                uniquePlayerName = playerName;
            } else {
                System.out.println("Name is already taken. Please enter a unique name:");
            }
        }
        
        return uniquePlayerName;
    }
    
    private boolean isNameUnique(List<Player> existingPlayers, String playerName) {
        boolean unique = true;
        
        for (Player existingPlayer : existingPlayers) {
            if (existingPlayer.getName().equals(playerName)) {
                unique = false;
            }
        }
        
        return unique;
    }
    
    private String getValidPlayerName() throws IOException {
        String playerName = null;
        
        while (playerName == null) {
            String string = reader.readLine().trim();
            if (string.matches("^[A-Za-z]+$")) {
                playerName = string;
            } else {
                System.out.println("Invalid player name, only upper and lower case letters are allowed [a-z]. "
                        + "Please try again:");
            }
        }
        
        return playerName;
    }
    
    private int getValidNumberOfPlayers() throws IOException {
        Integer numberOfPlayers = null;
        
        while (numberOfPlayers == null) {
            int number = getNumber();
            if (number >= 1 && number <= 6) {
                numberOfPlayers = number;
            } else {
                System.out.println("Invalid number of players, must be between 1 and 6. Please try again:");
            }
        }
        
        return numberOfPlayers;
    }
    
    private int getNumber() throws IOException {
        int number;
        try {
            number = Integer.parseInt(reader.readLine().trim());
        } catch (Exception e) {
            number = -1;
        }
        return number;
    }
    
}
