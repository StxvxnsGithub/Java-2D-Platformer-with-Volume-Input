package game;

// Importing libraries
import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.util.HashMap;

public class Player extends Walker {

    /** Inheriting from the Walker class, Player is the physical object that the user will move about.
     Movement and animation/hitbox updates are all done here. **/

    private int lives = 4;
    private boolean hasLives = true;
    private GameLevel level;
    private Vec2 startingPos; // Local attribute that will retain the player's starting position
    private SolidFixture hitbox; // Declares the hitbox to be a local attribute for future usage
    private String state, moving; // String variables to describe the current motions of the player

    /** I have used hashmaps to make the numerous polygon coordinates and
     image file paths easier to reference, simplifying the updateAnim() method. **/

    private static final HashMap<String, PolygonShape> hitboxes = new HashMap<String, PolygonShape>() {{
        put("idle", new PolygonShape(-0.75f, -1.67f, 0.42f, -1.66f, 0.76f,-1.46f,
                0.5f, 1.26f, 0.08f, 1.48f, -0.35f, 1.25f, -1.04f, -1.49f));
        put("idler", new PolygonShape(0.75f, -1.67f, -0.42f, -1.66f, -0.76f,-1.46f,
                -0.5f, 1.26f, -0.08f, 1.48f, 0.35f, 1.25f, 1.04f, -1.49f));
        put("run", new PolygonShape(-0.78f,-1.62f, 0.64f,-1.64f, 1.12f,-1.32f,
                0.79f,1.31f, 0.36f,1.59f, -0.05f,1.33f, -1.44f,-1.2f));
        put("runr", new PolygonShape(0.78f,-1.62f, -0.64f,-1.64f, -1.12f,-1.32f,
                -0.79f,1.31f, -0.36f,1.59f, 0.05f,1.33f, 1.44f,-1.2f));
        put("jump", new PolygonShape(0.42f,-1.69f, 0.84f,-1.46f, 0.29f,1.26f,
                -0.12f,1.47f, -0.53f,1.24f, -0.4f,-1.37f, -0.08f,-1.69f));
        put("jumpr", new PolygonShape(-0.42f,-1.69f, -0.84f,-1.46f, -0.29f,1.26f,
                0.12f,1.47f, 0.53f,1.24f, 0.4f,-1.37f, 0.08f,-1.69f));
    }};

//    private static final HashMap<String, BodyImage> images = new HashMap<String, BodyImage>() {{
//        put("idle",new BodyImage("data/player/idle.gif", 5f));
//        put("idler",new BodyImage("data/player/idler.gif", 5f));
//        put("run",new BodyImage("data/player/run.gif", 5f));
//        put("runr",new BodyImage("data/player/runr.gif", 5f));
//        put("jump",new BodyImage("data/player/jump.gif", 5f));
//        put("jumpr",new BodyImage("data/player/jump.gif", 5f));
//        put("land",new BodyImage("data/player/land.gif", 5f));
//    }};

    public Player(GameLevel level, Vec2 startingPos) {
        super(level); // Uses the superclass' constructor
        this.level = level;
        this.startingPos = startingPos; // Initialises the starting vector
        state = "idle"; // Default animation state is idle
        moving = "still"; // Default motion is stationary
        hitbox = new SolidFixture(this, hitboxes.get("idle")); // Hitbox creation for the default animation

        addImage(new BodyImage("data/player/idle.gif", 5f)); // Adds the default animation
//        this.setAlwaysOutline(true); // For checking hitboxes without debug viewer

        moveToStart(); // Moves the player to the starting position
//        hitbox.setFriction(2f); // Adding friction to avoid sliding
        // Friction on the player causes lift when player pushing balls. Better to use friction on platforms only
    }

    public void moveToStart() {
        if (lives <= 0 && hasLives) { // Checks if the player has any lives remaining
            hasLives = false; // Boolean toggle to prevent endLevel executed multiple times
            level.endLevel(false); // Calls the method to end the level, passing false for failed attempt
        } else if (lives > 0) {
            setPosition(startingPos); // Moves the player to the starting coordinates
            setLinearVelocity(new Vec2(0, 0)); // Resets the player's velocity, stopping them
            lives--; // Removes a life from the player
        }
    }

    public int getLives() {
        return lives;
    } // Returns the resetCounter's value

    public void updateAnim(String anim) {
        removeAllImages(); // Removes the old animation
        addImage(new BodyImage("data/player/"+anim+".gif", 5f)); // Adds the new animation
//        addImage(images.get(anim)); // Adds the new animation from the hashmap

        hitbox.destroy(); // Destroys the hitbox of the old animation as it will need to change accordingly
        hitbox = new SolidFixture(this, hitboxes.get(anim)); // Creates a new hitbox for the new animation

        state = anim; // Updates the identifier to the current animation
    }

    public void move(float speed) {
        if (speed == 0f) {          // Checks if speed is to be set to zero/stationary
            stopWalking();          // Stops the player's movement
            moving = "still";       // Updates the motion identifier accordingly
        } else {                    // If there is a non-zero value to the speed
            startWalking(speed);    // Moves the player at the speed passed by parameter

            if (speed > 0 ) {       // If the speed is positive (player moves rightwards)
                moving = "right";   // Sets the movement identifier to moving right
            } else {
                moving = "left";    // Sets the movement ientifier to moving left if not moving right
            }
        }
    }

    // Used by the landing collision handler to identify which direction the player is heading when landing
    public String getMoving() {
        return moving;
    } // Returns the current state of the player's motion

    public String getState() {
        return state;
    } // Returns the current animation being played
}