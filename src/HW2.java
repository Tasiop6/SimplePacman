import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HW2 {

    static List<int[]> playerMovesList;

    static List<List<int[]>> ghostList;
    public static void main (String[] args) {
        Board board = new Board();
        Scanner scanner = new Scanner(System.in);
        char option;
        String moveInput = null;
        boolean gamePause;
        boolean gameEnd;
        // Starting Menu until board is filled or quit
        do {
            printFirstMenu();
            option = scanner.next().charAt(0);
            handleFirstOption(option, board);
        } while (board.rows == 0);
        gamePause = false;
        gameEnd = false;
        // Game has Started.
        // Play game until we quit(GameEnd == true)
        while(!gameEnd) {
            // Run game or print menu based on gamePause
            if (gamePause) {
                do {
                    // Game menu (GamePause == true)
                    printMenu();
                    option = scanner.next().charAt(0);
                    handleOption(option, board, gamePause);
                } while (option != 'Q' && option != 'q' && option != 'C' && option != 'c');
                // Game status: Paused and we want to quit
                if (option == 'Q' || option == 'q') {
                    gameEnd = true;
                }
                // Game status: Paused and we want to continue
                if (option == 'C' || option == 'c') {
                    gamePause = false;
                }
            } else {
                //Game is running (GamePause == false)
                // Player moves until Z/z
                do {
                    System.out.print("Player move:");
                    moveInput = scanner.next();
                    playerMove(board, moveInput);

                } while (moveInput.charAt(0) != 'Z' && moveInput.charAt(0) != 'z');
                // Game status: Playing and we want to stop
                if(moveInput.charAt(0) != 'Z' || moveInput.charAt(0) != 'z') {
                    gamePause = true;
                }
            }
            System.out.println(moveInput+" "+option);
        }
    }
    // Starting Menu
    private static void printFirstMenu() {
        System.out.println("- Load Game      (L/l)");
        System.out.println("- Quit           (Q/q)\n");
        System.out.print("Your option: ");
    }

    // Basic Menu
    private static void printMenu() {
        System.out.println("- Load Game      (L/l)");
        System.out.println("- Debug Dijkstra (D/d)");
        System.out.println("- Show History   (H/h)");
        System.out.println("- Jump to move   (J/j)");
        System.out.println("- Continue game  (C/c)");
        System.out.println("- Quit           (Q/q)\n");
        System.out.print("Your option: ");
    }

    // Handling first user input
    private static void handleFirstOption(char option, Board board) {
        switch (option) {
            case 'L':
            case 'l':
                loadGame(board);
                break;
            case 'Q':
            case 'q':
                System.out.println("Exiting program...");
                break;
            default:
                System.out.println("Invalid option");
        }
    }

    // Handling following user inputs
    private static void handleOption(char option, Board board,boolean gamePause) {
        switch (option) {
            case 'L':
            case 'l':
                loadGame(board);
                break;
            case 'D':
            case 'd':
                debugDijkstra();
                break;
            case 'H':
            case 'h':
                showHistory();
                break;
            case 'J':
            case 'j':
                jumpToMove();
                break;
            case 'C':
            case 'c':
                continueGame();
                break;
            case 'Q':
            case 'q':
                System.out.println("Exiting program...");
                break;
            default:
                System.out.println("Invalid option");
        }
        System.out.println(); // New line for clarity
    }
    // L/l : Load game from file---------------------------------------------------------------------
    private static void loadGame(Board board) {
        System.out.println("Enter input filename: ");
        Scanner scanner = new Scanner(System.in);
//        String filePath = scanner.nextLine();
        String filePath = "C:\\Users\\tasio\\IdeaProjects\\hw2\\src\\package.json";
        initializeBoard(filePath, board);
    }
    // Initialize board through JSON file
    public static void initializeBoard(String filePath, Board board) {
        try {
            // JSONObject from our method
            JSONObject jsonObject = readJSONFile(filePath, board);
            if (jsonObject != null) {
                // Save to corresponding board variables
                int rows = jsonObject.getInt("rows");
                int columns = jsonObject.getInt("columns");
                int energy = jsonObject.getInt("energy");
                JSONArray initArray = jsonObject.getJSONArray("init");
                // Check JSON arguments
                if (!checkJsonArguments(rows, columns, energy, initArray)) {
                    System.out.format("Illegal arguments in '%s'", filePath);
                    return;
                }
                // Δημιουργία του πίνακα καμβά με βάση τα δεδομένα από το JSON
                board.rows = rows;
                board.columns = columns;
                board.cells =new BoardCell[rows][columns];
                for (int i = 0; i < initArray.length(); i++) {
                    int Row = i/columns;
                    int Column = i%columns;
                    System.out.println(Row+" "+Column);
                    System.out.println(initArray.getString(i).charAt(0));
                    switch (initArray.getString(i).charAt(0)) {
                        case '-':
                            board.cells[i / columns][i % columns] = new BoardCell(new Space());
                            break;
                        case '#':
                            board.cells[i / columns][i % columns] = new BoardCell(new Obstacle());
                            break;
                        case 'v':
                            board.cells[i / columns][i % columns] = new BoardCell(new Vegetable());
                            break;
                        case 'f':
                            board.cells[i / columns][i % columns] = new BoardCell(new Fish());
                            break;
                        case 'm':
                            board.cells[i / columns][i % columns] = new BoardCell(new Meat());
                            break;
                        case 'X':
                            Player player = new Player(i / columns, i % columns, energy);
                            playerMovesList = player.moveOptions(board);
                            board.cells[i / columns][i % columns] = new BoardCell(player);
                            board.cells[i / columns][i % columns].hasVisited = true;
                            break;
                        case '@':
                            Ghost ghost = new Ghost(i / columns, i % columns);
                            board.cells[i / columns][i % columns] = new BoardCell(ghost);
                            break;
                    }
                }
                board.printBoard();
            }
        } catch (JSONException e) {
            System.err.println("File NOT in JSON format: " + filePath);
        }
    }
    // Check file path and file form and read JSON file
    public static JSONObject readJSONFile(String filePath, Board board) {
        JSONObject jsonObject = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();
            jsonObject = new JSONObject(jsonString);
        } catch (IOException e) {
            System.err.println("Unable to read " + filePath);
            loadGame(board);
        }
        return jsonObject;
    }

    public static boolean checkJsonArguments(int rows, int columns, int energy, JSONArray initArray) {
        return rows > 0 && columns > 0 && energy > 0 && rows * columns == initArray.length();
    }

    // D/d : Print Dijkstra board------------------------------------------------------------------
    private static void debugDijkstra() {
        // Implement debugging Dijkstra functionality here
        System.out.println("Debugging Dijkstra...");
    }

    private static void showHistory() {
        // Implement showing history functionality here
        System.out.println("Showing history...");
    }

    private static void jumpToMove() {
        // Implement jump to move functionality here
        System.out.println("Jumping to move...");
    }

    private static void continueGame() {
        System.out.println("Continuing game...");
    }

    // Check the move from input
    public static void playerMove(Board board, String moveInput) {
        // Check if string is of type [letter,number];
        if(!Character.isLetter(moveInput.charAt(0)) && !Character.isDigit(moveInput.charAt(1))) {
            System.out.println("Invalid input. Try again...");
            return;
        }
        int[] pos = new int[2];
        // Convert letter and column to corresponding board cell
        pos[0] = moveInput.charAt(0) - 'A';
        pos[1] = moveInput.charAt(1) - '1';
        System.out.println(moveInput+": "+pos[0]+" + "+pos[1]);
        // Check if move is within the limits of the board
        if(pos[0] > board.rows || pos[1] > board.columns) {
            System.out.println("Invalid input. Try again...");
            return;
        }
        // Check if move exists in player's moveOptions
        if(exists(pos)) {
            System.out.println("Exists");
            BoardCell cellToGo = board.getCells(pos[0], pos[1]);

            if (cellToGo.getElement(0) instanceof Obstacle) {
                System.out.println("Invalid input. Try again...");
            }
            if (cellToGo.getElement(0) instanceof Ghost) {
                System.out.println("Game over...");
            }
        }
        //board.cells[row][column].elements[0].
    }

    private static boolean exists(int[] pos) {
        for (int[] move : playerMovesList) {
            if (Arrays.equals(move, pos)) {
                return true;
            }
        }
        return false;
    }
}