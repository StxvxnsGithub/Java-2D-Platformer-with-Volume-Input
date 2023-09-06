package game;

// Importing libraries
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

public class BallCollisionListener implements CollisionListener {

    /** BallCollisionListener is a collision handler for hostile balls that reset the player upon impact. **/

    private Player player; // Local variable to allow usage of the player object

    public BallCollisionListener(Player player) {
        this.player = player;
    } // Passes in the reference to the player

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if (collisionEvent.getOtherBody() instanceof Player) { // Checks to see if collided with the player
            player.moveToStart(); // Resets the player back to the start
        }
    }
}
