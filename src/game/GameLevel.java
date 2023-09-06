package game;

// Importing libraries
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public abstract class GameLevel extends World {

    /** GameLevel is an abstract parent class that contains shared resources for individual game levels.
     * GameLevel is never instantiated, as it is an abstract class, but its subclasses are.
     * Methods for creating the parallax effect, creating platforms, starting music, ending music, and
     * ending the level can all be found here. **/

    protected Game game; // Game declared for use outside of constructor
    protected Player player; // Player declared for use outside of constructor
    protected Platform leftBoundary, endBoundary; // Boundaries declared for use outside of constructor
    protected String timeOfDay; // Identifies which background theme to load
    protected Platform menuPlatform;
    protected String musicPath;
    protected SoundClip levelMusic;
    private boolean levelEnded = false; // Boolean for whether the level has been completed
    private static final HashMap<Integer, Float> parallaxCoefficients = new HashMap<Integer, Float>() {{
        put(1,0f); put(2,0.55f); put(3,0.8f); put(4,0.9f);
    }}; // Hashmap to assign the correct degree of parallax effect to each layer

    private Parallax[] parallaxes = new Parallax[8]; // Declares the array to contain parallax layers

    public GameLevel(Game game) {
        this.game = game; // Initialises the game object that created this world

        // Creates the starting platform and boundaries as objects of the Platform class
        Platform startingPlatform = new Platform(this, 14f,1f,-9f,-16f,new Color(72, 81, 108));
        leftBoundary = new Platform(this, 1f,30f,-16f,0f,new Color(0,0,0,0));

        // Creates an object of the player class and assigns its reference to the local variable
        player = new Player(this, new Vec2(-10f, -13f));
        LandingCollisionListener lcl = new LandingCollisionListener(player); // Creates the handler to detect landing
        player.addCollisionListener(lcl); // Adds the listener to the player's hitbox
    }

    protected void createParallax() {
        int parallaxLayer = 1; // Identifies the layer currently being created
        for (int i = 0; i < 8; i++) { // Loops 8 times for each parallax layer
            parallaxes[i] = new Parallax(this, timeOfDay, parallaxLayer, // Identifies which image to load
                    parallaxCoefficients.get(parallaxLayer),i % 2); // Hashmap supplies parallaxCoefficient
            // Every created layer alternates between being in set 0 or set 1
            if (i % 2 == 1) { parallaxLayer++; } // Increments every other loop for 2 parallax objects per layer
        }
    }

    protected void createPlatforms(float[][] platformData, Color platformColour) {
        for (float[] p : platformData) { // Creates a platform for every entry in the platformData array
            new Platform(this, p[0],p[1],p[2],p[3],platformColour); // (Future reference of platforms not needed)
        }
    }

    public void startMusic(double volume) {
        if (this instanceof MenuLevel || (!game.getMicChecked())) { // Checks if is the menu or if mic input not chosen
            try {
                levelMusic = new SoundClip(musicPath);  // Loads the sound file
                levelMusic.setVolume(volume);
                levelMusic.loop();                      // Sets music to loop
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(e);
            }
        }
    }

    public void stopMusic() {
        if (levelMusic != null) {   // Checks if there is music to stop
            levelMusic.stop();      // Stops the music
        }
    }

    public void endLevel(boolean completed) {
        if (!levelEnded) { // Boolean check to ensure that this the method's contents are not executed multiple times
            levelEnded = true; // Sets the levelCompleted boolean to true

            player.move(0f);  // Stops the player from moving
            player.updateAnim("idle"); // Sets the player animation to idle

            if (completed) { // Checks if the level was passed or failed
                game.end("Level Completed!");   // Passes the relevant end text
                try {
                    stopMusic(); // Stops previous level music
                    levelMusic = new SoundClip("data/audio/TakeAChance.mp3");  // Loads the win music
                    levelMusic.setVolume(0.2d); // Adjusts volume
                    levelMusic.play(); // Plays the audio once without looping
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    System.out.println(e);
                }
            } else {
                game.end("No More Lives");      // Passes the relevant end text
                player.destroy();                         // If failed, destroys the player as no more lives
                try {
                    stopMusic(); // Stops previous level music
                    levelMusic = new SoundClip("data/audio/Mellowtron.mp3");  // Loads the loss file
                    levelMusic.setVolume(0.4d); // Adjusts volume
                    levelMusic.play(); // Plays the audio once without looping
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public boolean isLevelEnded() {
        return levelEnded;
    } // Returns whether level is completed

    public float getProgress() {
        // First line obtains how far the player is from the left boundary
        return Math.abs((player.getPosition().x-leftBoundary.getPosition().x)
                /(endBoundary.getPosition().x-leftBoundary.getPosition().x));
    } // Divided by the distance between boundaries to obtain percentage of progress to the end boundary

    public Player getPlayer(){
        return player;
    } // Returns the reference to the player object

    public Parallax[] getParallaxes() {
        return parallaxes;
    } // Returns the references to the parallax objects

    public Platform getMenuPlatform() {
        return menuPlatform;
    } // Returns the menuPlatform

    public Game getGame() {
        return game;
    } // Returns the game object

    public String getTimeOfDay() {
        return timeOfDay;
    } // Returns the time of day for this level
}