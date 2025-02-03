import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.FontWeight;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.List;

public class AppleTreeGameOver extends Application {

    private List<ImageView> apples = new ArrayList<>();
    private List<Text> appleTexts = new ArrayList<>();
    private double[][] applePositions = {
            {330, 90}, {70, 370}, {340, 280}, {390, 210}, {150, 360},
            {200, 150}, {400, 130}, {200, 215}, {450, 240}, {450, 370},
            {240, 280}, {500, 330}, {300, 150}, {480, 160}, {240, 350},
            {100, 250}, {300, 210}, {380, 340}, {160, 280}, {530, 280}
    };
    private int totalScore = 0;
    private int numDrops = 0;
    private Label scoreLabel;
    private Label dropsLabel;
    private Pane root;
    private MediaPlayer mediaPlayer;
    private Timeline soundTimeline;
    private int poisonAppleIndex = -1;

    public AppleTreeGameOver(int score, int drops) {
        this.totalScore = score;
        this.numDrops = drops;
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();

        Image backgroundImage = new Image("skybg.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(650);
        backgroundImageView.setFitHeight(700);
        root.getChildren().add(backgroundImageView);
        
        String soundFile = "apple_drop_sound.mp3";
        Media sound = new Media(getClass().getResource(soundFile).toString());
        mediaPlayer = new MediaPlayer(sound);
        
        soundTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> {
        mediaPlayer.stop();
         }));
        
        Image treeImage = new Image("tree.png");
        ImageView treeImageView = new ImageView(treeImage);
        treeImageView.setFitWidth(650);
        treeImageView.setFitHeight(650);
        treeImageView.setTranslateX(0);
        treeImageView.setTranslateY(20);

        root.getChildren().add(treeImageView);

        scoreLabel = new Label("Score: " + totalScore);
        scoreLabel.setAlignment(Pos.TOP_RIGHT);
        scoreLabel.setTranslateX(220);
        scoreLabel.setFont(Font.font(20));
        root.getChildren().add(scoreLabel);

        dropsLabel = new Label("Drops: " + numDrops);
        dropsLabel.setAlignment(Pos.TOP_LEFT);
        dropsLabel.setTranslateX(20);
        dropsLabel.setFont(Font.font(20));
        root.getChildren().add(dropsLabel);

        // Randomly choose a poison apple
        poisonAppleIndex = (int) (Math.random() * applePositions.length);

        for (int i = 0; i < applePositions.length; i++) {
            Image appleImage = new Image("apple.png");
            ImageView appleImageView = new ImageView(appleImage);
            appleImageView.setFitWidth(35);
            appleImageView.setFitHeight(35);
            appleImageView.setTranslateX(applePositions[i][0]);
            appleImageView.setTranslateY(applePositions[i][1]);
            apples.add(appleImageView);
            root.getChildren().add(appleImageView);

            int score = new int[]{-20, -50, -100, 100, -500, 200}[(int) (Math.random() * 6)];

            Text appleText = new Text(String.valueOf(i + 1));
            appleText.setTranslateX(applePositions[i][0] + 10);
            appleText.setTranslateY(applePositions[i][1] + 27);
            appleText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            appleTexts.add(appleText);
            root.getChildren().add(appleText);

            // Add event handler for each apple
            if (i == poisonAppleIndex) {
                appleImageView.setOnMouseClicked(event -> {
                totalScore = 0;
                    endGameWithPoisonApple();
                
            });
            } else {
                appleImageView.setOnMouseClicked(event -> dropApple(event, appleImageView, appleText, score));
            }
        }

        Scene scene = new Scene(root, 650, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Apple Tree");
        primaryStage.show();
    }

    private void dropApple(MouseEvent event, ImageView appleImageView, Text appleText, int score) {
        appleImageView.setOnMouseClicked(null);
        appleText.setOnMouseClicked(null);

        apples.remove(appleImageView);

        numDrops--;
        dropsLabel.setText("Drops: " + numDrops);

        totalScore += score;
        scoreLabel.setText("Score: " + totalScore);

        if (totalScore < 0) {
            endGame();
            return;
        }

        if (numDrops == 0) {
            endGame();
            return;
        }

        Circle ground = new Circle(150, 380, 150);
        ground.setFill(null);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), appleImageView);
        TranslateTransition textTransition = new TranslateTransition(Duration.seconds(1), appleText);

        transition.setToY(550);
        textTransition.setToY(570);

        transition.setOnFinished(e -> root.getChildren().remove(appleImageView));
        textTransition.setOnFinished(e -> root.getChildren().remove(appleText));

        transition.play();
        textTransition.play();

        // Animation for points change
        Label changeLabel = new Label(score > 0 ? "+" + score : String.valueOf(score));
        changeLabel.setFont(Font.font(20));
        changeLabel.setTextFill(score > 0 ? Color.GREEN : Color.RED);
        changeLabel.setTranslateX(appleImageView.getTranslateX());
        changeLabel.setTranslateY(appleImageView.getTranslateY());
        root.getChildren().add(changeLabel);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), changeLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(e -> root.getChildren().remove(changeLabel));
        fadeTransition.play();
        
        transition.setOnFinished(e -> {
        // Play the sound effect when the animation finishes
        mediaPlayer.stop(); // Stop any previous playing instances
        mediaPlayer.play();
        
        // Create a Timeline to stop the sound after 1.5 seconds
        Timeline soundTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event1 -> {
            mediaPlayer.stop(); // Stop the sound after 1.5 seconds
        }));
        soundTimeline.play();
        
        root.getChildren().remove(appleImageView);
        });
    }

    private void endGame() 
    {
    int delayDuration = 200;
    int fallDuration = 200;

    for (int i = 0; i < apples.size(); i++) {
        ImageView apple = apples.get(i);
        Text appleText = appleTexts.get(i);
        int score = 0;
        TranslateTransition appletransition = new TranslateTransition(Duration.millis(fallDuration), apple);
        appletransition.setDelay(Duration.millis(delayDuration * i));
        appletransition.setToY(550);
        TranslateTransition texttransition = new TranslateTransition(Duration.millis(fallDuration), appleText);
        texttransition.setDelay(Duration.millis(delayDuration * i));
        texttransition.setToY(570);
        appletransition.setOnFinished(e -> root.getChildren().remove(apple));
        texttransition.setOnFinished(e -> root.getChildren().remove(appleText));
        
        appletransition.play();
        texttransition.play();
    }

    // Play background sound when all apples fall
    String backgroundSoundFile = "background_all_apple_drop_sound.mp3"; // Change this to the path of your background sound file
    Media backgroundSound = new Media(getClass().getResource(backgroundSoundFile).toString());
    MediaPlayer backgroundMediaPlayer = new MediaPlayer(backgroundSound);
    backgroundMediaPlayer.play();

    PauseTransition pauseTransition = new PauseTransition(Duration.millis(400));
    pauseTransition.setOnFinished(e -> {
        Stage endGameStage = new Stage();
        VBox endGameLayout = new VBox(20);
        endGameLayout.setAlignment(Pos.CENTER);
        endGameLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;");
    Stage currentStage = (Stage) root.getScene().getWindow(); // Get the current stage
        currentStage.close(); // Close the current stage
    });
    
    pauseTransition.setOnFinished(e -> {
    // Create a new Timeline for adding a delay before closing the stage
    Timeline delayBeforeClose = new Timeline(new KeyFrame(Duration.seconds(4), event -> {
        Stage currentStage = (Stage) root.getScene().getWindow(); // Get the current stage
        currentStage.close(); // Close the current stage
    }));
    delayBeforeClose.play(); // Start the delay before closing
    });

        
    Stage endGameStage = new Stage();
    VBox endGameLayout = new VBox(20);
    endGameLayout.setAlignment(Pos.CENTER);
    endGameLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;"); // Apply background color and padding

    Label gameOverLabel = new Label("Game Over");
    gameOverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
    gameOverLabel.setTextFill(Color.RED);

    Label scoreLabel = new Label("Your Score: " + totalScore);
     scoreLabel.setFont(Font.font("Arial", 20));

    Button replayButton = new Button("PLAY AGAIN");
    replayButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px;"); // Apply button style
    replayButton.setOnAction(e -> {
        endGameStage.close();
        GameHomepage gameWindow = new GameHomepage();
        gameWindow.start(new Stage());
    });
    
    endGameLayout.getChildren().addAll(gameOverLabel, scoreLabel, replayButton);
    Scene endGameScene = new Scene(endGameLayout, 300, 200);
    endGameStage.setScene(endGameScene);
    endGameStage.setTitle("Game Over");
    endGameStage.show();
        
    pauseTransition.play();
   }
   
    private void endGameWithPoisonApple() {
        // Set total score to 0 and end the game
        totalScore = 0;
        scoreLabel.setText("Score: " + totalScore);
        endGame();

        // Show message to the user about picking the poison apple
        Label poisonLabel = new Label("Poison apple!");
        poisonLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        poisonLabel.setTextFill(Color.RED);

        VBox endGameLayout = new VBox(20);
        endGameLayout.setAlignment(Pos.CENTER);
        endGameLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;");
        endGameLayout.getChildren().addAll(poisonLabel);
        
        Stage endGameStage = new Stage();
        Scene endGameScene = new Scene(endGameLayout, 300, 100);
        endGameStage.setScene(endGameScene);
        endGameStage.setTitle("Game Over");
        endGameStage.show();
    }
 
    
    public static void main(String[] args) {
        launch(args);
    }
}
