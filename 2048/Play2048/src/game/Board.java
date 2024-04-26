package game;

import java.util.ArrayList;

/**
 * 2048 Board
 * Methods to complete:
 * updateOpenSpaces(), addRandomTile(), swipeLeft(), mergeLeft(),
 * transpose(), flipRows(), makeMove(char letter)
 * 
 * @author Kal Pandit
 * @author Ishaan Ivaturi
 **/
public class Board {
    private int[][] gameBoard;               // the game board array
    private ArrayList<BoardSpot> openSpaces; // the ArrayList of open spots: board cells without numbers.

    /**
     * Zero-argument Constructor: initializes a 4x4 game board.
     **/
    public Board() {
        gameBoard = new int[4][4];
        openSpaces = new ArrayList<>();
    }

    /**
     * One-argument Constructor: initializes a game board based on a given array.
     * 
     * @param board the board array with values to be passed through
     **/
    public Board ( int[][] board ) {
        gameBoard = new int[board.length][board[0].length];
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                gameBoard[r][c] = board[r][c];
            }
        }
        openSpaces = new ArrayList<>();
    }

    /**
     * 1. Initializes the instance variable openSpaces (open board spaces) with an empty array.
     * 2. Adds open spots to openSpaces (the ArrayList of open BoardSpots).
     * 
     * Note: A spot (i, j) is open when gameBoard[i][j] = 0.
     * 
     * Assume that gameBoard has been initialized.
     **/
    public void updateOpenSpaces() {

        openSpaces = new ArrayList<BoardSpot>(); // boardspot is type

        for(int i=0;i<gameBoard.length;i++){

            for(int j =0;j<gameBoard[i].length;j++){

                if(gameBoard[i][j] == 0){
                
                BoardSpot emptyBoardSpot = new BoardSpot(i,j);
                openSpaces.add(emptyBoardSpot); //boardspot method

                }
            }
        }
        
        
    }

    /**
     * Adds a random tile to an open spot with a 90% chance of a 2 value and a 10% chance of a 4 value.
     * Requires separate uses of StdRandom.uniform() to find a random open space and determine probability of a 4 or 2 tile.
     * 
     * 1. Select a tile t by picking a random open space from openSpaces
     * 2. Pick a value v by picking a double from 0 to 1 (not inclusive of 1); < .1 means the tile is a 4, otherwise 2
     * 3. Update the tile t on gameBoard with the value v
     * 
     * Note: On the driver updateOpenStapes() is called before this method to ensure that openSpaces is up to date.
     **/
    public void addRandomTile() {
        
        if(openSpaces.isEmpty()){
            return;
        }

        int openSpaceAmount = openSpaces.size();
        int randomOpenSpace = (int)(StdRandom.uniform(0,openSpaceAmount));
        // int randomOpenSpace = (int)StdRandom.uniform() * openSpaceAmount;
        // random open space from openSpaces 
        BoardSpot randomTile = openSpaces.get(randomOpenSpace);
        //
        double v = (double)StdRandom.uniform(0,1);
        int t = 0;

            if(v<0.1){
            t = 2;
        }
            else{
            t = 4;
        }
        gameBoard[randomTile.getRow()][randomTile.getCol()] = t;
    }

    /**
     * Swipes the entire board left, shifting all nonzero tiles as far left as possible.
     * Maintains the same number and order of tiles. 
     * After swiping left, no zero tiles should be in between nonzero tiles. 
     * (ex: 0 4 0 4 becomes 4 4 0 0).
     **/
    public void swipeLeft() {
    // 2 0 2 -  
    // - - 2 - 
    // 2 2 - - 
    // - 4 - 2
    // - - 
        for(int row=0;row<4;row++){
            int misplacedZeroes = 0;

            while ( misplacedZeroes == 0){
                for (int col = 3; col !=0; col--){
                    if (col != 0){
                        if (gameBoard[row][col] != 0 && gameBoard[row][col-1] == 0){
                            gameBoard[row][col-1] = gameBoard[row][col];
                            gameBoard[row][col] = 0;
                        }
                    }
                }
                //Checking Misplaced Zeroes
                for (int col = 0; col < 4; col ++){
                    if (col != 3){
                        if (gameBoard[row][col] == 0 && gameBoard[row][col+1] != 0){
                            misplacedZeroes = 0;
                            break;
                        }
                    }
                    misplacedZeroes = 1;
                    // how to figure out when to set misplacedZeroes = 1 -> this means we break out of the while loop
                    // misplacedZeroes = 1; <- code I had before.
                }
            }
        }
    
    }


    /**
     * Find and merge all identical left pairs in the board. Ex: "2 2 2 2" will become "2 0 2 0".
     * The leftmost value takes on double its own value, and the rightmost empties and becomes 0.
     **/
    public void mergeLeft() {

    for(int i=0;i<gameBoard.length;i++){
        for(int j=0;j<3;j++){
            
            // if(gameBoard[i][j] + gameBoard[i][j+1] > 0){
            if(gameBoard[i][j] == gameBoard[i][j+1]){
                gameBoard[i][j] = gameBoard[i][j] * 2;
                gameBoard[i][j+1] = 0;
                j++;

                // }
            }
        }
    }       
}
    

    /**
     * Rotates 90 degrees clockwise by taking the transpose of the board and then reversing rows. 
     * (complete transpose and flipRows).
     * Provided method. Do not edit.
     **/
    public void rotateBoard() {
        transpose();
        flipRows();
    }

    /**
     * Updates the instance variable gameBoard to be its transpose. 
     * Transposing flips the board along its main diagonal (top left to bottom right).
     * 
     * To transpose the gameBoard interchange rows and columns.
     * Col 1 becomes Row 1, Col 2 becomes Row 2, etc.
     * 
     **/
    public void transpose() {
    
    int[][] tempArray = new int[4][4];
    for(int i=0;i<gameBoard.length;i++){
        for(int j=0;j<gameBoard.length;j++){
        tempArray[i][j] = gameBoard[j][i];
    
            
        }
    }
    gameBoard = tempArray;
    }

    /**
     * Updates the instance variable gameBoard to reverse its rows.
     * 
     * Reverses all rows. Columns 1, 2, 3, and 4 become 4, 3, 2, and 1.
     * 
     **/
    public void flipRows() {

        for(int i=0;i<gameBoard.length;i++){

        int firstIndex = 0;
        int lastIndex = gameBoard.length-1;

        while(firstIndex < lastIndex){

        int tempNum = gameBoard[i][firstIndex];

        //gameBoard[i][lastIndex] = tempNum;
        gameBoard[i][firstIndex] = gameBoard[i][lastIndex];
        gameBoard[i][lastIndex] = tempNum;

            firstIndex++;
            lastIndex--;
        }

        
        }
    }
        

    /**
     * Calls previous methods to make right, left, up and down moves.
     * Swipe, merge neighbors, and swipe. Rotate to achieve this goal as needed.
     * 
     * @param letter the first letter of the action to take, either 'L' for left, 'U' for up, 'R' for right, or 'D' for down
     * NOTE: if "letter" is not one of the above characters, do nothing. 
     **/
    public void makeMove(char letter) {

        
        
        //right
        if(letter == 'R'){
        rotateBoard();
        rotateBoard();
        swipeLeft();
        mergeLeft();
        swipeLeft();
        rotateBoard();
        rotateBoard();
        }

        //left
        else if(letter == 'L'){
        swipeLeft();
        mergeLeft();
        swipeLeft();
        

        }
        //up
        else if(letter == 'U'){
        rotateBoard();
        rotateBoard();
        rotateBoard();
        swipeLeft();
        mergeLeft();
        swipeLeft();
        rotateBoard();
       


        }
        //down
        else if(letter == 'D'){
        rotateBoard();
        swipeLeft();
        mergeLeft();
        swipeLeft();
        rotateBoard();
        rotateBoard();
        rotateBoard();

        }
    }
    

    /**
     * Returns true when the game is lost and no empty spaces are available. Ignored
     * when testing methods in isolation.
     * 
     * @return the status of the game -- lost or not lost
     **/
    public boolean isGameLost() {
        return openSpaces.size() == 0;
    }

    /**
     * Shows a final score when the game is lost. Do not edit.
     **/
    public int showScore() {
        int score = 0;
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                score += gameBoard[r][c];
            }
        }
        return score;
    }

    /**
     * Prints the board as integer values in the text window. Do not edit.
     **/
    public void print() {
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                String g = Integer.toString(gameBoard[r][c]);
                StdOut.print((g.equals("0")) ? "-" : g);
                for ( int o = 0; o < (5 - g.length()); o++ ) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }
    /**
     * Prints the board as integer values in the text window, with open spaces denoted by "**"". Used by TextDriver.
     **/
    public void printOpenSpaces() {
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                String g = Integer.toString(gameBoard[r][c]);
                for ( BoardSpot bs : getOpenSpaces() ) {
                    if (r == bs.getRow() && c == bs.getCol()) {
                        g = "**";
                    }
                }
                StdOut.print((g.equals("0")) ? "-" : g);
                for ( int o = 0; o < (5 - g.length()); o++ ) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }

    /**
     * Seed Constructor: Allows students to set seeds to debug random tile cases.
     * 
     * @param seed the long seed value
     **/
    public Board(long seed) {
        StdRandom.setSeed(seed);
        gameBoard = new int[4][4];
    }

    /**
     * Gets the open board spaces.
     * 
     * @return the ArrayList of BoardSpots containing open spaces
     **/
    public ArrayList<BoardSpot> getOpenSpaces() {
        return openSpaces;
    }

    /**
     * Gets the board 2D array values.
     * 
     * @return the 2D array game board
     **/
    public int[][] getBoard() {
        return gameBoard;
    }
}
