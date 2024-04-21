public class Board {
    int rows;
    int columns;
    BoardCell[][] cells;

    public Board() {
    }

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        cells = new BoardCell[rows - 1][columns - 1]; //Start at zero
    }

    // Conversions of user moves to cells in Board.
    // Conversion of letter to row
    public int letterToRow(String letter) {
        return letter.toUpperCase().charAt(0) - 'A';
    }

    //Conversion of number to colum
    public int numberToColumn(int number) {
        return number - 1;
    }

    public int columnToNumber(int number) {
        return number + 1;
    }

    private void dummy() {
        //Player player = new Player(0, 0);
        Board board = new Board(5, 6);
        Vegetable v = new Vegetable();
        //board.cells[0][0].elements[0] = v;
        //List<int[]> dummy1 = player.moveOptions(board);

        //List<int[]> obstacle = obstaclesPositions; // This will return a list with all the obstacles

        //dummy1.remove(obstacle);

    }

    public BoardCell getCells(int row, int column) {
        return this.cells[row][column];
    }

    // Prints our board
    public void printBoard() {
        System.out.print(" ");
        for (int i = 0; i < rows; i++) {
            System.out.print(" " + columnToNumber(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < rows; i++) {
            char row = (char) ('A' + i);
            System.out.print(row);
            for (int j = 0; j < columns; j++) {
                System.out.print(" ");
                printCellElement(cells[i][j].elements[0], i, j);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Prints contents of each cell of board
    public void printCellElement(BoardElement element, int i, int j) {
        // Object in Cell is Space and player has visited (" " or "-")
        if (element instanceof Space) {
            if (cells[i][j].getHasVisited()) {
                System.out.print(" ");
            } else {
                System.out.print("-");
            }
        } else if (element instanceof Obstacle) {
            System.out.print("#");
        } else if (element instanceof Vegetable) {
            System.out.print("v");
        } else if (element instanceof Fish) {
            System.out.print("f");
        } else if (element instanceof Meat) {
            System.out.print("m");
        } else if (element instanceof Player) {
            System.out.print("X");
        } else if (element instanceof Ghost) {
            System.out.print("@");
        }
    }
    public int[] PlayerPosition(Board board) {
        int[] pos = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board.cells[i][j].elements[0] instanceof Player) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }
}
