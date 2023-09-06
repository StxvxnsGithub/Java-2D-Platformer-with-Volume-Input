package game;

// Importing libraries

import java.awt.*;

public class GameLevel2 extends GameLevel {

    /**
     * GameLevel2 hosts all the relevant data for the second level of the game.
     */

    private float[][] platformData = {
            {6f,4f,18f,-14f},
            {10f,6f,37f,-12f},
            {10f,3f,43f,-16f},
            {7f,1f,68f,-16f},
            {14f,4f,89f,-16f}};
    // Platform data is formatted: {width, height, xPos, yPos}
    private Color platformColour = new Color(91, 100, 129);

    public GameLevel2(Game game) {
        super(game);
        timeOfDay = "day"; // Sets the time of day for the current game level
        this.musicPath = "data/audio/ShoresOfAvalon.mp3"; // Defines the music to be used
        startMusic(2d); // Uses the method of the parent class to start music

        // End boundary is the hitbox to detect if the player has reached the end
        createPlatforms(platformData, platformColour); // Uses the method of the parent class to create platforms

        endBoundary = new Platform(this, 1f,30f,89,0f,new Color(0,0,0,0));

        EndCollisionListener ecl = new EndCollisionListener(this); // Creates collision handler for the end
        endBoundary.addCollisionListener(ecl); // Adds the listener to the end boundary

        BallCollisionListener bcl = new BallCollisionListener(player); // Handles objects hitting the ball
        // Creates an instance of the Ball's FallingBall subclass, which is a threat to the player
        FallingBall fallingBall1 = new FallingBall(this,1f,8.5f,0f, 20f, 0.2f, 1f, new Color(105,98,135), 4000, 7000);
        fallingBall1.addCollisionListener(bcl); // Adds the listener to the ball's hitbox

        // Creates an instance of the Ball's FallingBall subclass, which is a threat to the player
        FallingBall fallingBall2 = new FallingBall(this,1f,57f,5f, 20f, 0.2f, 1f, new Color(105,98,135), 4000, 7000);
        fallingBall2.addCollisionListener(bcl); // Adds the listener to the ball's hitbox

        createParallax();
    }
}