package game;

// Importing libraries
import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.awt.*;

public class Ball extends DynamicBody {

    /** Ball inherits DyanmicBody and is designed for easy instantiation of circular movable balls. **/

    public Ball(GameLevel level, float r, float xPos, float yPos, float dens, float res, float fric, Color rgb) {
        super(level); // Uses the superclass' constructor
        SolidFixture circle = new SolidFixture(this, new CircleShape(r));
        circle.setDensity(dens); // Undergoing testing and tuning
        circle.setRestitution(res); // Undergoing testing and tuning
//        circle.setFriction(fric); // Undergoing testing and tuning

        setPosition(new Vec2(xPos, yPos)); // Sets the ball's starting position
        setFillColor(rgb); // Changes the colour of the ball
        setLineColor(new Color(0,0,0,0)); // Removes the outline
    }
}
