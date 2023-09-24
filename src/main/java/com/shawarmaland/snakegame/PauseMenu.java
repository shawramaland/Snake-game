package main.java.com.shawarmaland.snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PauseMenu extends JPanel {
    public PauseMenu(ActionListener resumeGamerListener, ActionListener mainMenuListener) {
        setLayout(new GridLayout(3, 1));

        JButton resumeButton = new JButton("Resume");
        JButton restartButton = new JButton("Restart");
        JButton mainMenuButton = new JButton("Main Menu");

        resumeButton.addActionListener(resumeGamerListener);
        restartButton.addActionListener(e -> {
            // TODO: Restart the game
        });
        mainMenuButton.addActionListener(mainMenuListener);

        add(resumeButton);
        add(restartButton);
        add(mainMenuButton);
    }
}
