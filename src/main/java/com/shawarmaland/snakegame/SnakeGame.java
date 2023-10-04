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
    private final int pointsToNextLevel = 100;

    private final Random random = new Random();

    private int score = 0;
    private int timerDelay = 100;
    private Timer timer;
    private Cell food;
    private String direction = "RIGHT";

    private boolean canShoot = false;
    private Cell shootingPowerUp;

    private Cell bulletVisual;

    private final LinkedList<Cell> snake = new LinkedList<>();

    private final LinkedList<Wall> walls = new LinkedList<>();

    public SnakeGame() {
        // Initialize the snake with one cell on the center
        snake.add(new Cell(gridWidth / 2 , gridHeight / 2));
        // Spawning food
        spawnFood();
        // generating Walls
        generateWalls();
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
                    case KeyEvent.VK_SPACE:
                        if(canShoot) {
                            shoot();
                        }
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
        int x, y;
        do {
            x = random.nextInt(gridWidth);
            y = random.nextInt(gridHeight);
        } while (snake.contains(new Cell(x, y)) || walls.contains(new Wall(x, y)));
        food = new Cell(x, y);

        if(random.nextInt(10) == 0) {
            do {
                x = random.nextInt(gridWidth);
                y = random.nextInt(gridHeight);
            } while (snake.contains(new Cell(x, y)) || walls.contains(new Wall(x, y)) || new Cell(x, y).equals(food));
            shootingPowerUp = new Cell(x, y);
        }
    }

    private void shoot() {
        int xIncrement = 0, yIncrement = 0;
        switch(direction) {
            case "UP":
                yIncrement = -1;
                break;
            case "DOWN":
                yIncrement = 1;
                break;
            case "LEFT":
                xIncrement = -1;
                break;
            case "RIGHT":
                xIncrement = 1;
                break;
        }

        Cell head = snake.getFirst();
        bulletVisual = new Cell(head.getX() + xIncrement, head.getY() + yIncrement);
        System.out.println("Bullet initial position: x=" + bulletVisual.getX() + ", y=" + bulletVisual.getY());

        while (true) {
            if(walls.contains(new Wall(bulletVisual.getX(), bulletVisual.getY()))) {
                System.out.println("Bullet hit a wall at position x= " + bulletVisual.getX() + ", y=" + bulletVisual.getY());
                walls.remove(new Wall(bulletVisual.getX(), bulletVisual.getY()));
                break;
            }
            bulletVisual.setX(bulletVisual.getX() + xIncrement);
            bulletVisual.setY(bulletVisual.getY() + yIncrement);

            // If bullet goes out of the grid, break the loop

            if(bulletVisual.getX() < 0 || bulletVisual.getX() >= gridWidth || bulletVisual.getY() < 0 || bulletVisual.getY() >= gridHeight) {
                break;
            }
        }
    }

    private void generateWalls() {
        int wallCount = switch (currentLevel) {
            case 1 -> 0;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 8;
            default -> 10;
        };

        for(int i = 0; i < wallCount; i++) {
            int x, y;
            do {
                x = random.nextInt(gridWidth);
                y = random.nextInt(gridHeight);
            } while (snake.contains(new Cell(x, y)) || walls.contains(new Wall(x, y)) || food.equals(new Cell(x,y)));

            walls.add(new Wall(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Draws the snake
        g.setColor(Color.GREEN);
        for(Cell cell : snake) {
            g.setColor(canShoot ? Color.CYAN : Color.GREEN);
            g.fillRect(cell.getX() * cellSize, cell.getY() * cellSize,cellSize, cellSize);
        }

        // Draws the food
        g.setColor(Color.RED);
        g.fillRect(food.getX() * cellSize, food.getY() * cellSize, cellSize, cellSize);

        // Draws the Walls
        g.setColor(Color.GRAY);
        for(Wall wall : walls) {
            g.fillRect(wall.getX() * cellSize, wall.getY() * cellSize, cellSize, cellSize);
        }

        if(bulletVisual != null) {
            g.setColor(Color.YELLOW);
            g.fillRect(bulletVisual.getX() * cellSize, bulletVisual.getY() * cellSize, cellSize, cellSize);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 15);
        g.drawString("Level: " + currentLevel, 200, 15);

        if (shootingPowerUp != null) {
            g.setColor(Color.BLUE);
            g.fillRect(shootingPowerUp.getX() * cellSize, shootingPowerUp.getY() * cellSize, cellSize, cellSize);
        }
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

        // Check for collisions with walls
        for(Wall wall : walls) {
            if(newHead.equals(new Cell(wall.getX(), wall.getY()))) {
                gameOver("Gave over: You hit a wall segment!");
                return;
            }
        }

        if(newHead.equals(shootingPowerUp)) {
            canShoot = true;
            // Set a timer for the shooting duration, e.g., 10 seconds
            new Timer(10000, e -> canShoot = false).start();
            shootingPowerUp = null;
        }

        // Portal through walls
        if (newHead.getX() < 0) {
            newHead.setX(gridWidth - 1);
        }
        if (newHead.getX() >= gridWidth) {
            newHead.setX(0);
        }
        if (newHead.getY() < 0) {
            newHead.setY(gridHeight - 1);
        }
        if (newHead.getY() >= gridHeight)
            newHead.setY(0);

        // Displays the Score
        if(newHead.equals(food)) {
            spawnFood();
            snake.addFirst(newHead);
            score += 10;
        }

        if(score >= pointsToNextLevel * currentLevel) {
            currentLevel++;
            // Existing switch statement for timerDelay...
            walls.clear();
            generateWalls();
        }

        // Increase speed based on score
        //    timerDelay = Math.max(timerDelay - 10, 50);
        //    timer.setDelay(timerDelay);
        //}
        if(score >= pointsToNextLevel * currentLevel) {
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

    private void resetGame() {
        snake.clear();
        snake.add(new Cell(gridWidth / 2, gridHeight / 2));
        direction = "RIGHT";
        score = 0;
        currentLevel = 1;
        timerDelay = 100;
        spawnFood();
        walls.clear();
        generateWalls();
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
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you want to restart the game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION
        );

        if(choice == JOptionPane.YES_OPTION) {
            resetGame();
            startGame();
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
