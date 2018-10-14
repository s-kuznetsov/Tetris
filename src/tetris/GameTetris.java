package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTetris extends JFrame {
    private static final String TITLE_OF_PROGRAM = "Tetris";
    private static final int START_LOCATION_X = 500;
    private static final int START_LOCATION_Y = 50;
    private static final int FIELD_DX = 7;
    private static final int FIELD_DY = 26;
    private static final int SHOW_DELAY = 400;
    private static final int[] SCORES = {100, 300, 700, 1500};
    private static final int FIELD_HEIGHT = Constants.FIELD_HEIGHT;
    private static final int FIELD_WIDTH = Constants.FIELD_WIDTH;
    private static final int CELL_SIZE = Constants.CELL_SIZE;
    private int gameScore = 0;
    private int[][] matrix = new int[FIELD_HEIGHT + 1][FIELD_WIDTH];
    private boolean gameOver = false;
    private Figure figure;
    private Field field;
    private List<Observer> subscribers = new ArrayList<Observer>();

    GameTetris() {
        figure = new Figure();
        field = new Field();
        subscribers.add(figure);
        subscribers.add(field);
        notifyObservers();

        setTitle(TITLE_OF_PROGRAM);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(START_LOCATION_X, START_LOCATION_Y, FIELD_WIDTH * CELL_SIZE + FIELD_DX,
                FIELD_HEIGHT * CELL_SIZE + FIELD_DY);
        setResizable(false);
        field.setBackground(Color.DARK_GRAY);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {figure.drop();}
                    if (e.getKeyCode() == KeyEvent.VK_UP) {figure.rotate();}
                    if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {figure.move(e.getKeyCode()); }
                }
                field.repaint();
            }
        });
        add(BorderLayout.CENTER, field);
        setVisible(true);
        Arrays.fill(matrix[FIELD_HEIGHT], 1);
    }

    public void startGame() {
        while (!gameOver) {
            try {
                Thread.sleep(SHOW_DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.repaint();
            checkFilling();
            if (figure.isTouchGround()) {
                figure.leaveOnTheGround();
                int index = subscribers.indexOf(figure);
                figure = new Figure();

                subscribers.set(index, figure);
                notifyObservers();
                gameOver = figure.isCrossGround();
                notifyObservers();
            } else {
                figure.stepDown();
            }
        }
    }

    private void checkFilling() {
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++)
                filled *= Integer.signum(matrix[row][col]);
            if (filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) System.arraycopy(matrix[i-1], 0, matrix[i], 0, FIELD_WIDTH);
            } else
                row--;
        }
        if (countFillRows > 0) {
            gameScore += SCORES[countFillRows - 1];
            setTitle(TITLE_OF_PROGRAM + " : " + gameScore);
        }
    }

    private void notifyObservers() {
        for (Observer observer : subscribers) {
            observer.updateMatrix(matrix);
            observer.updateFigure(figure);
            observer.updateStatusGame(gameOver);
        }
    }
}
