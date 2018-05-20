/* Piece.java
 *
 * Name: Preston West
 * Section: MCIS5103
 * Student ID: 909556994
 *
 */

package tictactoe;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Preston West
 */
public class Piece extends JButton
{    
    public final int i;
    public final int j;
    public State state;
    private final String xImageName;
    private final String oImageName;
    
    public Piece(int i, int j, String xImageName, String oImageName)
    {
        this.i = i;
        this.j = j;
        state = State.NULL;
        this.xImageName = xImageName;
        this.oImageName = oImageName;
    }
    
    public void click(boolean isComputer)
    {
        BufferedImage image;
        try
        {
            if (!isComputer)
            {
                image = ImageIO.read(getClass().getResource(xImageName));
                state = State.X;
            }
            else
            {
                image = ImageIO.read(getClass().getResource(oImageName));
                state = State.O;
            }
        }
        catch (IOException ex)
        {
            image = null;
        }
        
        ImageIcon icon = new ImageIcon(image);
        setIcon(icon);
        setDisabledIcon(icon);
        setEnabled(false);
    }
    
    public boolean equals(Piece other)
    {
        return this.i == other.i && this.j == other.j;
    }
}
