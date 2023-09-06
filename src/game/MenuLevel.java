package game;

// Importing libraries

import city.cs.engine.SoundClip;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MenuLevel extends GameLevel {

    /**
     * MenuLevel hosts all the relevant data for the main menu. Whilst not a level of the game, it utilises the
     * methods of the parent class GameLevel for many features such as music playing and parallax creation
     */

    public MenuLevel(Game game) {
        super(game);
        timeOfDay = "dawn"; // Sets the time of day for the current game level
        this.musicPath = "data/audio/PastSadness.mp3"; // Defines the music to be used
        startMusic(1d); // Uses the method of the parent class to start music


        // Instantiates a platform that moves with the player via PlayerTracker
        menuPlatform = new Platform(this, 50f,1f,-9f,-16f,new Color(72, 81, 108));

        player.removeAllCollisionListeners();   // Collision listeners are not needed on the main menu
        player.move(5f);                 // Player is set to run for the main menu
        player.updateAnim("run");               // Animation updated accordingly

        createParallax();
    }
}