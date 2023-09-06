package game;

// Importing libraries
import city.cs.engine.BodyImage;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;

public class Parallax extends StaticBody {

    /** The Parallax class is used for the parallax effect seen in the game's background.
     * Each layer of the parallax effect is made up of two individual parallax objects.
     * This class instantiates them and moves them accordingly with the player. **/

    // Below local attributes are declared for usage outside the constructor
    private float width, height, currentPos;
    private float parallaxCoefficient;
    private int set; // As each layer has two identical objects, set differentiates between batches

    public Parallax(GameLevel level, String time, int layer, float parallaxCoefficient, int set) {
        super(level); // Uses the constructor of the parent class StaticBody
        this.parallaxCoefficient = parallaxCoefficient; // Initialises the parallaxCoefficient
        this.set = set; // Initialises which set the parallax object is a member of
        this.width = 57.6f; // All layer images have the same width
        this.height = 32.4f; // All layer images have the same height
        this.currentPos = 0f; // Starting position is always 0
//        this.setPosition(new Vec2(0f-(set*57.6f/2f),0f));

        String imagePath = ("data/bg"+time+"/"+layer+".png"); // Forms the file path from the information given
        addImage(new BodyImage(imagePath, height)); // Adds an image to the object from the path provided
    }

    public void updateParallax(Player player) {
        setPosition(new Vec2((currentPos + player.getPosition().x*parallaxCoefficient-set*57.6f),
                getPosition().y)); // X position of the layer is set to a fraction of the player's current position,
        // determined by the parallaxCoefficient. If in set 1, will be a full length to the left of the set 0 objects

        if (getPosition().x < player.getPosition().x+20f-width) { // Checks if the parallax object is off the left edge
            currentPos += width*2; // If so, leapfrogs the layer ahead of the other set
        } else if (getPosition().x > player.getPosition().x+20f+width) { // Checks if object is off the right edge
            currentPos -= width*2; // If so, leapfrogs the layer behind the other set
        }
    }
}
