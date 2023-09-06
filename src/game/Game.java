package game;

// Importing libraries

import java.awt.*;
import javax.swing.*;

public class Game {

    /** Initialises the game by loading key scripts, the menu, relevant UIs, and is also responsible for handling
     * the transitioning between game levels. **/

    // Declaration of local variables to enable usage beyond the constructor
    private PlayerKeyController playerKeyController;
    private PlayerMicController playerMicController;
    private GameLevel level;
    private GameView view;
    private JPanel mainPanel, endPanel, sliderPanel;
    private JLayeredPane layeredPane;
    private JCheckBox microphoneCheck, microphoneMute;
    private MenuButton levelOneButton, levelTwoButton, levelThreeButton;
    private boolean unlockedLevelTwo = false, unlockedLevelThree = false;

    public Game() {
        level = new MenuLevel(this); // Creates the game's world

        view = new GameView(level, 1152, 648); // Creates the gameview with a 16:9 window size
        level.addStepListener(new PlayerTracker(level, view, level.getPlayer())); // Used for post step actions

        // Creates an input controller for the player created in the gameworld constructor
        playerKeyController = new PlayerKeyController(view, level.getPlayer());
        playerMicController = new PlayerMicController(level, level.getPlayer());

        view.addMouseListener(new GiveFocus(view)); // Focuses the game when clicked on, requiring the mouse

        // JPanel for the UI components of the main menu
        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, 1152, 648);;
//        mainPanel.setBackground(new Color(255,0,0,255));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        int centreX = 576;
        // Creation of the menu buttons uses the MenuButton class to reduce repeated code
        levelOneButton = new MenuButton("Level One",centreX-120,260,240,35);
        levelTwoButton = new MenuButton("Locked",centreX-120,330,240,35);
        levelThreeButton = new MenuButton("Locked",centreX-120,400,240,35);

        // Lambda functions to easily add action listeners to the menu buttons
        levelOneButton.addActionListener(e -> loadLevelOne());
        levelTwoButton.addActionListener(e -> loadLevelTwo());
        levelThreeButton.addActionListener(e -> loadLevelThree());

        // Checkbox to toggle microphone input
        microphoneCheck = new JCheckBox("Check to enable volume control of player");
        microphoneCheck.setFont(new Font(microphoneCheck.getFont().getName(), Font.PLAIN, 18));
        microphoneCheck.setHorizontalAlignment(JLabel.CENTER);
        microphoneCheck.setForeground(new Color(255,255,255));
        microphoneCheck.setBounds(centreX-250,500,500,50);
        microphoneCheck.setFocusable(false);

        // Control description
        JLabel controlLabel = new JLabel("Talk = Move, Shout = Jump");
        controlLabel.setFont(new Font(controlLabel.getFont().getName(), Font.PLAIN, 12));
        controlLabel.setForeground(new Color(255,255,255));
        controlLabel.setHorizontalAlignment(JLabel.CENTER);
        controlLabel.setBounds(centreX-250,530,500,50);

        // Adds main menu components to the relevant panel
        mainPanel.add(levelOneButton);
        mainPanel.add(levelTwoButton);
        mainPanel.add(levelThreeButton);
        mainPanel.add(microphoneCheck);
        mainPanel.add(controlLabel);

        // A panel for slider-related components
        sliderPanel = new JPanel();
        sliderPanel.setBounds(0, 0, 1152, 648);;
        sliderPanel.setOpaque(false);
        sliderPanel.setLayout(null);

        // Creates a slider to allow the mic sensitivity to be adjusted
        JSlider sensSlider = new JSlider(SwingConstants.VERTICAL,90,110,100);
        sensSlider.setBounds(10,324-150,100,300);
        sensSlider.addChangeListener( e -> playerMicController.updateSens(sensSlider.getValue()));

        // Label to describe the slider's function
        JLabel sensLabel = new JLabel("Sensitivity");
        sensLabel.setFont(new Font(sensLabel.getFont().getName(), Font.PLAIN, 12));
        sensLabel.setForeground(new Color(255,255,255));
        sensLabel.setHorizontalAlignment(JLabel.CENTER);
        sensLabel.setBounds(10,455,100,50);

        // Checkbox to mute microphone
        microphoneMute = new JCheckBox("Mute Mic");
        microphoneMute.setFont(new Font(microphoneMute.getFont().getName(), Font.PLAIN, 12));
        microphoneMute.setHorizontalAlignment(JLabel.CENTER);
        microphoneMute.setForeground(new Color(255,255,255));
        microphoneMute.setBounds(10,480,100,50);
        microphoneMute.setFocusable(false);

        microphoneMute.addActionListener(e -> toggleMic());

        // Adds components to slider panel
        sliderPanel.add(sensSlider);
        sliderPanel.add(sensLabel);
        sliderPanel.add(microphoneMute);

        // Layered pane allows panels to overlay others
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1152, 648);
        view.setBounds(0, 0, 1152, 648);

        layeredPane.add(view, Integer.valueOf(0));
        layeredPane.add(mainPanel, Integer.valueOf(1));
        layeredPane.add(sliderPanel, Integer.valueOf(2));

        final JFrame frame = new JFrame("Steven's Game");  // Creates and names the game's window
        frame.add(layeredPane);

        // MacOS and Windows require different frame sizes
        String os = System.getProperty("os.name"); // Obtains the OS' name
        if (os.startsWith("Mac")) {
            frame.setSize(new Dimension(1152, 670)); // MacOS frame size
        } else {
            frame.setSize(new Dimension(1168, 687)); // Windows
        }

        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stops the game when the window is closed
        frame.setLocationByPlatform(true);
        frame.setResizable(false); // Prevents the window from being resized by the user
//        frame.pack();
        frame.setVisible(true);

        // Vector Coordinate Finder
//        view.addMouseListener(new other.VectorFinder(view));

        // Debug Viewer
//        JFrame debugView = new DebugViewer(world, 500, 648);

        level.start(); // Starts the gameworld once setup execution finished
    }

    public void end(String resultText) {
        // Unlocks the next level if the previous one is completed
        if (level instanceof GameLevel1 && resultText == "Level Completed!") {
            unlockedLevelTwo = true;
            levelTwoButton.setText("Level Two");
        } else if (level instanceof GameLevel2 && resultText == "Level Completed!") {
            unlockedLevelThree = true;
            levelThreeButton.setText("Level Three");
        }

        if (microphoneCheck.isSelected()) {                 // Checks if the microphone has been toggled
            playerMicController.stopMicrophone();           // Stops microphone input
            level.removeStepListener(playerMicController);  // Removes the step listener
        } else {                                            // When the microphone has not been toggled
            view.removeKeyListener(playerKeyController);    // Removes the key listener
        }

        // Creates the panel for the end menu
        endPanel = new JPanel();
        endPanel.setBounds(0, 0, 1152, 648);
        endPanel.setOpaque(false);
        endPanel.setLayout(null);

        // Creates the label to display the result
        JLabel resultLabel = new JLabel(resultText);
        resultLabel.setFont(new Font(resultLabel.getFont().getName(), Font.PLAIN, 36));
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setBounds(576-150,200,300,50);

        // Creates the button to return to the main menu
        MenuButton menuButton = new MenuButton("Menu",576-120,350,240,35);
        menuButton.addActionListener(e -> loadMenuLevel());

        // Adds the relevant components to the end menu
        endPanel.add(resultLabel);
        endPanel.add(menuButton);

        // Adds to the layered pane to be displayed
        layeredPane.add(endPanel, Integer.valueOf(2));
    }

    private void changeLevel() {
        level.addStepListener(new PlayerTracker(level, view, level.getPlayer())); // Readds the player tracker
        view.setWorld(level); // Updates the gameworld used by the gameview
        view.newLevel(level); // Updates the gamelevel used by the gameview
    }

    private void startPlayerControl() {
        if (microphoneCheck.isSelected()) {                     // Checks if the microphone has been toggled
            playerMicController.setPlayer(level.getPlayer());   // Updates to the new player object
            level.addStepListener(playerMicController);         // Adds step listener to the new level
            playerMicController.startMicrophone();              // Begins microphone usage
        } else {                                                // When the microphone has not been toggled
            playerKeyController.setPlayer(level.getPlayer());   // Updates to the new player object
            view.addKeyListener(playerKeyController);           // Adds the key listener for use
            layeredPane.remove(sliderPanel);                    // Removes the sensitivity slider as it's not needed
        }
    }

    private void loadMenuLevel() {
        level.stop();                                           // Halts the level
        level.stopMusic();                                      // Stops the current music
        level = new MenuLevel(this);                      // Loads the menu level

        changeLevel();
        layeredPane.remove(endPanel);                           // Removes the end panel as no longer needed
        layeredPane.remove(sliderPanel);                        // Removes slider panel if any exists to prevent dupe
        layeredPane.add(sliderPanel, Integer.valueOf(2));    // Adds the sensitivity slider
        layeredPane.add(mainPanel, Integer.valueOf(1));      // Adds the main menu
        level.start();                                          // Starts the level as it's now ready
    }

    private void loadLevelOne() {
        level.stop();                       // Halts the level
        level.stopMusic();                  // Stops the current music
        level = new GameLevel1(this);       // Loads level one

        changeLevel();
        startPlayerControl();               // Function call for the relevant listeners to be added
        layeredPane.remove(mainPanel);      // Removes the main menu
        level.start();                      // Starts the level as it's now ready
    }

    private void loadLevelTwo() {
        if (unlockedLevelTwo) {
            level.stop();                       // Halts the level
            level.stopMusic();                  // Stops the current music
            level = new GameLevel2(this);       // Loads level two

            changeLevel();
            startPlayerControl();               // Function call for the relevant listeners to be added
            layeredPane.remove(mainPanel);      // Removes the main menu
            level.start();                      // Starts the level as it's now ready
        }
    }

    private void loadLevelThree() {
        if (unlockedLevelThree) {
            level.stop();                       // Halts the level
            level.stopMusic();                  // Stops the current music
            level = new GameLevel3(this);       // Loads level two

            changeLevel();
            startPlayerControl();               // Function call for the relevant listeners to be added
            layeredPane.remove(mainPanel);      // Removes the main menu
            level.start();                      // Starts the level as it's now ready
        }
    }

    // Toggles the microphone between being on or off
    private void toggleMic() {
        // Only toggles if not in the main menu as microphone not on yet in the menu
        if (microphoneMute.isSelected() && !(level instanceof MenuLevel)) {
            playerMicController.stopMicrophone();
        } else if (!(level instanceof MenuLevel)){
            playerMicController.startMicrophone();
        }
    }

    // Fetches the state of the microphone check
    public boolean getMicChecked() {
        return microphoneCheck.isSelected();
    }

    // Returns the view object
    public GameView getView() {
        return view;
    }

    public static void main(String[] args) {
        new Game(); // Entry point
    }
}