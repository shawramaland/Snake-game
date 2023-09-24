package main.java.com.shawarmaland.snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverMenu extends JPanel {
    public GameOverMenu(ActionListener restartGameListener, ActionListener mainMenuListener) {
        setLayout(new GridLayout(2, 1));

        JButton restartButton = new JButton("Restart");
        JButton mainMenuButton = new JButton("Main Menu");

        restartButton.addActionListener(restartGameListener);
        mainMenuButton.addActionListener(mainMenuListener);

        add(restartButton);
        add(mainMenuButton);
    }
}
