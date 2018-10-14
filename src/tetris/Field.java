package tetris;

import javax.swing.*;
import java.awt.*;

public class Field extends JPanel implements Observer {
    private static final int FIELD_HEIGHT = Constants.FIELD_HEIGHT;
    private static final int FIELD_WIDTH = Constants.FIELD_WIDTH;
    private static final int CELL_SIZE = Constants.CELL_SIZE;
    private static final int[][] GAME_OVER_MSG = {
            {0,1,1,0,0,0,1,1,0,0,0,1,0,1,0,0,0,1,1,0},
            {1,0,0,0,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,1},
            {1,0,1,1,0,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1},
            {1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,1,0,1,0,1,0,0,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,0,1,1,0,0,1,1,1,0,0},
            {1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0},
            {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,1,0,0},
            {1,0,0,1,0,1,1,0,0,0,1,0,0,0,0,1,0,0,1,0},
            {0,1,1,0,0,1,0,0,0,0,0,1,1,0,0,1,0,0,1,0}};
    private int[][] matrix;
    private Figure figure;
    private boolean gameOver;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < FIELD_WIDTH; x++)
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                if (x < FIELD_WIDTH - 1 && y < FIELD_HEIGHT - 1) {
                    g.setColor(Color.white);
                    g.drawLine((x+1)*CELL_SIZE, (y+1)*CELL_SIZE, (x+1)*CELL_SIZE, (y+1)*CELL_SIZE);
                    g.drawLine((x+1)*CELL_SIZE, (y+1)*CELL_SIZE, (x+1)*CELL_SIZE, (y+1)*CELL_SIZE);
                }
                if (matrix[y][x] > 0) {
                    g.setColor(new Color(matrix[y][x]));
                    g.fill3DRect(x*CELL_SIZE, y*CELL_SIZE,CELL_SIZE, CELL_SIZE, true);
                }
            }

        if (gameOver) {
            g.setColor(Color.white);
            for (int y = 0; y < GAME_OVER_MSG.length; y++)
                for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                    if (GAME_OVER_MSG[y][x] == 1) g.fill3DRect(x*15+28, y*15+225, 15, 15, true);
        } else
            figure.paint(g);
    }

    @Override
    public void updateMatrix(int[][] matrix) {
        this.matrix=matrix;
    }

    @Override
    public void updateFigure(Figure figure) {
        this.figure=figure;
    }

    @Override
    public void updateStatusGame(boolean gameOver) {
        this.gameOver=gameOver;
    }
}
