package tetris;

public interface Observer {
    void updateMatrix(int[][] matrix);
    void updateFigure(Figure figure);
    void updateStatusGame(boolean gameOver);
}
