package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class Ghost extends BoardElement implements Movable {

    public int row;
    public int column;

    public Ghost(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> options = new ArrayList<>();

        options.add(new int[]{(row - 1 + board.rows) % board.rows, column}); // Up
        options.add(new int[]{(row + 1) % board.rows, column}); // Down
        options.add(new int[]{row, (column - 1 + board.columns) % board.columns}); // Left
        options.add(new int[]{row, (column + 1) % board.columns}); // Right

        /*//Check for Obstacles
        if(board.getElement(((row - 1 + board.rows) % board.rows), column, 0) instanceof Obstacle) {
            options.removeFirst(); // Up
        }
        if(board.getElement(((row + 1) % board.rows), column, 0) instanceof Obstacle) {
            options.remove(1); // Up
        }
        if(board.getElement(row, (column - 1 + board.columns) % board.columns, 0) instanceof Obstacle) {
            options.remove(2); // Up
        }
        if(board.getElement(row, (column + 1) % board.columns, 0) instanceof Obstacle) {
            options.remove(3); // Up
        }*/

        return options;
    }
}
