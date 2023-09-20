package main.java.com.shawarmaland.snakegame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame gamePanel = new SnakeGame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(630, 650);
        frame.setResizable(false);
        frame.add(gamePanel);
        frame.setVisible(true);
    }
}
