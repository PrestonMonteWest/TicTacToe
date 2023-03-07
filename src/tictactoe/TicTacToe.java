package tictactoe;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Preston West
 */
public class TicTacToe {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        Board board = new Board();
        ControlPanel control = new ControlPanel(board);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(control, BorderLayout.NORTH);
        frame.add(board, BorderLayout.SOUTH);
        frame.pack();

        // Logic for centering JFrame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xTopLeft = (screenSize.width - frame.getWidth()) / 2;
        int yTopLeft = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(xTopLeft, yTopLeft);

        frame.setVisible(true);
    }
}
