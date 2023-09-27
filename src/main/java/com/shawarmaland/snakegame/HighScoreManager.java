package main.java.com.shawarmaland.snakegame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "highscores.dat";

    // An inner class to represent a single high score entry.
    public static class Score implements Serializable {
        private static final long serialVersionUID = 1L;
        private String playerName;
        private int scoreValue;

        public Score(String playerName, int scoreValue) {
            this.playerName = playerName;
            this.scoreValue = scoreValue;
        }
        public String getPlayerName() {
            return playerName;
        }

        public int getScoreValue() {
            return scoreValue;
        }
    }

    public void saveScore(String playerName, int score) {
        List<Score> highScores = getScores();
        highScores.add(new Score(playerName, score));
        updateScoreFile(highScores);
    }

    public List<Score> getTopScores(int n) {
        List<Score> highScores = getScores();
        Collections.sort(highScores, Comparator.comparingInt(s -> -s.scoreValue));
        return highScores.subList(0, Math.min(n, highScores.size()));
    }

    private void updateScoreFile(List<Score> scores) {
        try (FileOutputStream fos = new FileOutputStream(HIGHSCORE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Score> getScores() {
        List<Score> highScores;
        try(FileInputStream fis = new FileInputStream(HIGHSCORE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            highScores = (List<Score>)ois.readObject();
        } catch (Exception e) {
            highScores = new ArrayList<>();
        }
        return highScores;
    }
}
