package tictactoe;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Preston West
 */
public final class Board extends JPanel implements ActionListener {
    private boolean isComputer;
    private final int BOARD_SIZE = 3;
    private final int ICON_SIZE = 200;
    final Piece[][] pieces;
    private final Piece[][] winningComboSet;
    private Computer player2;

    public Board() {
        isComputer = false;

        setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));

        // Used to set the width and height of each state
        int pixelSize = ICON_SIZE * BOARD_SIZE;
        setPreferredSize(new Dimension(pixelSize, pixelSize));

        pieces = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece piece = pieces[i][j] = new Piece(
                        i, j, "/assets/redX.png", "/assets/blueO.png"
                );

                // This action listener handles logic for both player1 and player2 (computer)
                piece.addActionListener(this);
                add(piece);
            }
        }

        // The number of winning combinations of size 'BOARD_SIZE'
        // This value is obtained by rows + columns + 2 diagnols
        // Since rows = columns, it's equal to 2(BOARD_SIZE) + 2
        int setSize = 2 * (BOARD_SIZE + 1);
        winningComboSet = new Piece[setSize][BOARD_SIZE];
        fillWinningCombos();

        player2 = new Computer(pieces, winningComboSet);
    }

    private void fillWinningCombos() {
        // Rows
        for (int i = 0; i < pieces.length; i++) {
            System.arraycopy(pieces[i], 0, winningComboSet[i], 0, pieces[i].length);
        }
        int count = pieces.length;

        // Columns
        for (int j = 0; j < pieces[0].length; j++) {
            Piece[] combo = winningComboSet[count++];
            for (int i = 0; i < pieces.length; i++) {
                combo[i] = pieces[i][j];
            }
        }

        // First diagnol
        Piece[] combo = winningComboSet[count++];
        for (int i = 0; i < pieces.length; i++) {
            combo[i] = pieces[i][i];
        }

        // Second diagnol
        combo = winningComboSet[count];
        for (int i = 0; i < pieces.length; i++) {
            combo[i] = pieces[pieces.length - (i + 1)][i];
        }
    }

    private boolean checkGameState() {
        State winner = checkCombos();

        // Game has ended with one winner
        if (winner != State.NULL) {
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
            String message;
            if (winner == State.X) {
                message = "Player 1 wins!";
            }
            else {
                message = "Player 2 wins!";
            }

            if (!isComputer) {
                toggleNullPieces();
            }

            JOptionPane.showMessageDialog(frame, message);
            return true;
        }

        boolean noNullSpaces = true;
        rowLoop:
        for (Piece[] row : pieces) {
            for (Piece piece : row) {
                State state = piece.state;
                if (state == State.NULL) {
                    noNullSpaces = false;
                    break rowLoop;
                }
            }
        }

        // Game has ended with no winner
        if (noNullSpaces) {
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
            String message = "Nobody wins!";
            JOptionPane.showMessageDialog(frame, message);
            return true;
        }

        return false;
    }

    private State checkCombos() {
        State winner = State.NULL;
        for (Piece[] combo : winningComboSet) {
            winner = combo[0].state;
            for (Piece piece : combo) {
                if (piece.state == State.NULL || piece.state != winner) {
                    winner = State.NULL;
                    break;
                }
            }

            if (winner != State.NULL) {
                break;
            }
        }

        return winner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("reset")) {
            reset();
        }
        else {
            handleClick(e);
        }
    }

    private void handleClick(ActionEvent e) {
        Piece piece = (Piece)e.getSource();
        piece.click(isComputer);

        boolean isFinished = checkGameState();

        if (isFinished) {
            return;
        }

        if (!isComputer) {
            toggleNullPieces();
            isComputer = true;
            new Thread(player2).start();
        }
        else {
            toggleNullPieces();
            isComputer = false;
        }
    }

    private void reset() {
        for (Piece[] row : pieces) {
            for (Piece piece : row) {
                piece.state = State.NULL;
                piece.setIcon(null);
                piece.setEnabled(true);
            }
        }

        isComputer = false;
        player2 = new Computer(pieces, winningComboSet);
    }

    private void toggleNullPieces() {
        for (Piece[] row : pieces) {
            for (Piece piece : row) {
                if (piece.state != State.NULL) {
                    continue;
                }

                piece.setEnabled(!piece.isEnabled());
            }
        }
    }
}
