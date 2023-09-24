package main.java.com.shawarmaland.snakegame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630, 650);
        frame.setResizable(false);

        SnakeGame gamePanel = new SnakeGame();

        StartMenu startMenu = new StartMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.add(gamePanel);
                gamePanel.initializeGame();
                gamePanel.requestFocusInWindow();
                frame.revalidate();
                frame.repaint();
            }
        });

        frame.add(startMenu);
        frame.setVisible(true);
    }
}
