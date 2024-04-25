package ce326.hw2;

import java.util.ArrayList;
import java.util.List;

public class Player extends BoardElement implements Movable {

    public int row;

    public int column;

    public int energy;


    public Player(int row, int column, int energy) {
        this.row = row;
        this.column = column;
        this.energy = energy;
    }

    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> options = new ArrayList<>();

        options.add(new int[]{(row - 1 + board.rows) % board.rows, column}); // Up
        options.add(new int[]{(row + 1) % board.rows, column}); // Down
        options.add(new int[]{row, (column - 1 + board.columns) % board.columns}); // Left
        options.add(new int[]{row, (column + 1) % board.columns}); // Right

        //Diagonal moves
        options.add(new int[]{(row - 1 + board.rows) % board.rows, (column - 1 + board.columns) % board.columns}); // Up-Left
        options.add(new int[]{(row - 1 + board.rows) % board.rows, (column + 1) % board.columns}); // Up-Right
        options.add(new int[]{(row + 1) % board.rows, (column - 1 + board.columns) % board.columns}); // Down-Left
        options.add(new int[]{(row + 1) % board.rows, (column + 1) % board.columns}); // Down-Right

        return options;
    }
}
