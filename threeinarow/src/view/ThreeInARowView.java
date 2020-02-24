package view;

import controller.ThreeInARowGame;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

interface ButtonListener {
    public void buttonClicked(int row, int col);
}

public class ThreeInARowView {
    public static final String GAME_END_NOWINNER = "Game ends in a draw";

    public final int N; /* Set Board size */
    public JFrame gui = new JFrame("Three in a Row");
    public JButton[][] blocks;
    public JButton reset = new JButton("Reset");
    public JTextArea playerturn = new JTextArea();
    private ThreeInARowGame controller = null;

    /**
     * Creates a new game initializing the GUI.
     */
    public ThreeInARowView(ThreeInARowGame controller) {
        this.controller = controller;
        this.N = controller.N;
        blocks = new JButton[N][N];
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
                controller.resetGame();
            }
        });

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton)(e.getSource());
                for (int i = 0; i < N; i ++) {
                    for (int j = 0; j < N; j ++) {
                        if (blocks[i][j] == button) {
                            controller.move(i, j);
                            return;
                        }
                    }
                }
            }
        };

        // Initialize a JButton for each cell of the 3x3 game board.
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                blocks[row][column] = new JButton();
                blocks[row][column].setPreferredSize(new Dimension(75,75));
                game.add(blocks[row][column]);
                blocks[row][column].addActionListener(listener);
            }
        }
    }


    public void setTopText(String text) {
        playerturn.setText(text);
    }

    public void enableButton(int i, int j, boolean enabled) {
        blocks[i][j].setEnabled(enabled);
    }

    public void setButtonText(int i, int j, String text) {
        blocks[i][j].setText(text);
    }

    public void setVisible(boolean visible) {
        gui.setVisible(visible);
    }
}