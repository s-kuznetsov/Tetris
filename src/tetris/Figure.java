package tetris;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Figure implements Observer {
    private static final int FIELD_WIDTH = Constants.FIELD_WIDTH;
    private static final int[][][] SHAPES = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 0x00f0f0}},
            {{0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {4, 0xf0f000}},
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x0000f0}},
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf0a000}},
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x00f000}},
            {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xa000f0}},
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf00000}}
    };
    private ArrayList<Cell> cells = new ArrayList<Cell>();
    private int[][] shape = new int[4][4];
    private int size, color;
    private int x = 3, y = 0;
    private int[][] matrix;

    Figure() {
        int type = (new Random()).nextInt(SHAPES.length);
        this.size = SHAPES[type][4][0];
        this.color = SHAPES[type][4][1];
        if (size == 4) y = -1;
        for (int i = 0; i < size; i++)
            System.arraycopy(SHAPES[type][i], 0, shape[i], 0, SHAPES[type][i].length);
        createFromShape();
    }

    private void createFromShape() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) cells.add(new Cell(x + this.x, y + this.y));
    }

    public boolean isTouchGround() {
        for (Cell cell : cells) if (matrix[cell.getY() + 1][cell.getX()] > 0) return true;
        return false;
    }

    public boolean isCrossGround() {
        for (Cell cell : cells) if (matrix[cell.getY()][cell.getX()] > 0) return true;
        return false;
    }

    public void leaveOnTheGround() {
        for (Cell cell : cells) matrix[cell.getY()][cell.getX()] = color;
    }

    private boolean isTouchWall(int direction) {
        for (Cell cell : cells) {
            if (direction == KeyEvent.VK_LEFT && (cell.getX() == 0 || matrix[cell.getY()][cell.getX() - 1] > 0)) return true;
            if (direction == KeyEvent.VK_RIGHT && (cell.getX() == FIELD_WIDTH - 1 || matrix[cell.getY()][cell.getX() + 1] > 0)) return true;
        }
        return false;
    }

    public void move(int direction) {
        if (!isTouchWall(direction)) {
            int dx = direction - 38;
            for (Cell cell : cells) cell.setX(cell.getX() + dx);
            x += dx;
        }
    }

    public void stepDown() {
        for (Cell cell : cells) cell.setY(cell.getY() + 1);
        y++;
    }

    public void drop() { while (!isTouchGround()) stepDown(); }

    private boolean isWrongPosition() {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (shape[y][x] == 1) {
                    if (y + this.y < 0) return true;
                    if (x + this.x < 0 || x + this.x > FIELD_WIDTH - 1) return true;
                    if (matrix[y + this.y][x + this.x] > 0) return true;
                }
        return false;
    }

    private void rotateShape(int direction) {
        for (int i = 0; i < size/2; i++)
            for (int j = i; j < size-1-i; j++)
                if (direction == KeyEvent.VK_RIGHT) {
                    int tmp = shape[size-1-j][i];
                    shape[size-1-j][i] = shape[size-1-i][size-1-j];
                    shape[size-1-i][size-1-j] = shape[j][size-1-i];
                    shape[j][size-1-i] = shape[i][j];
                    shape[i][j] = tmp;
                } else {
                    int tmp = shape[i][j];
                    shape[i][j] = shape[j][size-1-i];
                    shape[j][size-1-i] = shape[size-1-i][size-1-j];
                    shape[size-1-i][size-1-j] = shape[size-1-j][i];
                    shape[size-1-j][i] = tmp;
                }
    }

    public void rotate() {
        rotateShape(KeyEvent.VK_RIGHT);
        if (!isWrongPosition()) {
            cells.clear();
            createFromShape();
        } else
            rotateShape(KeyEvent.VK_LEFT);
    }

    public void paint(Graphics g) {
        for (Cell cell : cells) cell.paint(g, color);
    }

    @Override
    public void updateMatrix(int[][] matrix) {
        this.matrix=matrix;
    }

    @Override
    public void updateFigure(Figure figure) {

    }

    @Override
    public void updateStatusGame(boolean gameOver) {

    }
}
