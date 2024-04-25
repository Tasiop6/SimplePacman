package ce326.hw2;

import java.util.*;

public class Board {
    int rows;
    int columns;
    BoardCell[][] cells;

    public Board() {
    }

    // Copy constructor (for list of Boards for jump)
    public Board(Board board) {
        this.rows = board.rows;
        this.columns = board.columns;
        cells = new BoardCell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                BoardElement[] elements = new BoardElement[2];
                cells[i][j] = new BoardCell();
                cells[i][j].elements = elements;
                cells[i][j].elements[0] = board.getElement(i , j,0);
                cells[i][j].elements[1] = board.getElement(i , j,1);

            }
        }
    }

    // Conversions of user moves to cells in Board.
    // Conversion of row to letter
    public String rowToLetter(int row) {
        return Character.toString ((char) (row + 'A'));
    }

    //Conversion of column to number
    public int columnToNumber(int number) {
        return number + 1;
    }

    public BoardCell getCells(int row, int column) {
        return this.cells[row][column];
    }

    public void setFirstElement(int row,int column, BoardElement element ) {
        cells[row][column].elements[0] = element;
    }

    public BoardElement getElement(int row, int column, int i) {
        if(i <= 1){
            return cells[row][column].elements[i];
        }
        return null;
    }

    public void setSecondElement(int row,int column, BoardElement element ) {
        cells[row][column].elements[1] = element;
    }
    // Prints our board
    public void printBoard() {
        System.out.print(" ");
        for (int i = 0; i < columns; i++) {
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
        switch (element) {
            case null -> {
                if (cells[i][j].getHasVisited()) {
                    System.out.print(" ");
                } else {
                    System.out.print("-");
                }
            }
            case Obstacle obstacle -> System.out.print("#");
            case Vegetable vegetable -> System.out.print("v");
            case Fish fish -> System.out.print("f");
            case Meat meat -> System.out.print("m");
            case Player player -> System.out.print("X");
            case Ghost ghost -> System.out.print("@");
            default -> {
            }
        }
    }
    public int[] playerPosition() {
        int[] pos = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].elements[0] instanceof Player) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }

    // Find position of ghost(not moved)
    public int[] ghostPosition(List<int[]> movedGhost) {
        int k = 0;
        int[] ghostPos = new int[2];
        for (int i = 0; i < rows; i++) {
            outerLoop:
            for (int j = 0; j < columns; j++) {
                // Scan board for i-th ghost
                int[] boardCell = new int[2];
                boardCell[0] = i;
                boardCell[1] = j;
                if (cells[i][j].elements[0] instanceof Ghost) {
                    if (movedGhost.isEmpty()) {
                        // No ghost is moved, copy position
                        ghostPos[0] = i;
                        ghostPos[1] = j;
                        return ghostPos;
                    }
                    for (int[] movedCell : movedGhost) {
                        if (Arrays.equals(movedCell, boardCell)) {
                            continue outerLoop; // Ghost was moved, skip to the next cell
                        }
                    }
                    // Found moved ghost, copy position
                    ghostPos[0] = i;
                    ghostPos[1] = j;
                    return ghostPos;
                }
            }
        }
        return ghostPos;
    }
    public BoardElement checkFood(int row, int column) {
        return switch (cells[row][column].elements[0]) {
            case Meat meat -> new Meat();
            case Vegetable vegetable -> new Vegetable();
            case Fish fish -> new Fish();
            case null, default -> null;
        };
    }

    // Find the shortest path with Dijkstra
    public List<DijkstraCell> shortestPath(int[] playerPos) {
        int rows = this.rows;
        int columns = this.columns;
        // Create a grid of our board with ones in cells
        // with obstacles and 0 in evey other cell
        int[][] grid = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++){
                if (getElement(i, j, 0) instanceof Obstacle){
                    grid[i][j] = 1;
                }else{
                    grid[i][j] = 0;
                }
            }
        }
        // Distance array and Visited array
        int[][] distanceArray = new int[rows][columns];
        boolean[][] visitedArray = new boolean[rows][columns];
        // Fill distance array with max int value
        for (int i=0; i < rows; i++) {
            Arrays.fill(distanceArray[i], Integer.MAX_VALUE);
        }
        // Movement directions
        int[] dx = {0, 0, 1, -1}; // Right, Left
        int[] dy = {1, -1, 0, 0}; // Down, Left

        // Priority queue for Dijkstra
        PriorityQueue<DijkstraCell> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(cell -> cell.distance));
        // Add cell of player through Dijkstra cell
        priorityQueue.offer(new DijkstraCell(playerPos[0], playerPos[1], 0));
        // Player's distance = 0
        distanceArray[playerPos[0]][playerPos[1]] = 0;
        // Dijkstra
        while (!priorityQueue.isEmpty()) {
            DijkstraCell currentCell = priorityQueue.poll();
            int currentRow = currentCell.row;
            int currentColumn = currentCell.column;

            // If we have been in this cell go to next iteration
            if (visitedArray[currentRow][currentColumn]) continue;
            // Now we have visited
            visitedArray[currentRow][currentColumn] = true;
            // Traverse through all neighbouring cells
            for (int i = 0; i < 4; i++) {
                int newRow = (currentRow + dx[i] + rows) % rows; // Wrap around if out of bounds
                int newColumn = (currentColumn + dy[i] + columns) % columns; // Wrap around if out of bounds
                // Check if neighbouring cell has obstacle(1)
                if (grid[newRow][newColumn] == 0) {
                    // +1 is cost of move
                    if (distanceArray[newRow][newColumn] > distanceArray[currentRow][currentColumn] + 1) {
                        distanceArray[newRow][newColumn] = distanceArray[currentRow][currentColumn] + 1;
                        priorityQueue.offer(new DijkstraCell(newRow, newColumn, distanceArray[newRow][newColumn]));
                    }
                }
            }
        }
        // Copy cells with the shortest path to our list
        List<DijkstraCell> shortestPath = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (distanceArray[i][j] != Integer.MAX_VALUE) {
                    shortestPath.add(new DijkstraCell(i, j, distanceArray[i][j]));
                }
            }
            // Save distance array
            Homework2.dijkstraDistanceArray = distanceArray;
        }
        return shortestPath;
    }
}
