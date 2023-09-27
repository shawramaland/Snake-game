package main.java.com.shawarmaland.snakegame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;

public class SnakeGame extends JPanel {
    private final int cellSize = 30;
    private final int gridWidth = 20;
    private final int gridHeight = 20;

    private int currentLevel = 1;
    private int pointsToNextLeve = 100;

    private int score = 0;
    private int timerDelay = 100;
    private Timer timer;
    private Cell food;
    private String direction = "RIGHT";

    private final LinkedList<Cell> snake = new LinkedList<>();

    public SnakeGame() {
        // Initialize the snake with one cell on the center
        snake.add(new Cell(gridWidth / 2 , gridHeight / 2));
        spawnFood();

        // Adds a key listener
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if(!direction.equals("DOWN")) direction = "UP";
                        break;
                    case KeyEvent.VK_DOWN:
                        if(!direction.equals("UP")) direction = "DOWN";
                        break;
                    case KeyEvent.VK_LEFT:
                        if(!direction.equals("RIGHT")) direction = "LEFT";
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(!direction.equals("LEFT")) direction = "RIGHT";
                        break;
                }
            }
        });
        this.setFocusable(true);
    }

    public void initializeGame() {
        startGame();
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);
        } while (snake.contains(new Cell(x, y)));
        food = new Cell(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draws the snake
        g.setColor(Color.GREEN);
        for(Cell cell : snake) {
            g.fillRect(cell.getX() * cellSize, cell.getY() * cellSize,cellSize, cellSize);
        }

        // Draws the food
        g.setColor(Color.RED);
        g.fillRect(food.getX() * cellSize, food.getY() * cellSize, cellSize, cellSize);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 15);
        g.drawString("Level: " + currentLevel, 200, 15);
    }

    private void moveSnake() {
        Cell head = snake.getFirst();
        Cell newHead = new Cell(head.getX(), head.getY());

        switch(direction) {
            case "UP":
                newHead.setY(newHead.getY() - 1);
                break;
            case "DOWN":
                newHead.setY(newHead.getY() + 1);
                break;
            case "LEFT":
                newHead.setX(newHead.getX() - 1);
                break;
            case "RIGHT":
                newHead.setX(newHead.getX() + 1);
                break;
        }

        // Check for collisions with itself
        for(int i = 0; i < snake.size() - 1; i++) {
            if(snake.get(i).equals(newHead)) {
                gameOver("Game Over: You ran into yourself!");
                return;
            }
        }

        // Checking for collisions with the wall
        if(newHead.getX() < 0 || newHead.getX() >= gridWidth || newHead.getY() < 0 || newHead.getY() >= gridHeight) {
            gameOver("Game Over: You hit the wall!");
            return;
        }

        // Displays the Score
        if(newHead.equals(food)) {
            spawnFood();
            snake.addFirst(newHead);
            score += 10;
        }

        // Increase speed based on score
        // if(score != 0 && score % 50 == 0) {// every 50 points
        //    timerDelay = Math.max(timerDelay - 10, 50);
        //    timer.setDelay(timerDelay);
        //}
        if(score >= pointsToNextLeve * currentLevel) {
            currentLevel++;
            switch(currentLevel) {
                case 2:
                    timerDelay = 90;
                    break;
                case 3:
                    timerDelay = 80;
                    break;
                case 4:
                    timerDelay = 70;
                    break;
                case 5:
                    timerDelay = 60;
                    break;
                case 6:
                    timerDelay = 50;
                    break;
            }
            timer.setDelay(timerDelay);
        }

        // checking if the snake has eaten the food
        if(newHead.equals(food)) {
            spawnFood();
            snake.addFirst(newHead);
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }

        // TODO: Handle collision and eating food
    }

    private void gameOver(String reason) {
        timer.stop();
        HighScoreManager hsm = new HighScoreManager();

        // Save the score
        hsm.saveScore("PlayerName", score);

        List<HighScoreManager.Score> topScores = hsm.getTopScores(5);
        // TODO: display the scores or provide UI feedback
        System.out.println("Game Over!" + reason);
        System.out.println("Top score is: ");
        for(HighScoreManager.Score score : topScores) {
            System.out.println(score.getPlayerName() + " : " + score.getScoreValue());
        }
    }

    public void startGame() {
        timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSnake();
                repaint();
            }
        });
        timer.start();
    }
}
