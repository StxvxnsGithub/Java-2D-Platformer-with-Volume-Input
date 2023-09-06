package game;

// Importing libraries
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyController implements KeyListener {

    /** PlayerKeyController, using KeyListener, handles key inputs for control of the player. **/

    private GameView view; // Declaration of the view for usage beyond constructor
    private Player player; // Declaration of the player for usage beyond constructor

    public PlayerKeyController(GameView view, Player player){
        this.view = view; // Initialises the view to be the game's current view
        this.player = player; // Initialises the player being controlled
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {    // Executes when a keypress is detected
        int code = e.getKeyCode();          // Obtains the keycode of the input

        if (code == KeyEvent.VK_D) {                // Checks if the key pressed was D
            player.move(5f);                                                       // Calls the player's move method with a positive (rightwards) speed
            if (player.getState() == "jump" || player.getState() == "jumpr") {  // Checks if the player is in the air
                player.updateAnim("jump");          // Changes to the jumping right animation
            } else {                                // If not jumping, must be running
                player.updateAnim("run");           // Changes to the running right animation
            }
        } else if (code == KeyEvent.VK_A) {         // Else checks if the key pressed was A
            player.move(-5f);                // Calls the player's move method with a negative (leftwards) speed
            if (player.getState() == "jump" || player.getState() == "jumpr") {  // Checks if the player is in the air
                player.updateAnim("jumpr");         // Changes to the jumping left animation
            } else {                                // If not jumping, must be running
                player.updateAnim("runr");          // Changes to the running left animation
            }
        } else if (code == KeyEvent.VK_SPACE) {     // Else checks if the spacebar was pressed
            player.jump(12);                 // Calls the jump method inherited from the walker superclass
            if (player.getState() == "idle" || player.getState() == "run") {            // Checks if facing right
                player.updateAnim("jump");          // Changes to the jumping right animation
            } else if (player.getState() == "idler" || player.getState() == "runr") {   // Checks if facing left
                player.updateAnim("jumpr");         // Changes to the jumping left animation
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {   // Executes when a key is released
        int code = e.getKeyCode();          // Obtains the keycode of the key released

        if (code == KeyEvent.VK_D) {                // Checks if the D key was released
            player.move(0f);                 // Sets movement to 0
            if (player.getState() == "jump" || player.getState() == "jumpr") {  // Checks if player is in the air
                player.updateAnim("jump");          // Changes to the jumping right animation
            } else {                                // If not jumping, must have been moving on the platform
                player.updateAnim("idle");          // Changes to the idle right animation
            }
        } else if (code == KeyEvent.VK_A) {         // Checks if the D key was released
            player.move(0f);                 // Sets movement to 0
            if (player.getState() == "jump" || player.getState() == "jumpr") {  // Checks if player is in the air
                player.updateAnim("jumpr");         // Changes to the jumping left animation
            } else {                                // If not jumping, must have been moving on the platform
                player.updateAnim("idler");         // Changes to the idle left animation
            }
        }
    }
}