package game;

// Importing libraries
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;

public class LandingCollisionListener implements CollisionListener {

    /** Handles the updating of the player's animation when they land after jumping. **/

    private Player player; // Declares the player as a local variable for usage in the collide method
    boolean onPlatform = true; // Identifies whether the ball is making contact with the platform upon jump
    // onPlatform is necessary as the player's hitbox collides with the platform upon jumping

    public LandingCollisionListener(Player player) {
        this.player = player;
    } // Initialises the player

    @Override
    public void collide(CollisionEvent collisionEvent) {
        boolean isJumping = player.getState() == "jump" || player.getState() == "jumpr"; // Ensures player jumping
        boolean isPlatform = collisionEvent.getOtherBody() instanceof Platform; // Ensures is platform

        // onPlatform used to ensure collision that occurs when first leaving platform is ignored
        if (!onPlatform && isJumping && isPlatform &&
                player.getPosition().y-2 > collisionEvent.getOtherBody().getPosition().y) {
            // Second line of if statement ensures player is on top of platform to avoid head or
            // side hitting a platform and causing the animation to switch to a landed animation

            if (player.getState() == "jump") {          // Checks if the player is facing right
                player.updateAnim("idle");              // Hence uses right-facing/normal animation
            } else {                                    // If not facing right, was facing left
                player.updateAnim("idler");             // Else uses idler(eversed) for left-facing
            }

            // Used to correct the animation to running if movement keys still held down on impact
            if (player.getMoving() == "right") {        // Checks if player is moving right
                player.updateAnim("run");               // Hence uses right-facing/normal animation
            } else if (player.getMoving() == "left") {  // Checks if player is moving left
                player.updateAnim("runr");              // Else uses runr(eversed) to run left
            }

            onPlatform = true;  // Sets onPlatform back to true as player has landed
        } else if (isJumping && isPlatform && // Ensures player has jumped, touched a platform, and above a platform
                player.getPosition().y-2 > collisionEvent.getOtherBody().getPosition().y) {
            onPlatform = false; // Toggles onPlatform to false as the player has just jumped up from the platform
        }
    }
}
