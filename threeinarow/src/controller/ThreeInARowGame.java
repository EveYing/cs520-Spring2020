package controller;

import view.ThreeInARowView;
import model.ThreeInARowBlock;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;


/**
 * Java implementation of the 3 in a row game, using the Swing framework.
 * <p>
 * This quick-and-dirty implementation violates a number of software engineering
 * principles and needs a thorough overhaul to improve readability,
 * extensibility, and testability.
 */
public class ThreeInARowGame {
    public static final String GAME_END_NOWINNER = "Game ends in a draw";

    public final int N = 3; /* Set Board size */
    public ThreeInARowBlock[][] blocksData = new ThreeInARowBlock[N][N];

    /**
     * The current player taking their turn
     */
    public String player = "X";
    public int movesLeft = N * N;
    private ThreeInARowView view;

    /**
     * Starts a new game in the GUI.
     */
    public static void main(String[] args) {
        ThreeInARowGame game = new ThreeInARowGame();
    }

    /**
     * Creates a new game initializing the GUI.
     */
    public ThreeInARowGame() {
        view = new ThreeInARowView(this);
        // Initialize a JButton for each cell of the 3x3 game board.
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocksData[row][column] = new ThreeInARowBlock(this);
                // The last row contains the legal moves
                blocksData[row][column].setContents("");
                blocksData[row][column].setIsLegalMove(row == N-1);
                view.setButtonText(row, column, blocksData[row][column].getContents());
                view.enableButton(row, column, blocksData[row][column].getIsLegalMove());
            }
        }
        view.setVisible(true);
    }

    /**
     * Moves the current player into the given block.
     *
     * @param block The block to be moved to by the current player
     */
	public void move(int row, int col) {
		movesLeft--;
		updateBlock(row, col);
		String winner = checkWinner();

        if (movesLeft % 2 == 1) {
            view.setTopText("'X': Player 1");
			player = "X";
        } else {
            view.setTopText("'O': Player 2");
			player = "O";
        }

		if (winner.equals("X")) {
			view.setTopText("Player 1 wins!");
            endGame();
        } else if (winner.equals("O")) {
            view.setTopText("Player 2 wins!");
            endGame();
        } else if (movesLeft == 0) {
            view.setTopText(GAME_END_NOWINNER);
        }
	}

	protected String checkWinner(){
		// check if rows match
		for (int i = 0; i < N; i++) {
			boolean ifWin = true;
			for (int j = 1; j < N; j++) {
				if (!blocksData[i][j].getContents().equals(blocksData[i][j-1].getContents())) {
					ifWin = false;
					break;
				}
			}
			if (ifWin) {
				return blocksData[i][0].getContents();
			}
		}
		// check if column match
		for (int j = 0; j < N; j++) {
			boolean ifWin = true;
			for (int i = 1; i < N; i++) {
				if (!blocksData[i][j].getContents().equals(blocksData[i-1][j].getContents())) {
					ifWin = false;
					break;
				}
			}
			if (ifWin) {
				return blocksData[0][j].getContents();
			}
		}
		// check if diagnol match
        boolean ifWin = true;
        for (int i = 1; i < N; i++) {
			if (!blocksData[i][i].getContents().equals(blocksData[i-1][i-1].getContents())) {
				ifWin = false;
				break;
			}
		}
        if (ifWin) {
            return blocksData[0][0].getContents();
        }
        for (int i = 1; i < N; i++) {
			if (!blocksData[i][N-i-1].getContents().equals(blocksData[i-1][N-i].getContents())) {
				return "";
			}
		}
        return blocksData[N-1][0].getContents();
	}

    /**
     * Find the block Position and Updates the block at the given row and column
     * after one of the player's moves.
     *
     * @param row    The row that contains the block
     * @param column The column that contains the block
     */
    protected void updateBlock(int row, int column) {
        blocksData[row][column].setContents(player);
        view.setButtonText(row, column, blocksData[row][column].getContents());
        view.enableButton(row, column, false);
        enableIfEmpty(row-1, column);
        enableIfEmpty(row+1, column);
        enableIfEmpty(row, column-1);
        enableIfEmpty(row, column+1);        
    }
    
    private void enableIfEmpty(int row, int column) {
        if (row < 0 || column < 0 || row >= N || column >= N)
            return;
        if (blocksData[row][column].getContents().length() == 0) {
            blocksData[row][column].setIsLegalMove(true);
            view.enableButton(row, column, true);
        }
    }

    /**
     * Ends the game disallowing further player turns.
     */
    public void endGame() {
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                view.enableButton(row, column, false);
            }
        }
    }

    /**
     * Resets the game to be able to start playing again.
     */
    public void resetGame() {
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocksData[row][column].reset();
                // Enable the bottom row
                blocksData[row][column].setIsLegalMove(row == N-1);
                view.setButtonText(row, column, "");
                view.enableButton(row, column, row == N-1);
            }
        }
        player = "X";
        movesLeft = N * N;
        view.setTopText("Player 1 to play 'X'");
    }
}
