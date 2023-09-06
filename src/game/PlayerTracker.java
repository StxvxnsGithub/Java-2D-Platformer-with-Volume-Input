package game;

// Importing libraries
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import org.jbox2d.common.Vec2;

public class PlayerTracker implements StepListener {

    /** A StepListener subclass designed to track the player and update the background parallax effect visual.
     * Also ensures that the menu platform tracks the player when it's the menu so that the player never runs
     * off of the edge. **/

    // Declares the references to objects for usage beyond the constructor
    private GameLevel level;
    private GameView view;
    private Player player;

    public PlayerTracker(GameLevel level, GameView view, Player player) {
        this.level = level; // Initialises the world local attribute
        this.view = view; // Initialises the view local attribute
        this.player = player; // Initialises the player local attribute
    }

    public void preStep(StepEvent e) {} // Executes before physics simulation of each frame

    public void postStep(StepEvent e) { // Executes after physics simulation of each frame
        // Sets the camera's x to follow the player but with an offset
        view.setCentre(new Vec2(player.getPosition().x+20f,0));

        for (Parallax parallax : level.getParallaxes()) { // Will loop through each parallax object in parallaxes
            parallax.updateParallax(player); // Calls the parallax object's update method
        }

        if (player.getPosition().y < -18f) { // Checks if the player has fallen below the platforms
            player.moveToStart(); // Resets the player to start if they fall out of bounds
        }

        if (level instanceof MenuLevel) { // Checks if it is the menu level
            level.getMenuPlatform().setPosition(new Vec2((player.getPosition().x), level.getMenuPlatform().getPosition().y));
        } // Moves the menuPlatform with the player
    }

    public void setPlayer(Player player) {
        this.player = player;
    } // Updates the player to be tracked
}