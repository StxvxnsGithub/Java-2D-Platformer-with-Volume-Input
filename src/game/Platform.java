package game;

// Importing libraries
import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.awt.*;

public class Platform extends StaticBody {

    /** Platform inherits from StaticBody to provide a simpler and tidier way of instantiating platforms. **/

    public Platform(GameLevel level, float w, float h, float xPos, float yPos, Color rgb) {
        super(level); // Uses the superclass' constructor
        SolidFixture platformFixture = new SolidFixture(this, new BoxShape(w, h)); // Creates hitbox
        platformFixture.setFriction(4f); // Uses friction to prevent objects from sliding

        setPosition(new Vec2(xPos, yPos)); // Sets the platform's position per parameters supplied
        setFillColor(rgb); // Sets the platform's colour to the rgb colour provided
        setLineColor(new Color(0,0,0,0)); // Removes the platform's outline
    }
}
