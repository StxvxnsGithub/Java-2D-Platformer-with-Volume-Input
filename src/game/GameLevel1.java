package game;

// Importing libraries
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.HashMap;

public class GameLevel1 extends GameLevel {

    /**
     * GameLevel1 hosts all the relevant data for the first level of the game.
     */

    private float[][] platformData = {
            {6f,1f,18f,-16f},
            {10f,0.5f,30f,-10},
            {14f,1f,65f,-16f}};
    // Platform data is formatted: {width, height, xPos, yPos}
    private Color platformColour = new Color(83, 93, 117);

    public GameLevel1(Game game) {
        super(game);
        timeOfDay = "dawn"; // Sets the time of day for the current game level
        this.musicPath = "data/audio/PastSadness.mp3"; // Defines the music to be used
        startMusic(1d); // Uses the method of the parent class to start music

        createPlatforms(platformData, platformColour); // Uses the method of the parent class to create platforms

        // End boundary is the hitbox to detect if the player has reached the end
        endBoundary = new Platform(this, 1f,30f,65f,0f,new Color(0,0,0,0));

        EndCollisionListener ecl = new EndCollisionListener(this); // Creates collision handler for the end
        endBoundary.addCollisionListener(ecl); // Adds the listener to the end boundary

        // Creates the dynamic body ball obstacle to serve as a movable obstruction
        Ball ball = new Ball(this,3f,36f,-7f, 20f, 0.2f, 1f, new Color(92,108,145));

        createParallax();
    }
}