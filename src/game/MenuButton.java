package game;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {

    /** Subclass of JButton for the menu buttons, reducing the amount of repeated code for each individual button. **/

    public MenuButton(String text, int x, int y, int w, int h) {
        super(text); // Uses the superclass
        setFocusable(false); // Removes text outline when button pressed
        setRolloverEnabled(false); // Removes hover highlight
        setBackground(new Color(196,204,209)); // Changes the button colour
        setBounds(x, y, w, h); // Sets the button position and dimensions
    }
}
