package game;

// Importing libraries
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

public class EndCollisionListener implements CollisionListener {

    /** EncCollisionListener handles collisions against the end boundary.
     * Intended to execute level-ending code when the player reaches it. **/

    private GameLevel level; // Declares local variable to allow usage of the gameworld within objects of this class

    public EndCollisionListener(GameLevel level) {
        this.level = level;
    } // Initialises the world attribute

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody() instanceof Player) { // Checks to see if the player has touched it
            level.endLevel(true); // Calls the method and identifies a completed level
        }
    }
}
