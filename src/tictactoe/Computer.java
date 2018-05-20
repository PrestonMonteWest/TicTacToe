/* Computer.java
 *
 * Name: Preston West
 * Section: MCIS5103
 * Student ID: 909556994
 *
 */

package tictactoe;

import java.util.ArrayList;

/**
 *
 * @author Preston West
 */
public class Computer implements Runnable
{
    private final Piece[][] pieces;
    private final Piece[][] winningComboSet;
    
    // Variables for winning strategy
    private int cornerCount;
    private final int MAX_CORNERS = 3;
    private final Piece[] corners;
    
    public Computer(Piece[][] pieces, Piece[][] winningComboSet)
    {
        this.pieces = pieces;
        this.winningComboSet = winningComboSet;
        
        cornerCount = 0;
        corners = new Piece[4];
        
        int lastIndex = pieces.length - 1;
        corners[0] = pieces[0]         [0];
        corners[1] = pieces[0]         [lastIndex];
        corners[2] = pieces[lastIndex] [lastIndex];
        corners[3] = pieces[lastIndex] [0];
    }
    
    @Override
    public void run()
    {
        try
        {
            // The computer is "thinking"
            Thread.sleep(1200L);
        }
        catch (InterruptedException ex) {}
        
        if (checkWinningCombos())
        {
            return;
        }
        
        if (checkCorners())
        {
            return;
        }
        
        checkIndividualPieces();
    }
    
    private boolean checkWinningCombos()
    {
        ArrayList<Piece[]> combos = getNearlyCompleteCombos();
        if (combos.isEmpty())
        {
            return false;
        }
        
        ArrayList<Piece> xPieces = new ArrayList<>();
        ArrayList<Piece> oPieces = new ArrayList<>();
        
        // Separate almost-complete combos into X and O arrays
        combos.forEach((combo) -> {
            // To reduce the amount of code redundancy
            ArrayList<Piece> temp;
            
            Piece piece1 = combo[0];
            Piece piece2 = combo[1];
            Piece piece3 = combo[2];
            
            boolean isX = piece1.state == State.X || piece2.state == State.X
                    || piece3.state == State.X;
            
            if (isX)
            {
                temp = xPieces;
            }
            else
            {
                temp = oPieces;
            }
            
            if (piece1.state == State.NULL)
            {
                temp.add(piece1);
            }
            else if (piece2.state == State.NULL)
            {
                temp.add(piece2);
            }
            else
            {
                temp.add(piece3);
            }
        });
        
        int i = clickFirstNullPiece(oPieces);
        
        // If there are no possible O winning combos, then stop the X winning combo(s)
        if (i == -1)
        {
            clickFirstNullPiece(xPieces);
        }
        
        return true;
    }
    
    private int clickFirstNullPiece(ArrayList<Piece> pieces)
    {
        int index = 0;
        for (Piece piece : pieces)
        {
            if (piece.state == State.NULL)
            {
                for (Piece corner : corners)
                {
                    if (piece.equals(corner))
                    {
                        cornerCount++;
                        break;
                    }
                }
                piece.setEnabled(true);
                piece.doClick();
                return index;
            }
            
            index++;
        }
        
        return -1;
    }
    
    private ArrayList<Piece[]> getNearlyCompleteCombos()
    {
        int numCombos = 2 * (pieces.length + 1);
        ArrayList<Piece[]> combos = new ArrayList<>(numCombos);
        
        for (Piece[] combo : winningComboSet)
        {
            boolean isX = false;
            boolean isO = false;
            int nullCount = 0;
            for (Piece piece : combo)
            {
                if (piece.state != State.NULL)
                {
                    if (piece.state == State.X)
                    {
                        isX = true;
                    }
                    else
                    {
                        isO = true;
                    }
                    
                    continue;
                }
                
                nullCount++;
            }
            
            boolean isBoth = isX && isO;
            if (nullCount == 1 && !isBoth)
            {
                combos.add(combo);
            }
        }
        
        return combos;
    }
    
    private boolean checkCorners()
    {
        if (cornerCount >= MAX_CORNERS)
        {
            return false;
        }
        
        for (Piece corner : corners)
        {
            if (corner.state == State.NULL)
            {
                cornerCount++;
                corner.setEnabled(true);
                corner.doClick();
                return true;
            }
        }
        
        return false;
    }
    
    private void checkIndividualPieces()
    {
        for (Piece[] row : pieces)
        {
            for (Piece piece : row)
            {
                if (piece.state == State.NULL)
                {
                    piece.setEnabled(true);
                    piece.doClick();
                    return;
                }
            }
        }
    }
}
