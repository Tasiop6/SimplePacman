package ce326.hw2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Homework2 {

    // Pause game
    static boolean gamePause;

    // Ends game
    static boolean gameEnd;

    //Board
    static Board board;

    //List of player's moves list
    static List<int[]> playerMovesList;

    // List of all ghosts' moves lists
    static List<List<int[]>> ghostsMovesList = new ArrayList<>();

    // Player's energy
    static int energy;

    // List of all energies trough gameTurns
    static List<Integer> energies = new ArrayList<>();

    // Amount of ghosts
    static int ghosts;

    // Active when board is cleared
    static boolean playerWin;

    // Active when game is over
    static boolean gameOver;

    // Moves played by player
    static List<String> finalPlayerMoves = new ArrayList<>();

    // Moves played by ghosts
    static List<String> finalGhostMoves = new ArrayList<>();

    // Debug dijkstra is on
    static boolean dijkstraOn;

    // Dijkstra array board
    static int[][] dijkstraDistanceArray;

    // Game turns
    static int gameTurns;

    // List of Boards (add one each gameTurn)
    static List<Board> BoardsGameList = new ArrayList<>();

    // Jump button(for load)
    static boolean jumpActivated;

    public static void main(String[] args) {
        board = new Board();
        Scanner scanner = new Scanner(System.in);
        char option;
        String moveInput = null;
        
        // Starting Menu until board is filled or quit
        do {
            printFirstMenu();
            option = scanner.next().charAt(0);
            handleFirstOption(option);
        } while (board.rows == 0);
        
        gamePause = false;
        gameEnd = false;
        
        // Game has Started.
        // Play game until we quit(GameEnd == true)
        while (!gameEnd) {
            // Run game or print menu based on gamePause or gameOver
            if (gamePause || gameOver || playerWin) {
                do {
                    // Game menu
                    printMenu();
                    option = scanner.next().charAt(0);
                    handleOption(option);
                
                } while (option != 'Q' && option != 'q' && option != 'C' && option != 'c' && option != 'L' && option != 'l' && option != 'J' && option != 'j');
                
                // Game status: Paused and we want to quit
                if (option == 'Q' || option == 'q') {
                    gameEnd = true;
                }
                
                // Game status: Paused and we want to continue or load new game
                if (option == 'C' || option == 'c' || option == 'L' || option == 'l') {
                    gamePause = false;
                }
            } else {
                //Game is running (GamePause == false)
                // Player moves until Z/z
                do {
                    System.out.print("Player move:");
                    // Scan player's next move
                    moveInput = scanner.next();
                    // Check if possible and execute
                    int checkIfValid = playerMove(board, moveInput);
                    switch (checkIfValid) {
                        case 0:
                            System.out.println();
                            System.out.println("Invalid input. Try again...");
                            System.out.println();
                            // Remove last entry of final player moves
                            break;
                        case -1:
                            gameOver = true;
                            System.out.println();
                            System.out.println("YOU LOST");
                            //Remove last entry of final player moves;
                            //finalPlayerMoves.removeLast();
                            break;
                        case 1:
                            // Move Ghosts
                            int checkIfPlayerHit = moveGhosts(board);
                            if (checkIfPlayerHit == -1) {
                                gameOver = true;
                                // Remove last entry from player moves.
                                // Player loses on first move
                                if(finalPlayerMoves.size()==2) {
                                    finalPlayerMoves.clear();
                                }else{
                                    finalPlayerMoves.removeLast();
                                    finalPlayerMoves.remove(2 * gameTurns - 2); //second last
                                }
                            }else{
                                // Add game turn(for history iterations)
                                gameTurns++;
                            }
                            break;
                        default:
                    }
                    // Print Board if game is still on
                    if (!gameOver && checkIfValid != 0 && moveInput.charAt(0) != 'z' && moveInput.charAt(0) != 'Z') {
                        if (dijkstraOn) {
                            System.out.println();
                            printDijkstraBoard();
                        }

                        System.out.println();
                        board.printBoard();
                        System.out.println();
                    }
                } while (moveInput.charAt(0) != 'Z' && moveInput.charAt(0) != 'z' && !gameOver);
                // Game status: Playing and we want to stop
                if (moveInput.charAt(0) != 'Z' || moveInput.charAt(0) != 'z') {
                    System.out.println();
                    gamePause = true;
                }
            }
        }
    }

    // Starting Menu
    private static void printFirstMenu() {
        System.out.println("\n- Load Game      (L/l)");
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
    private static void handleFirstOption(char option) {
        switch (option) {
            case 'L':
            case 'l':
                loadGame();
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
    private static void handleOption(char option) {
        switch (option) {
            case 'L':
            case 'l':
                loadGame();
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
                break;
            default:
                System.out.println("\nInvalid option");
        }
    }

    // L/l : Load game from file---------------------------------------------------------------------
    private static void loadGame() {

        // If load is from jump
        if (jumpActivated){
            jumpActivated = false;
            // Change the features of board to loaded board
            for(int i=0; i < board.rows; i++){
                for(int j=0; j < board.columns; j++){

                    // Change all hasVisited except ghosts, obstacles and
                    // the ones from cells in finalPlayersMoves;
                    board.cells[i][j].setHasVisited(false);

                    // Found player
                    if(board.getElement(i, j, 0) instanceof Player){
                        Player player = new Player(i, j, energy);
                        board.cells[i][j].setHasVisited(true);
                        playerMovesList = player.moveOptions(board);
                    }
                    // Found ghost
                    if(board.getElement(i, j, 0) instanceof Ghost){
                        Ghost ghost = new Ghost(i, j);
                        ghostsMovesList.add(ghost.moveOptions(board));
                    }
                    // Found obstacle
                    if(board.getElement(i, j, 0) instanceof Obstacle){
                        board.cells[i][j].setHasVisited(true);
                    }
                    // Pos to string to compare
                    String position = board.rowToLetter(i) + (board.columnToNumber(j));
                    // Player has passed in older moves
                    if (finalPlayerMoves.contains(position)){
                        board.cells[i][j].setHasVisited(true);
                    }
                }
            }
            if (dijkstraOn) {
                System.out.println();
                printDijkstraBoard();
            }
            board.printBoard();
            System.out.println();
            return;
        }
        
        // New game initialize everything
        initializeGame();

        System.out.println();
        System.out.println("Enter input filename: ");
        
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        initializeBoard(filePath);
    }
    // Initialize game
    public static void initializeGame() {
        gameOver = false;
        gameTurns = 0;
        ghosts = 0;
        finalPlayerMoves.clear();
        finalGhostMoves.clear();
        energies.clear();
        //Initialize ghosts moves list again(previous loads)
        ghostsMovesList.clear();
    }

    // Initialize board through JSON file
    public static void initializeBoard(String filePath) {
        try {
            // JSONObject from our method
            JSONObject jsonObject = readJSONFile(filePath);
            if (jsonObject != null) {
                
                // Save to corresponding board variables
                int rows = jsonObject.getInt("rows");
                int columns = jsonObject.getInt("columns");
                energy = jsonObject.getInt("energy");
                energies.add(energy);
                JSONArray initArray = jsonObject.getJSONArray("init");
                
                // Check JSON arguments
                if (!checkJsonArguments(rows, columns, energy, initArray)) {
                    System.out.format("Illegal arguments in '%s'\n", filePath);
                    return;
                }
                // Instantiate board through JSON
                board.rows = rows;
                board.columns = columns;
                board.cells = new BoardCell[rows][columns];
               
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        BoardElement[] elements = new BoardElement[2];
                        board.cells[i][j] = new BoardCell();
                        board.cells[i][j].elements = elements;

                    }
                }
                // Set each cell with corresponding BoardElement
                for (int i = 0; i < initArray.length(); i++) {
                    int Row = i / columns;
                    int Column = i % columns;
                    switch (initArray.getString(i).charAt(0)) {
                        case '-':
                            board.setFirstElement(Row, Column, null);
                            break;
                        case '#':
                            board.setFirstElement(Row, Column, new Obstacle());
                            board.cells[Row][Column].hasVisited = true;
                            break;
                        case 'v':
                            board.setFirstElement(Row, Column, new Vegetable());
                            break;
                        case 'f':
                            board.setFirstElement(Row, Column, new Fish());
                            break;
                        case 'm':
                            board.setFirstElement(Row, Column, new Meat());
                            break;
                        case 'X':
                            Player player = new Player(Row, Column, energy);
                            playerMovesList = player.moveOptions(board);
                            board.setFirstElement(Row, Column, player);
                            board.cells[Row][Column].hasVisited = true;
                            break;
                        case '@':
                            Ghost ghost = new Ghost(Row, Column);
                            ghosts++;
                            ghostsMovesList.add(ghost.moveOptions(board));
                            board.setFirstElement(Row, Column, ghost);
                            break;
                    }
                }
                System.out.println();
                board.printBoard();
                System.out.println();
            }
        } catch (JSONException e) {
            System.out.println("File NOT in JSON format: " + filePath);
        }
    }

    // Check file path and file form and read JSON file
    public static JSONObject readJSONFile(String filePath) {
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
            System.out.println("Unable to read " + filePath);
            loadGame();
        }
        return jsonObject;
    }

    public static boolean checkJsonArguments(int rows, int columns, int energy, JSONArray initArray) {
        return rows > 0 && columns > 0 && energy > 0 && rows * columns == initArray.length();
    }

    // D/d : Print Dijkstra board------------------------------------------------------------------
    private static void debugDijkstra() {
        System.out.println();
        dijkstraOn = !dijkstraOn;
    }
    public static void printDijkstraBoard() {
        System.out.print(" ");
        for (int i = 0; i < board.columns; i++) {
            System.out.print(" " + board.columnToNumber(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < board.rows; i++) {
            char row = (char) ('A' + i);
            System.out.print(row);
            for (int j = 0; j < board.columns; j++) {

                if (dijkstraDistanceArray[i][j] != Integer.MAX_VALUE) {

                    // For symmetry
                    if(dijkstraDistanceArray[i][j]>9) {
                        System.out.print(dijkstraDistanceArray[i][j]);
                    }else{
                        System.out.print(" "+dijkstraDistanceArray[i][j]);
                    }
                }else{
                    System.out.print(" ?");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // H/h : Show history-----------------------------------------------------------
    private static void showHistory() {

        // Print history of moves
        int moveIndex = 0; // Iterator for ghost moves sets
        System.out.println("{\n  \"moves\": {");
        for (int i=0; i < gameTurns; i++){
            System.out.print("        \""+(i+i)+"\": \"");
            System.out.print(finalPlayerMoves.get(2*i));
            System.out.print("-");
            System.out.print(finalPlayerMoves.get(2*i+1));
            System.out.println("\",");
            System.out.print("        \""+(i+i+1)+"\": \"");
            
            for (int j=0; j<ghosts; j++) {
                System.out.print(finalGhostMoves.get(2*j+moveIndex));
                System.out.print("-");
                System.out.print(finalGhostMoves.get(2*j+1+moveIndex));
                if((j+1)==ghosts){
                    System.out.print("\"");
                }
                System.out.print(",");
            }
            System.out.println();
            moveIndex += 2*ghosts; // Move to next set of ghost moves
        }
        System.out.println("    }\n}");
        System.out.println();
    }

    // J/j : Jump to move---------------------------------------------------------------
    private static void jumpToMove() {
        System.out.println("\nEnter move number: ");
        Scanner scanner = new Scanner(System.in);
        int moveNumber = scanner.nextInt();
        if (moveNumber < 0 || moveNumber > 2 * gameTurns - 1) {
            System.out.println("Wrong number\n");
            return;
        }
        // MoveNumber exists, so we load that Board
        // and game plays where it left off.
        gameOver = false;   // In case our last move was a game over
        gamePause = false;

        // Clear lists than control available moves
        playerMovesList.clear();
        ghostsMovesList.clear();

        // Activate jump
        jumpActivated = true;

        // Set ghosts to 0
        //ghosts = 0;

        // Board is fetched from list
        board = new Board(BoardsGameList.get(moveNumber / 2));
        // Remove newer Boards
        BoardsGameList.subList(gameTurns,BoardsGameList.size());
        // Old energy
        energy = energies.get(gameTurns-1);
        energies.subList(gameTurns,energies.size()).clear();
        // Final player moves
        // Clear the list from bottom up to loaded Board moves
        // This is the case in which we jump to current gameTurn (gameTurn is the same)
            if(gameTurns != (moveNumber / 2) + 1) {
                gameTurns = (moveNumber / 2) + 1;
                if (finalPlayerMoves.size() > gameTurns) {
                    finalPlayerMoves.subList((gameTurns - 1) * 2 + 2, finalPlayerMoves.size()).clear();
                }
            }
        // Final ghost moves
        // Clear the list from bottom up to loaded Board moves
        if (finalGhostMoves.size() > gameTurns) {
            finalGhostMoves.subList(((gameTurns - 1) * 2 + 2) * ghosts, finalGhostMoves.size()).clear();
        }
        // Load jump
        loadGame();
    }

    // C/c : Continue Game
    private static void continueGame() {
        System.out.println();
        if(gameOver) {
            System.out.println("GAME OVER");
            System.out.println();
        }
    }

    // Player movement------------------------------------------------------------------
    // Check the move from input
    public static int playerMove(Board board, String moveInput) {
        
        // Check if string is of type [letter,number];
        if (!Character.isLetter(moveInput.charAt(0)) && !Character.isDigit(moveInput.charAt(1))) {
            return 0;
        }
        int[] newPlayerPos = new int[2];
        
        if(moveInput.length() == 1 && moveInput.charAt(0) == 'Z' || moveInput.charAt(0) == 'z') {
            return 2; //default case
        }
        if(moveInput.length() == 1 && moveInput.charAt(0) != 'Z' && moveInput.charAt(0) != 'z') {
            return 0;
        }
        
        // Convert letter and column to corresponding board cell
        newPlayerPos[0] = moveInput.charAt(0) - 'A';
        newPlayerPos[1] = moveInput.charAt(1) - '1';
        
        // Check if move is within the limits of the board
        if (newPlayerPos[0] > board.rows - 1 || newPlayerPos[1] > board.columns - 1) {
            return 0;
        }
        // Check if move exists in player's moveOptions
        if (!playerMoveExists(newPlayerPos)) {
            // Not possible
            return 0;
        }
        BoardCell cellToGo = board.getCells(newPlayerPos[0], newPlayerPos[1]);
        // Check if cell we want to go has an obstacle
        if (cellToGo.getElement(0) instanceof Obstacle) {
            return 0;
        }
        // Check if cell we want to go has a ghost
        if (cellToGo.getElement(0) instanceof Ghost) {
            return -1;
        }
        // Move is possible.
        // Find player's position
        int[] oldPlayerPos = board.playerPosition();
        
        // Copy first element of new cell
        BoardElement food = board.checkFood(newPlayerPos[0], newPlayerPos[1]);
        
        // Check food
        switch (food) {
            case Vegetable vegetable -> energy += ((Vegetable) food).eaten();
            case Fish fish -> energy += ((Fish) food).eaten();
            case Meat meat -> energy += ((Meat) food).eaten();
            case null, default -> {
            }
        }
        // We need to move our player in new cell
        // Copy our player in first element of new cell with depleted energy
        energy--;
        if (energy == 0) {
            return -1;
        }
        // Add energy to list
        energies.add(energy);
        Player player = new Player(newPlayerPos[0], newPlayerPos[1], energy);
        playerMovesList = player.moveOptions(board);
        board.setFirstElement(newPlayerPos[0], newPlayerPos[1], player);
        // Player has visited new cell
        board.cells[newPlayerPos[0]][newPlayerPos[1]].setHasVisited(true);
        
        // Check if player has visited all cells
        if(hasVisitedAll(board)) {
            gameOver = true;
            System.out.println("\nYOU WON");
            return 2;
        }
        // ADD move to list of player's made moves
        // Convert old and new position to user input
        String oldPosition = board.rowToLetter(oldPlayerPos[0]) + (board.columnToNumber(oldPlayerPos[1]));
        String newPosition = board.rowToLetter(newPlayerPos[0]) + (board.columnToNumber(newPlayerPos[1]));
        
        // Add positions to final player moves
        finalPlayerMoves.add(oldPosition);
        finalPlayerMoves.add(newPosition);
        
        // Fix player's old cell
        // Delete copy of player in old cell
        board.setFirstElement(oldPlayerPos[0], oldPlayerPos[1], null);
        return 1;
    }

    // Find if move input exists in player's move options
    private static boolean playerMoveExists(int[] pos) {
        for (int[] move : playerMovesList) {
            if (Arrays.equals(move, pos)) {
                return true;
            }
        }
        return false;
    }

    // Ghosts' movement-------------------------------------------------------------
    public static int moveGhosts(Board board) {
        int[] oldGhostPos = new int[2];
        
        // List of moved Ghosts
        List<int[]> movedGhosts = new ArrayList<>();
        
        //For each ghost
        for (int ghostNumber = 0; ghostNumber < ghostsMovesList.size(); ghostNumber++) {
            
            // Find ghost's position
            oldGhostPos = board.ghostPosition(movedGhosts);
           
            // Find player's position
            int[] playerPos = board.playerPosition();
            
            // List of cells with the shortest path distance from player
            List<DijkstraCell> cellsShortestPath = board.shortestPath(playerPos);
            
            // List of Dijkstra cells to remove
            List<DijkstraCell> cellsToRemove = new ArrayList<>();
           
            // Find cells in cellsShortestPath list that are also moveOptions of ghost
            // Get list of moveOptions of i-th ghost
            List<int[]> ghostMovesList = ghostsMovesList.get(ghostNumber);
           
            // Iterate through each Dijkstra cell
            for (DijkstraCell cell : cellsShortestPath) {
                boolean matchFound = false;
                //Iterate through list of moves of i-th ghost
                for (int[] ghostMoves : ghostMovesList) {
                    // If Dijkstra cell is in ghost's move options stop loop
                    if (cell.row == ghostMoves[0] && cell.column == ghostMoves[1]) {
                        matchFound = true;
                        break;
                    }
                }
                // If Dijkstra cell not in moves remove it
                if (!matchFound) {
                    cellsToRemove.add(cell);
                }
            }
            //Remove cells form Dijkstra list
            cellsShortestPath.removeAll(cellsToRemove);
            
            // Now find the Dijkstra cells with the
            // smallest distance and remove the others
            removeNonSmallestDistanceCells(cellsShortestPath);
            
            // Move Ghost to the first smallestPath cell that does not have another ghost
            for (int i=0; i<cellsShortestPath.size(); i++) {
                int[] newGhostPos = new int[2];
                newGhostPos[0] = cellsShortestPath.get(i).row;
                newGhostPos[1] = cellsShortestPath.get(i).column;
                // If position has another ghost go to the next shortest path cell
                if (board.getElement(newGhostPos[0], newGhostPos[1], 0) instanceof Ghost) {

                    // Check if no available moves
                    if (i == cellsShortestPath.size()-1){

                        // No available moves(Add position in final
                        // two times two show ghost didn't move
                        // Convert position first to user input
                        String oldPosition = board.rowToLetter(oldGhostPos[0]) + (board.columnToNumber(oldGhostPos[1]));

                        finalGhostMoves.add(oldPosition);
                        finalGhostMoves.add(oldPosition);

                        // Add ghost to moved ghosts
                        movedGhosts.add(oldGhostPos);
                    }
                    continue;
                }
                // If position has player gameOver
                if (board.getElement(newGhostPos[0], newGhostPos[1], 0) instanceof Player) {
                    System.out.println("\nYOU LOST");
                    return -1;
                }
                // No ghost in new position, so we move ghost there
                // Copy first element of new cell to second element in new cell.
                board.setSecondElement(newGhostPos[0], newGhostPos[1], board.getElement(newGhostPos[0], newGhostPos[1], 0));
                
                // Copy ghost in first element of new cell
                Ghost ghost = new Ghost(newGhostPos[0], newGhostPos[1]);
                
                // Add ghost to moved list
                movedGhosts.add(newGhostPos);
               
                // Remove ghostMoves from list(Always the first as they shift after removal)
                ghostsMovesList.remove(ghostNumber);
                ghostsMovesList.add(ghostNumber, ghost.moveOptions(board));
                board.setFirstElement(newGhostPos[0], newGhostPos[1], ghost);
                
                // Convert old and new position to user input
                String oldPosition = board.rowToLetter(oldGhostPos[0]) + (board.columnToNumber(oldGhostPos[1]));
                String newPosition = board.rowToLetter(newGhostPos[0]) + (board.columnToNumber(newGhostPos[1]));

                // Add final ghosts' moves
                finalGhostMoves.add(oldPosition);
                finalGhostMoves.add(newPosition);

                // Fix ghost's old cell
                // Copy second element in first element of old cell
                board.setFirstElement(oldGhostPos[0], oldGhostPos[1], board.getElement(oldGhostPos[0], oldGhostPos[1], 1));
                break;
            }
        }
        // Copy Board in list
        BoardsGameList.add(new Board(board));
        return 1;
    }

    // Keeps cells from the shortest distance array with the smallest distance
    public static void removeNonSmallestDistanceCells(List<DijkstraCell> cellsShortestPath) {
        // Find the smallest distance among all cells
        if (cellsShortestPath.isEmpty()){
            return;
        }
        int smallestDistance = cellsShortestPath.getFirst().distance;
        for (DijkstraCell cell : cellsShortestPath) {
            if (cell.distance < smallestDistance) {
                smallestDistance = cell.distance;
            }
        }
        // Remove all cells that don't have the smallest distance
        // iterator is not progressing when we remove(i+1 cell shifts to i)
        for (int i = 0; i < cellsShortestPath.size(); ) {
            if (cellsShortestPath.get(i).distance != smallestDistance) {
                cellsShortestPath.remove(i);
            } else {
                i++;
            }
        }
    }

    // Check if player has visited all cells
    public static boolean hasVisitedAll (Board board) {
        for (int i = 0; i < board.rows; i++) {
            for (int j = 0; j < board.columns; j++) {
                if (!board.cells[i][j].getHasVisited()) {
                   return false;
                }
            }
        }
        return true;
    }
}