package com.sd.demo;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    public static void main(String[] args) {
        int rows = 8;
        int cols = 8;
        int numMines = 10;
        char[][] board = new char[rows][cols];
        boolean[][] revealed = new boolean[rows][cols];
        initializeBoard(board);
        placeMines(board, numMines);
        calculateHints(board);
        boolean gameOver = false;
        Scanner scanner = new Scanner(System.in);
        while (!gameOver) {
            printBoard(board, revealed);
            System.out.print("Enter 'r' to reveal a cell or 'q' to quit: ");
            String action = scanner.nextLine();
            if (action.equals("q")) {
                break;
            }
            if (action.equals("r")) {
                System.out.print("Enter row: ");
                int row = scanner.nextInt();
                System.out.print("Enter column: ");
                int col = scanner.nextInt();
                if (row >= 0 && row < rows && col >= 0 && col < cols) {
                    if (board[row][col] == '*') {
                        System.out.println("Game over! You hit a mine.");
                        gameOver = true;
                    } else {
                        revealCell(board, revealed, row, col);
                        if (checkWin(board, revealed, numMines)) {
                            System.out.println("Congratulations! You've cleared the board!");
                            gameOver = true;
                        }
                    }
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
                scanner.nextLine(); // Consume newline
            }
        }
        scanner.close();
    }
    // Initializes the game board with empty cells
    private static void initializeBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = ' ';
            }
        }
    }
    // Randomly places mines on the game board
    private static void placeMines(char[][] board, int numMines) {
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int row = rand.nextInt(board.length);
            int col = rand.nextInt(board[0].length);
            if (board[row][col] != '*') {
                board[row][col] = '*';
                minesPlaced++;
            }
        }
    }
    // Calculates the hints for each cell indicating the number of nearby mines
    private static void calculateHints(char[][] board) {
        // Arrays to define the neighboring cells' positions
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != '*') {
                    int count = 0;
                    // Check neighboring cells for mines
                    for (int d = 0; d < 8; d++) {
                        int newRow = i + dr[d];
                        int newCol = j + dc[d];
                        if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[i].length
                                && board[newRow][newCol] == '*') {
                            count++;
                        }
                    }
                    if (count > 0) {
                        board[i][j] = (char) ('0' + count);
                    }
                }
            }
        }
    }
    // Reveals a cell and its neighbors recursively
    private static void revealCell(char[][] board, boolean[][] revealed, int row, int col) {
        if (revealed[row][col]) {
            return;
        }
        revealed[row][col] = true;
        if (board[row][col] == ' ') {
            int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
            // Reveal neighboring cells recursively
            for (int d = 0; d < 8; d++) {
                int newRow = row + dr[d];
                int newCol = col + dc[d];
                if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[row].length) {
                    revealCell(board, revealed, newRow, newCol);
                }
            }
        }
    }
    // Checks if the player has won the game
    private static boolean checkWin(char[][] board, boolean[][] revealed, int numMines) {
        int countRevealed = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (revealed[i][j]) {
                    countRevealed++;
                }
            }
        }
        return countRevealed == (board.length * board[0].length - numMines);
    }
    // Prints the game board
    private static void printBoard(char[][] board, boolean[][] revealed) {
        System.out.println("\nMinesweeper Board:");
        System.out.print("  ");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board[i].length; j++) {
                if (revealed[i][j]) {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}