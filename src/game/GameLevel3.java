package game;

// Importing libraries

import city.cs.engine.BoxShape;
import city.cs.engine.DynamicBody;
import org.jbox2d.common.Vec2;

import java.awt.*;

public class GameLevel3 extends GameLevel {

    /**
     * GameLevel3 hosts all the relevant data for the third level of the game.
     */

    private float[][] platformData = {
            {20.5f,1f,34.5f,-16f},
            {4f,0.5f,26f,-9f},
            {5f,0.5f,37f,-3f},
            {4f,0.5f,51f,-3f},
            {4f,4f,51f,4f},
            {7f,1f,68f,-16f},
            {10f,2f,92f,-16f},
            {6f,0.5f,105f,-8f},
            {6f,0.5f,115f,-2f},
            {14f,2f,147.5f,-16f}};
    // Platform data is formatted: {width, height, xPos, yPos}
    private Color platformColour = new Color(67, 63, 98);

    public GameLevel3(Game game) {
        super(game);
        timeOfDay = "dusk"; // Sets the time of day for the current game level
        this.musicPath = "data/audio/LoneHarvest.mp3"; // Defines the music to be used
        startMusic(1.2d); // Uses the method of the parent class to start music

        createPlatforms(platformData, platformColour); // Uses the method of the parent class to create platforms

        // End boundary is the hitbox to detect if the player has reached the end
        endBoundary = new Platform(this, 1f,30f,147.5f,0f,new Color(0,0,0,0));

        EndCollisionListener ecl = new EndCollisionListener(this); // Creates collision handler for the end
        endBoundary.addCollisionListener(ecl); // Adds the listener to the end boundary

        // Creates an interactable dynamic body for the player to push
        DynamicBody slidingBlock = new DynamicBody(this, new BoxShape(6f, 1f));
        slidingBlock.setPosition(new Vec2(43f,-1.5f));
        slidingBlock.setFillColor(new Color(91, 100, 129)); // Sets the platform's colour to the rgb colour provided
        slidingBlock.setLineColor(new Color(0,0,0,0)); // Removes the platform's outline

        BallCollisionListener bcl = new BallCollisionListener(player); // Handles objects hitting the ball
        // Creates an instance of the Ball's FallingBall subclass, which is a threat to the player
        FallingBall fallingBall1 = new FallingBall(this,1f,58f,0f, 20f, 0.2f, 1f, new Color(105,98,135), 2000, 7000);
        fallingBall1.addCollisionListener(bcl); // Adds the listener to the ball's hitbox

        // Creates an instance of the Ball's FallingBall subclass, which is a threat to the player
        FallingBall fallingBall2 = new FallingBall(this,1f,78,-4f, 20f, 0.2f, 1f, new Color(105,98,135), 4000, 7000);
        fallingBall2.addCollisionListener(bcl); // Adds the listener to the ball's hitbox

        createParallax();
    }
}