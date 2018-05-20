/* ControlPanel.java
 *
 * Name: Preston West
 * Section: MCIS5103
 * Student ID: 909556994
 *
 */

package tictactoe;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 *
 * @author Preston West
 */
public final class ControlPanel extends JPanel
{
    public ControlPanel(Board board)
    {
        setLayout(new BorderLayout());
        
        JButton reset = new JButton("Reset");
        reset.addActionListener(board);
        
        add(reset, BorderLayout.CENTER);
    }
}
