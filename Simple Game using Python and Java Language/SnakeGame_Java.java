import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SnakeGame extends JPanel implements KeyListener {

    private final int GAME_WIDTH = 600;
    private final int GAME_HEIGHT = 600;
    private final int SPEED = 110;
    private final int SPACE_SIZE = 50;
    private final int BODY_PARTS = 3;
    private final Color SNAKE_COLOR = Color.WHITE;
    private final Color FOOD_COLOR = Color.BLUE;
    private final Color BACKGROUND_COLOR = Color.BLACK;

    private int score = 0;
    private String direction = "down";

    private ArrayList<Point> snake;
    private Point food;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setFocusable(true);
        this.addKeyListener(this);

        snake = new ArrayList<>();
        for (int i = 0; i < BODY_PARTS; i++) {
            snake.add(new Point(0, 0));
        }

        createFood();
        new Thread(this::gameLoop).start();
    }

    private void createFood() {
        int x = (int) (Math.random() * (GAME_WIDTH / SPACE_SIZE)) * SPACE_SIZE;
        int y = (int) (Math.random() * (GAME_HEIGHT / SPACE_SIZE)) * SPACE_SIZE;
        food = new Point(x, y);
    }

    private void nextTurn() {
        Point head = snake.get(0);
        int x = (int) head.getX();
        int y = (int) head.getY();

        if (direction.equals("up")) {
            y -= SPACE_SIZE;
        } else if (direction.equals("down")) {
            y += SPACE_SIZE;
        } else if (direction.equals("left")) {
            x -= SPACE_SIZE;
        } else if (direction.equals("right")) {
            x += SPACE_SIZE;
        }

        Point newHead = new Point(x, y);
        snake.add(0, newHead);

        if (x == food.getX() && y == food.getY()) {
            score++;
            createFood();
        } else {
            snake.remove(snake.size() - 1);
        }

        if (checkCollision()) {
            gameOver();
        }

        repaint();
    }

    private boolean checkCollision() {
        Point head = snake.get(0);

        if (head.getX() < 0 || head.getX() >= GAME_WIDTH || head.getY() < 0 || head.getY() >= GAME_HEIGHT) {
            return true;
        }

        for (int i = 1; i < snake.size(); i++) {
            Point bodyPart = snake.get(i);
            if (head.equals(bodyPart)) {
                return true;
            }
        }

        return false;
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(FOOD_COLOR);
        g.fillRect((int) food.getX(), (int) food.getY(), SPACE_SIZE, SPACE_SIZE);

        g.setColor(SNAKE_COLOR);
        for (Point point : snake) {
            g.fillRect((int) point.getX(), (int) point.getY(), SPACE_SIZE, SPACE_SIZE);
        }
    }

    private void gameLoop() {
        while (true) {
            nextTurn();
            try {
                Thread.sleep(SPEED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && !direction.equals("right")) {
            direction = "left";
        } else if (keyCode == KeyEvent.VK_RIGHT && !direction.equals("left")) {
            direction = "right";
        } else if (keyCode == KeyEvent.VK_UP && !direction.equals("down")) {
            direction = "up";
        } else if (keyCode == KeyEvent.VK_DOWN && !direction.equals("up")) {
            direction = "down";
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new SnakeGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
