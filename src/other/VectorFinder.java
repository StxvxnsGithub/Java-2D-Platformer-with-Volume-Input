package other;

import game.GameView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VectorFinder implements MouseListener {

    /** This is just a quick development tool that I made to output
     * coordinates to enable easier placement of game objects.
     * This is not needed to play the game. **/

    private GameView view;

    public VectorFinder(GameView v){
        view = v;
    }

    @Override
    public void mouseClicked(MouseEvent e){
        System.out.println(view.viewToWorld(e.getPoint())); // Prints coordinates of a mouseclick
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
