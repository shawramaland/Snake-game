package main.java.com.shawarmaland.snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JPanel {
    public StartMenu(ActionListener startGameListener) {
        setLayout(new GridLayout(3, 1));

        JButton startButton = new JButton("Start Game");
        JButton highScoresButton = new JButton("High Scores");
        JButton exitButton = new JButton("Exit");

        startButton.addActionListener(startGameListener);

        highScoresButton.addActionListener(e -> {
            //TODO: Show high scores
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(startButton);
        add(highScoresButton);
        add(exitButton);
    }
}
