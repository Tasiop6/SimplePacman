import java.util.ArrayList;
import java.util.List;

public class Ghost extends BoardElement implements Movable{

    public int row;
    public int column;

    public  Ghost(){
    }

    public Ghost(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public List<int[]> moveOptions(Board board) {
        List<int[]> options = new ArrayList<>();

        // All possible ghost movements for each BoardCell
        // 1. Top row
        if (row == 0) {
            // 1a. Top row, first cell
            if (column == 0) {
                topFirstCellMovements(board, options);
            } else if (column == board.columns) {
                // 1b. Top row, last cell
                topLastCellMovements(board, options);
            } else {
                // 1c. Top row, rest of cells
                topCellMovements(board, options);
            }
            // 2. Bottom row
        } else if (row == board.rows) {
            // 2a. Bottom row, first cell
            if (column == 0) {
                bottomFirstCellMovements(board, options);
            } else if (column == board.columns) {
                // 2b. Bottom row, last cell
                bottomLastCellMovements(board, options);
            } else {
                // 2c. Bottom row, rest of cells
                bottomCellMovements(board, options);
            }
            // 3. Middle rows
        } else {
            // 3a. Middle rows, first cell
            if (column == 0) {
                middleFirstCellMovements(board, options);
            } else if (column == board.columns) {
                // 3b. Top row, last cell
                middleLastCellMovements(board, options);
            } else {
                // 3c. Middle row, rest of cells
                standardCellMovements(board, options);
            }
        }
        return options;
    }
    // Different moves for each cell of the Board borders.
    // 1. Top row-------------------------------------------------------
    // 1a. When ghost is in first cell of top row (row = 0 & column = 0)
    private void topFirstCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        int maxColumn = board.columns - 1;
        options.add(new int[]{maxRow, 0}); // Up
        options.add(new int[]{1, 0}); // Down
        options.add(new int[]{0, maxColumn}); // Left
        options.add(new int[]{0, 1}); // Right
    }

    // 1b. When ghost is in last cell of top row (row = 0 & column = max)
    private void topLastCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        options.add(new int[]{maxRow, column}); // Up
        options.add(new int[]{1, column}); // Down
        options.add(new int[]{0, column - 1}); // Left
        options.add(new int[]{0, 0}); // Right
    }

    // 1c. When ghost is in a middle cell of top row (row = 0 & 0 < column < max)
    private void topCellMovements(Board board, List<int[]> options) {
        int maxRow = board.rows - 1;
        options.add(new int[]{maxRow, column}); // Up
        options.add(new int[]{1, column}); // Down
        options.add(new int[]{0, column - 1}); // Left
        options.add(new int[]{0, column + 1}); // Right
    }

    // 2. Bottom row-------------------------------------------------------
    // 2a. When ghost is in first cell of bottom row (row = max & column = 0)
    private void bottomFirstCellMovements(Board board, List<int[]> options) {
        int maxColumn = board.columns - 1;
        options.add(new int[]{row - 1, 0}); // Up
        options.add(new int[]{0, 0}); // Down
        options.add(new int[]{row, maxColumn}); // Left
        options.add(new int[]{row, 1}); // Right
    }
    // 2b. When ghost is in last cell of bottom row (row = max & column = max)
    private void bottomLastCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{0, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, 0}); // Right
    }

    // 2c. When ghost is in a middle cell of bottom row (row = max & 0 < column < max)
    private void bottomCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{0, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, column + 1}); // Right
    }

    // 3. Middle rows-----------------------------------------------------------
    // 3a. When ghost is in fist cell of middle row (0< row < max & column = 0)
    private void middleFirstCellMovements(Board board, List<int[]> options) {
        int maxColumn = board.columns - 1;
        options.add(new int[]{row - 1, 0}); // Up
        options.add(new int[]{row + 1, 0}); // Down
        options.add(new int[]{row, maxColumn}); // Left
        options.add(new int[]{row, 1}); // Right
    }

    // 3b. When ghost is in last cell of middle row (0< row < max & column = max)
    private void middleLastCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{row + 1, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, 0}); // Right
    }

    // 3c. When ghost is in a middle cell of middle row (Standard moves with no exceptions)
    private void standardCellMovements(Board board, List<int[]> options) {
        options.add(new int[]{row - 1, column}); // Up
        options.add(new int[]{row + 1, column}); // Down
        options.add(new int[]{row, column - 1}); // Left
        options.add(new int[]{row, column + 1}); // Right
    }
}
