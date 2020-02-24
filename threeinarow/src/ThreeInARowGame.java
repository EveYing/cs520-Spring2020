import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

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
    public JFrame gui = new JFrame("Three in a Row");
    public ThreeInARowBlock[][] blocksData = new ThreeInARowBlock[N][N];
    public JButton[][] blocks = new JButton[N][N];
    public JButton reset = new JButton("Reset");
    public JTextArea playerturn = new JTextArea();
    /**
     * The current player taking their turn
     */
    public String player = "X";
    public int movesLeft = N * N;

    /**
     * Starts a new game in the GUI.
     */
    public static void main(String[] args) {
        ThreeInARowGame game = new ThreeInARowGame();
        game.gui.setVisible(true);
    }

    /**
     * Creates a new game initializing the GUI.
     */
    public ThreeInARowGame() {
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(new Dimension(500, 350));
        gui.setResizable(true);

        JPanel gamePanel = new JPanel(new FlowLayout());
        JPanel game = new JPanel(new GridLayout(N, N));
        gamePanel.add(game, BorderLayout.CENTER);

        JPanel options = new JPanel(new FlowLayout());
        options.add(reset);
        JPanel messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.white);

        gui.add(gamePanel, BorderLayout.NORTH);
        gui.add(options, BorderLayout.CENTER);
        gui.add(messages, BorderLayout.SOUTH);

        messages.add(playerturn);
        playerturn.setText("Player 1 to play 'X'");

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // Initialize a JButton for each cell of the 3x3 game board.
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocksData[row][column] = new ThreeInARowBlock(this);
                // The last row contains the legal moves
                blocksData[row][column].setContents("");
                blocksData[row][column].setIsLegalMove(row == N-1);
                blocks[row][column] = new JButton();
                blocks[row][column].setPreferredSize(new Dimension(75, 75));
                updateBlock(row, column);
                game.add(blocks[row][column]);
                blocks[row][column].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        move((JButton) e.getSource());
                    }
                });
            }
        }
    }

    /**
     * Moves the current player into the given block.
     *
     * @param block The block to be moved to by the current player
     */
	protected void move(JButton block) {
		movesLeft--;

		updateBlock(block);
		String winner = checkWinner(block);

        if (movesLeft % 2 == 1) {
            playerturn.setText("'X': Player 1");
			player = "X";
        } else {
            playerturn.setText("'O': Player 2");
			player = "O";
        }

		if (winner.equals("X")) {
			playerturn.setText("Player 1 wins!");
        } else if (winner.equals("O")) {
            playerturn.setText("Player 2 wins!");
        } else if (movesLeft == 0) {
            playerturn.setText(GAME_END_NOWINNER);
        }
	}

	protected String checkWinner(JButton block){
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
    protected void updateBlock(JButton block) {
		int row = -1;
		int column = -1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (block == blocks[i][j]){
					row = i;
					column = j;
				}
			}
		}
        blocksData[row][column].setContents(player);
        blocks[row][column].setText(blocksData[row][column].getContents());
        blocks[row][column].setEnabled(false);
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
            blocks[row][column].setEnabled(true);
        }
    }
    
    protected void updateBlock(int row, int column) {
        blocks[row][column].setText(blocksData[row][column].getContents());
        blocks[row][column].setEnabled(blocksData[row][column].getIsLegalMove());
    }

    /**
     * Ends the game disallowing further player turns.
     */
    public void endGame() {
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocks[row][column].setEnabled(false);
            }
        }
    }

    /**
     * Resets the game to be able to start playing again.
     */
    public void resetGame() {
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocks[row][column].setEnabled(row == N-1);
                blocksData[row][column].reset();
                // Enable the bottom row
                blocksData[row][column].setIsLegalMove(row == N-1);
                blocks[row][column].setText("");
            }
        }
        player = "X";
        movesLeft = N * N;
        playerturn.setText("Player 1 to play 'X'");
    }
}
