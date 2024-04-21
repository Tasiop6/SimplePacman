import java.util.ArrayList;
import java.util.List;

public class Player extends BoardElement implements Movable {

    public int row;

    public int column;

    public int energy;

    public Player() {
    }

    public Player(int row, int column, int energy) {
        this.row = row;
        this.column = column;
        this.energy = energy;
    }

    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> options = new ArrayList<>();

        // All possible player movements for each BoardCell
        // 1. Top row
        if (row == 0){
            // 1a. Top row, first cell
            if (column == 0) {
                topFirstCellMovements(board, options);
            } else if (column == board.columns) {
                // 1b. Top row, last cell
                topLastCellMovements(board, options);
            }else {
                // 1c. Top row, rest of cells
                topCellMovements(board, options);
            }
            // 2. Bottom row
        } else if (row == board.rows) {
            // 2a. Bottom row, first cell
            if (column == 0) {
                bottomFirstCellMovements(board, options);
            }else if (column == board.columns) {
                // 2b. Bottom row, last cell
                bottomLastCellMovements(board, options);
            }else {
                // 2c. Bottom row, rest of cells
                bottomCellMovements(board, options);
            }
            // 3. Middle rows
        }else {
            // 3a. Middle rows, first cell
            if (column == 0) {
                middleFirstCellMovements(board, options);
            } else if (column == board.columns) {
                // 3b. Top row, last cell
                middleLastCellMovements(board, options);
            }else {
                // 3c. Middle row, rest of cells
                standardCellMovements(board, options);
            }
        }
        return options;
    }

    // Different moves for each cell of the Board borders.
    // 1. Top row-------------------------------------------------------
    // 1a. When player is in first cell of top row (row = 0 & column = 0)
    private void topFirstCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        int maxColumn = board.columns - 1;
        options.add(new int[]{maxRow, 0}); // Up
        options.add(new int[]{1, 0}); // Down
        options.add(new int[]{0, maxColumn}); // Left
        options.add(new int[]{0, 1}); // Right

        // Diagonal moves
        options.add(new int[]{maxRow, maxColumn}); // Up-left
        options.add(new int[]{maxRow, 1}); // Up-right
        options.add(new int[]{1, maxColumn}); // Down-left
        options.add(new int[]{1, 1}); // Down-right
    }

    // 1b. When player is in last cell of top row (row = 0 & column = max)
    private void topLastCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        options.add(new int[]{maxRow, column}); // Up
        options.add(new int[]{1, column}); // Down
        options.add(new int[]{0, column - 1}); // Left
        options.add(new int[]{0, 0}); // Right

        // Diagonal moves
        options.add(new int[]{maxRow, column - 1}); // Up-left
        options.add(new int[]{maxRow, 0}); // Up-right
        options.add(new int[]{1, column - 1}); // Down-left
        options.add(new int[]{1, 0}); // Down-right
    }

    // 1c. When player is in a middle cell of top row (row = 0 & 0 < column < max)
    private void topCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        options.add(new int[]{maxRow, column}); // Up
        options.add(new int[]{1, column}); // Down
        options.add(new int[]{0, column - 1}); // Left
        options.add(new int[]{0, column + 1}); // Right

        // Diagonal moves
        options.add(new int[]{maxRow, column - 1}); // Up-left
        options.add(new int[]{maxRow, column + 1}); // Up-right
        options.add(new int[]{1, column - 1}); // Down-left
        options.add(new int[]{1, column + 1}); // Down-right
    }

    // 2. Bottom row-------------------------------------------------------
    // 2a. When player is in first cell of bottom row (row = max & column = 0)
    private void bottomFirstCellMovements(Board board, List<int[]> options) {
        int maxColumn = board.columns - 1;
        options.add(new int[]{row - 1, 0}); // Up
        options.add(new int[]{0, 0}); // Down
        options.add(new int[]{row, maxColumn}); // Left
        options.add(new int[]{row, 1}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, maxColumn}); // Up-left
        options.add(new int[]{row - 1, 1}); // Up-right
        options.add(new int[]{0, maxColumn}); // Down-left
        options.add(new int[]{0, 1}); // Down-right
    }
    // 2b. When player is in last cell of bottom row (row = max & column = max)
    private void bottomLastCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{0, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, 0}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, column - 1}); // Up-left
        options.add(new int[]{row - 1, 0}); // Up-right
        options.add(new int[]{0, column - 1}); // Down-left
        options.add(new int[]{0, 0}); // Down-right
    }

    // 2c. When player is in a middle cell of bottom row (row = max & 0 < column < max)
    private void bottomCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{0, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, column + 1}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, column - 1}); // Up-left
        options.add(new int[]{row - 1, column + 1}); // Up-right
        options.add(new int[]{0, column - 1}); // Down-left
        options.add(new int[]{0, column + 1}); // Down-right
    }

    // 3. Middle rows-----------------------------------------------------------
    // 3a. When player is in fist cell of middle row (0< row < max & column = 0)
    private void middleFirstCellMovements(Board board, List<int[]> options) {
        int maxColumn = board.columns - 1;
        options.add(new int[]{row - 1, 0}); // Up
        options.add(new int[]{row + 1, 0}); // Down
        options.add(new int[]{row, maxColumn}); // Left
        options.add(new int[]{row, 1}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, maxColumn}); // Up-left
        options.add(new int[]{row - 1, 1}); // Up-right
        options.add(new int[]{row + 1, maxColumn}); // Down-left
        options.add(new int[]{row + 1, 1}); // Down-right
    }

    // 3b. When player is in last cell of middle row (0< row < max & column = max)
    private void middleLastCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{row + 1, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, 0}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, column - 1}); // Up-left
        options.add(new int[]{row - 1, 0}); // Up-right
        options.add(new int[]{row + 1, column - 1}); // Down-left
        options.add(new int[]{row + 1, 0}); // Down-right
    }

    // 3c. When player is in a middle cell of middle row (Standard moves with no exceptions)
    private void standardCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{row + 1, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, column + 1}); // Right

        // Diagonal moves
        options.add(new int[]{row - 1, column - 1}); // Up-left
        options.add(new int[]{row - 1, column + 1}); // Up-right
        options.add(new int[]{row + 1, column - 1}); // Down-left
        options.add(new int[]{row + 1, column + 1}); // Down-right
    }
    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
