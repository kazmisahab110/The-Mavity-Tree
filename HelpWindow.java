import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

public class HelpWindow {
    public Scene createScene() {
        StackPane layout = new StackPane();
        String helpText = "Quick recap on how to play the game:\n\n" +
                          "• After running the program, the user comes up to the starting page.\n" +
                          "• The user presses the start game button.\n" +
                          "• The user is taken to the first spin wheel.\n" +
                          "• The user presses spin button, the spin wheel spins and assigns user with specific number of drops randomly.\n" +
                          "• The user then presses the next button.\n" +
                          "• The user comes to the next spin wheel window again the user presses the spin button, and the user is assigned with score to play the game with.\n" +
                          "• The user presses the next button.\n" +
                          "• It takes the user to the main tree window.\n" +
                          "• The user plays the game.\n" +
                          "• Now if the user picks the poison apple the game ends with user getting 0 points.\n" +
                          "• If the user manages to bring his score below zero, the user again loses the game.\n" +
                          "• Now, if the user plays the game in accordance with the normal scenario, after the user’s number of drops become zero, an animation will play, game ends and the total score is what the player earned throughout the game.\n" +
                          "• After game ends, irrespective of the scenario, the user is given the option to replay the game.";

        Label helpLabel = new Label(helpText);
        layout.getChildren().add(helpLabel);
        layout.setAlignment(helpLabel, Pos.CENTER); // Center the label in the StackPane
        Scene scene = new Scene(layout, 600, 400); // Adjust size as needed
        return scene;
    }
}
