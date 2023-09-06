package game;

// Importing libraries
import org.jbox2d.common.Vec2;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class FallingBall extends Ball implements ActionListener {

    /** FallingBall is a child of Ball where it will routinely drops and attempts to stop the player.
     * Utilises ActionListener for the timer of routine drops. **/

    private float xPos, yPos; // Declares the coordinates for later usage

    public FallingBall(GameLevel level, float r, float xPos, float yPos, float dens, float res, float fric, Color rgb, int mainDelay, int iniDelay) {
        super(level, r, xPos, yPos, dens, res, fric, rgb); // Uses the superclass' constructor
        this.xPos = xPos; // Sets the x-coordinate to that the object is being created at
        this.yPos = yPos; // Sets the y-coordinate to that the object is being created at

        Timer timer = new Timer(mainDelay,this); // Creates a timer with a 4-second delay
        timer.setInitialDelay(iniDelay); // Starts with an initially longer delay to allow loading
        timer.start(); // Starts the timer
    }

    public void actionPerformed(ActionEvent actionEvent) { // Executes when the timer finishes
        setLinearVelocity(new Vec2(0,0)); // Stops the ball's velocity gained from falling
        setPosition(new Vec2(xPos, yPos)); // Sets the ball's position back to its origin
        setLinearVelocity(new Vec2(0,0)); // Stops the ball's velocity gained from falling
    }
}
