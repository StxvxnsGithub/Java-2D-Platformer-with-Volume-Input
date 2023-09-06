package game;

// Importing libraries
import city.cs.engine.*;
import javax.swing.*;
import java.awt.*;

public class GameView extends UserView {

    /** GameView is a child of UserView which provides the view the user looks through/at.
     * It is also used to draw the background, the timer, the number of lives remaining, and the progress bar. **/

    private GameLevel level; // Local variable declared to allow usage beyond constructor

    // Declares the local variables necessary for the stopwatch. Long is used for
    // its greater capacity as milliseconds since unix time is rather large.
    private long startTime, millisecondsElapsed, decisecondsElapsed, secondsElapsed, minutesElapsed;
    private String imagePath = "data/bgdawn/static.png";

    public GameView(GameLevel level, int width, int height) {
        super(level, width, height); // Uses the superclass' constructor
        this.level = level; // Initialises local world variable

        startTime = System.currentTimeMillis(); // Obtains the start time of the level attempt
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        // Draws on the background
        g.drawImage(new ImageIcon(imagePath).getImage(), 0, 0, this);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        if (!(level instanceof MenuLevel)) { // Only draws the following when it is not the main menu
            if (!level.isLevelEnded()) { // Only executes if the level has not been completed
                millisecondsElapsed = System.currentTimeMillis() - startTime; // Calculates milliseconds since start
                decisecondsElapsed = (millisecondsElapsed / 100) % 10; // Obtains number of deciseconds towards second
                secondsElapsed = (millisecondsElapsed / 1000) % 60; // Obtains seconds towards a minute
                minutesElapsed = millisecondsElapsed / 60000; // Obtains number of minutes that have passed since start

                g.scale(2f, 2f); // Increases the scale for larger text
                g.setColor(new Color(255, 255, 255)); // Sets font colour to white

                if (secondsElapsed < 10) { // If less than 10 seconds, will need to draw the 0 between secs and mins
                    g.drawString(minutesElapsed + ":0" + secondsElapsed + "." + decisecondsElapsed, 15, 25);
                } else {
                    g.drawString(minutesElapsed + ":" + secondsElapsed + "." + decisecondsElapsed, 15, 25);
                }
            } else {
                g.scale(2f, 2f); // Increases the scale for larger text
                g.setColor(new Color(255, 255, 255)); // Sets font colour to white

                // Draws on the final time elapsed of the level attempt
                if (secondsElapsed < 10) { // If less than 10 seconds, will need to draw the 0 between secs and mins
                    g.drawString(minutesElapsed + ":0" + secondsElapsed + "." + decisecondsElapsed, 15, 25);
                } else {
                    g.drawString(minutesElapsed + ":" + secondsElapsed + "." + decisecondsElapsed, 15, 25);
                }
            }
            g.scale(0.5f, 0.5f); // Halves the scale for a smaller font
            // Displays the number of lives remaining
            g.drawString("Lives remaining: " + level.getPlayer().getLives(), 30, 70);

            // Draws the progress bar
            g.drawImage(new ImageIcon("data/progressBar.png").getImage(), 1152 / 2 - 100, 50, this);
            int progress = Math.round(940 + (1310 - 940) * level.getProgress()); // Calculates the progress in pixels
            g.scale(0.5f, 0.5f); // Halves size for a small player image
            g.drawImage(new ImageIcon("data/player/run.gif").getImage(), progress, 70, this);
            // Draws the player's running gif to show progress made
        }
    }

    public void newLevel(GameLevel level) {
        this.level = level; // Updates the level attribute
        imagePath = "data/bg" + level.getTimeOfDay() + "/static.png"; // Updates the background image to be used
        startTime = System.currentTimeMillis(); // Restarts the timer
    }
}
