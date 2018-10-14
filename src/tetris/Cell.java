package tetris;

import java.awt.*;

public class Cell {
    private static final int CELL_SIZE = Constants.CELL_SIZE;
    private int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void paint(Graphics g, int color) {
        g.setColor(new Color(color));
        g.fill3DRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE, true);
    }
}
