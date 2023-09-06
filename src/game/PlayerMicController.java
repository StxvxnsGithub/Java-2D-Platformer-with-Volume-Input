package game;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class PlayerMicController implements StepListener {

    // Declaration of local variables to enable usage beyond the constructor
    private GameLevel level;
    private Player player;
    private Thread microphoneInputThread;
    private boolean usingMic = false;
    private float micInputVol;
    private float sensitivity = 1.0f;
    private TargetDataLine targetDataLine;

    public PlayerMicController(GameLevel level, Player player) {
        this.level = level;
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    /**
     I dislike using postStep as it lowers performance, but have to due to a bug caused by including the implementation
     within microphoneInputThread. When used within the thread, CSEngine hits a nullpointerexception that I cannot
     solve. The issue is caused when I attempt to update the player's image and hitboxes from within the thread.
     Therefore, I instead do this outside of the thread to avoid the bug.
     **/

    /**
     * Note: control via volume is a bit finicky but simply requires fine-tuning on what volume should be
     * considered a jump. Varies with microphone and ambient sounds and have not been able to test with different
     * microphones/environments. I suggest testing the volume control once or twice but playing the rest of the game
     * via keyboard.
     */

    @Override
    public void postStep(StepEvent stepEvent) {
        if (usingMic) {
            float input = (micInputVol * 2) * sensitivity; // Modifies the audio data with sensitivity
//            System.out.println("Processed audiodata: " + input + " Sensitivity: " + sensitivity);

            if (input > 110) { // Checks if the input is loud enough to jump
                player.move(5f);
                player.jump(12);
                if (player.getState() != "jump") {
                    player.updateAnim("jump");
                }
            } else if (input > 100) { // Checks if the input is loud enough to move
                float movementBoost = (input - 100) / 4;
                //            System.out.print(" " + movementBoost);
                //            player.move(2.5f + movementBoost);
                player.move(5f);
                if (player.getState() != "run" && player.getState() != "jump") {
                    player.updateAnim("run");
                }
            } else if (player.getMoving() != "still") {
                player.move(0f);
                if ((player.getState() != "run" || player.getState() != "jump")
                        && player.getState() != "idle") {
                    player.updateAnim("idle");
                }
            }
        }
    }


    /**
     The code below is used to capture audio from the microphone and obtain the volume to be used for
     player input.

     Resources used:
     **/
    // - [1] https://docs.oracle.com/javase/tutorial/sound/capturing.html
    // - [2] https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/TargetDataLine.html
    // - [3] https://youtu.be/WSyTrdjKeqQ
    // - [4] https://stackoverflow.com/questions/3899585/microphone-level-in-java
    // - [5] https://codeahoy.com/java/How-To-Stop-Threads-Safely/
    // - [6] https://stackoverflow.com/questions/4152201/calculate-decibels

    private void microphoneInput() {
        try {
            // Resources [1] and [3] were used for the following code

            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat); // Holds info for dataline
            if (!AudioSystem.isLineSupported(dataLineInfo)) { // Checks if the audio is supported
                System.out.println("Audio input is not supported.");
                return;
            }
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo); // Obtains the line to take audio data from
            targetDataLine.open(); // Prepares to receive data from the microphone
            targetDataLine.start(); // Starts capturing data from the microphone

            // A thread is used so that the while loop doesn't stop the game from performing other tasks

            microphoneInputThread = new Thread() {
                @Override
                public void run() {
                    byte buffer[] = new byte[10000]; // Buffer where audio data is stored
                    while (usingMic) {
                        int readData = targetDataLine.read(buffer, 0, buffer.length); // Writes audio data into buffer
                        // The integer readData becomes the number of bytes read [2]

                        // RMS method inspired by the stack overflow question [4]
                        long sum = 0;
                        for (int i = 0; i < readData; i++) {
                            sum += Math.pow(buffer[i], 2); // Squared and then summed up
                        }
                        float meanSquared = sum / readData; // Mean is calculated
                        float rootMeanSquared = (float) Math.sqrt(meanSquared); // Square rooted to obtain rms

                        micInputVol = rootMeanSquared;
//                        micInputVol = (float) (20 * Math.log10(rootMeanSquared)); // Converted to decibels [6]
//                        System.out.println("Microphone input: " + micInputVol);
                    }
                }
            };

            microphoneInputThread.start(); // Starts the thread
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateSens(int rawSensitivity) {
        sensitivity = rawSensitivity/100f; // Divides the sensitivity to use it as a multiplier
    }

    public void startMicrophone() {
        usingMic = true;
        microphoneInput();
    }

    public void stopMicrophone() {
        usingMic = false;
        microphoneInputThread.interrupt(); // Used to safely stop thread execution when next able to [5]
        targetDataLine.stop(); // Stops recoridng microphone input
        targetDataLine.close(); // Closes the data line as no longer needed
        micInputVol = 0f; // Resets input;
    }
}
