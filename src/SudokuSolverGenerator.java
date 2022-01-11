import java.util.Scanner;

public class SudokuSolverGenerator {

    private final static int GRID_LENGTH = 9;
    private final static int randomInt = (int)(Math.random()*8+1);

    public static void main(String[] args){

        int[][]zerosSudokuBoard = new int[GRID_LENGTH][GRID_LENGTH];

        //Subsection Row Variables
        int[][] sub1r = new int[3][9];
        int[][] sub2r = new int[3][9];
        int[][] sub3r = new int[3][9];

        //Subsection Column Variables
        int[][] sub1c = new int[9][3];
        int[][] sub2c = new int[9][3];
        int[][] sub3c = new int[9][3];


        Scanner userPath = new Scanner(System.in);

        int userBoardChoice;
        int answer;
        int userArrayValueInput;
        int[][] UserInputedArray = new int[GRID_LENGTH][GRID_LENGTH];


        System.out.println("Press '1' if you want to input your own board");
        System.out.println("Press '2' if you want to try a random board");

        userBoardChoice = userPath.nextInt();

        //If '1' then takes inputs for each element in sudoku board
        if(userBoardChoice == 1){
            for(int r = 0; r < GRID_LENGTH; r++){
                for(int c = 0; c < GRID_LENGTH; c++){
                    System.out.println("What is the " + (c+1) + " element of the " + (r+1) + " row");
                    userArrayValueInput = userPath.nextInt();
                    UserInputedArray[r][c] = userArrayValueInput;
                }
            }
            displaySudokuBoard(UserInputedArray);
            System.out.println("Press '3' if you want the solution");
            answer = userPath.nextInt();

            //If '3' then solves sudoku board or displays 'no solution' if the sudoku board is not solvable
            if(answer == 3){
                System.out.println();
                if(boardSolvable(UserInputedArray)){
                    System.out.println("      Solved Sudoku Board");
                    System.out.println();
                    displaySudokuBoard(UserInputedArray);
                }
                else{
                    System.out.println("      There is no Solution");
                }
                System.out.println();
            }
        }
        //If '2' then generates a random solvable sudoku board
        else if(userBoardChoice == 2){
            System.out.println();
            System.out.println("      Random Sudoku Board");
            System.out.println();

            generateSudokuBoard(zerosSudokuBoard, sub1r, sub2r, sub3r, sub1c, sub2c, sub3c);

            System.out.println();

            System.out.println("Press '3' if you want the solution");
            answer = userPath.nextInt();

            //If '3' displays solution to sudoku board
            if(answer == 3){
                boardSolvable(zerosSudokuBoard);
                System.out.println();
                System.out.println("      Here is the Solution!");
                System.out.println();
                displaySudokuBoard(zerosSudokuBoard);
            }
        }
    }

    private static void displaySudokuBoard(int[][] sudokuBoard){
        for(int r = 0; r < GRID_LENGTH; r++){
            if(r % 3 == 0 && r > 1)
                System.out.println("_______     _______     _______");
            for(int c = 0; c < GRID_LENGTH; c++){
                if(c % 3 == 0 && c > 1)
                    System.out.print("|  ");
                System.out.print(sudokuBoard[r][c] + "  ");
            }
            System.out.println();
        }
    }

    private static boolean validRow(int[][] sudokuBoard,int row, int trialValue){
        for(int i = 0; i < GRID_LENGTH; i++) {
            if(sudokuBoard[row][i] == trialValue) {
                return false;
            }
        }
        return true;
    }

    private static boolean validCol(int[][] sudokuBoard,int col, int trialValue){
        for(int i = 0; i < GRID_LENGTH; i++) {
            if(sudokuBoard[i][col] == trialValue) {
                return false;
            }
        }
        return true;
    }

    private static boolean validBox(int[][] sudokuBoard, int row, int col, int trialValue){
        int topLeftOfBoxRow = row - row % 3;
        int topLeftOfBoxCol = col - col % 3;

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++) {
                if (sudokuBoard[topLeftOfBoxRow+x][topLeftOfBoxCol+y] == trialValue){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validPosition(int[][]sudokuBoard, int row, int col, int trialValue){
        return  validRow(sudokuBoard,row, trialValue) &&
                validCol(sudokuBoard,col, trialValue) &&
                validBox(sudokuBoard,row,col, trialValue);
    }

    /**
     * solves a sudoku board using backtracking
     * Steps
     * Loops through each element of 2d array
     * If 0 then it places a valid number 1-9
     * If it runs out of valid numbers to place it backtracks
     * until a solution is found
     * */
    private static boolean boardSolvable(int[][]sudokuBoard){
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                if(sudokuBoard[r][c] == 0) {
                    for (int t = 1; t < GRID_LENGTH + 1; t++) {
                        if(Math.random()*1 < 1) {
                            if (validPosition(sudokuBoard, r, c, t)) {
                                sudokuBoard[r][c] = t;
                                if (boardSolvable(sudokuBoard)) {
                                    return true;
                                } else {
                                    sudokuBoard[r][c] = 0;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * calls methods to display an unsolved sudoku board
     * sub refers to subsection and r and c refer to row and column
     * */
    private static void generateSudokuBoard(int[][]zerosSudokuBoard, int[][] sub1r, int[][] sub2r, int[][] sub3r, int[][] sub1c, int[][] sub2c, int[][] sub3c){
        boardSolvable(zerosSudokuBoard);
        groupRows(zerosSudokuBoard, sub1r, sub2r, sub3r);
        swapGroupRows(sub1r, sub2r, sub3r);
        rowsBackToSudokuBoard(zerosSudokuBoard, sub1r, sub2r, sub3r);
        groupColumns(zerosSudokuBoard, sub1c, sub2c, sub3c);
        swapGroupColumns(sub1c, sub2c, sub3c);
        columnsBackToSudokuBoard(zerosSudokuBoard, sub1c, sub2c, sub3c);
        swapNumbers(zerosSudokuBoard);
        removeNumbers(zerosSudokuBoard);
        displaySudokuBoard(zerosSudokuBoard);
    }

    /**
     * randomly removes values in sudoku board
     * */
    private static void removeNumbers(int[][]zerosSudokuBoard){
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                if(Math.random() * 3 < 1){
                    zerosSudokuBoard[r][c] = 0;
                }
            }
        }
    }

    /**
     * Creates two randomly assigned variables 1-9
     * If they are not equal it swaps all of its occurences in the sudoku board with each other
     * */
    private static void swapNumbers(int[][]zerosSudokuBoard){
        int[][] result = new int[9][9];
        for(int i = 0; i < 10; i++) {
            int random1 = (int) (Math.random() * 8 + 1);
            int random2 = (int) (Math.random() * 8 + 1);
            for (int r = 0; r < GRID_LENGTH; r++) {
                for (int c = 0; c < GRID_LENGTH; c++) {
                    if (zerosSudokuBoard[r][c] == random1 && random1 != random2) {
                        result[r][c] = random2;
                        zerosSudokuBoard[r][c] = result[r][c];
                    }
                    else if (zerosSudokuBoard[r][c] == random2 && random1 != random2) {
                        result[r][c] = random1;
                        zerosSudokuBoard[r][c] = result[r][c];
                    }
                }
            }
        }
    }

    /**
     * creates 2d array for each row-subsection of the sudoku board
     * in total, three 2d arrays of size 3x9
     **/
    private static void groupRows(int[][]zerosSudokuBoard, int[][] sub1r, int[][] sub2r, int[][] sub3r){
        //Group rows 1-3
        for(int r = 0; r < GRID_LENGTH-2*(GRID_LENGTH/3); r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                int t = zerosSudokuBoard[r][c];
                sub1r[r][c] = t;
            }
        }
        //Group rows 4-6
        for(int r = 3; r < GRID_LENGTH-(GRID_LENGTH/3); r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                int t = zerosSudokuBoard[r][c];
                sub2r[r-3][c] = t;
            }
        }
        //Group rows 7-9
        for(int r = 6; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                int t = zerosSudokuBoard[r][c];
                sub3r[r-6][c] = t;
            }
        }
    }

    /**
     * Randomly swaps the sudoku board row-subsections
     * */
    private static void swapGroupRows(int[][] sub1r, int[][] sub2r, int[][] sub3r) {
        int randomNumber = ((int) (Math.random() * 3 + 1));
        for(int i = 0; i < sub1r.length; i++) {
            for(int c = 0; c < GRID_LENGTH; c++) {
                if(randomNumber == 1){
                    int temp = sub1r[i][c];
                    sub1r[i][c] = sub2r[i][c];
                    sub2r[i][c] = temp;
                }
                else if(randomNumber == 2){
                    int temp = sub1r[i][c];
                    sub1r[i][c] = sub3r[i][c];
                    sub3r[i][c] = temp;
                }
                else{
                    int temp = sub2r[i][c];
                    sub2r[i][c] = sub3r[i][c];
                    sub3r[i][c] = temp;
                }
            }
        }
    }

    /**
     * combines the 2d array for each row subsection (3x9) together to form a 9x9 array
     * */
    private static void rowsBackToSudokuBoard(int[][]zerosSudokuBoard, int[][] sub1r, int[][] sub2r, int[][] sub3r){

        int[][]result = new int[sub1r.length+ sub2r.length+sub3r.length][];
        System.arraycopy(sub1r, 0, result, 0, sub1r.length);
        System.arraycopy(sub2r, 0, result, sub2r.length, sub2r.length);
        System.arraycopy(sub3r, 0, result, sub1r.length + sub2r.length, sub3r.length);
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                int t = result[r][c];
                zerosSudokuBoard[r][c] = t;
            }
        }
    }

    /**
     * creates 2d array for each column-subsection of the sudoku board
     * in total, three 2d arrays of size 9x3
     **/
    private static void groupColumns(int[][]zerosSudokuBoard, int[][] sub1c, int[][] sub2c, int[][] sub3c){
        //Group columns 1-3
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH/3; c++){
                int t = zerosSudokuBoard[r][c];
                sub1c[r][c] = t;
            }
        }
        //Group columns 4-6
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 3; c < GRID_LENGTH-(GRID_LENGTH/3); c++){
                int t = zerosSudokuBoard[r][c];
                sub2c[r][c-3] = t;
            }
        }
        //Group columns 7-9
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 6; c < GRID_LENGTH; c++){
                int t = zerosSudokuBoard[r][c];
                sub3c[r][c-6] = t;
            }
        }
    }

    /**
     * Randomly swaps the sudoku board column-subsections
     * */
    private static void swapGroupColumns(int[][] sub1c, int[][] sub2c, int[][] sub3c) {
        int randomNumber = ((int) (Math.random() * 3 + 1));
        for(int i = 0; i < sub1c.length; i++) {
            for(int c = 0; c < GRID_LENGTH/3; c++) {
                if(randomNumber == 1){
                    int temp = sub1c[i][c];
                    sub1c[i][c] = sub2c[i][c];
                    sub2c[i][c] = temp;
                }
                else if(randomNumber == 2){
                    int temp = sub1c[i][c];
                    sub1c[i][c] = sub3c[i][c];
                    sub3c[i][c] = temp;
                }
                else{
                    int temp = sub2c[i][c];
                    sub2c[i][c] = sub3c[i][c];
                    sub3c[i][c] = temp;
                }
            }
        }
    }

    /**
     * combines the 2d array for each row subsection (9x3) together to form a 9x9 array
     * */
    private static void columnsBackToSudokuBoard(int[][]zerosSudokuBoard, int[][] sub1c, int[][] sub2c, int[][] sub3c){

        int[][]result = new int[9][9];
        //Group columns 1-3
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH/3; c++){
                int t = sub1c[r][c];
                result[r][c] = t;
            }
        }
        //Group columns 4-6
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH/3; c++){
                int t = sub2c[r][c];
                result[r][c+3] = t;
            }
        }
        //        Group columns 7-9
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH/3; c++){
                int t = sub3c[r][c];
                result[r][c+6] = t;
            }
        }
        for(int r = 0; r < GRID_LENGTH; r++){
            for(int c = 0; c < GRID_LENGTH; c++){
                int t = result[r][c];
                zerosSudokuBoard[r][c] = t;
            }
        }
    }
}





