import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import java.util.Random;
import javafx.animation.AnimationTimer;


public class GameHomepage extends Application {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int NUM_PACMANS = 10;
    private static final int PACMAN_SIZE = 20;

    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: #1E1E1E;");

        // Load the background music file
        Media backgroundMusic = new Media(getClass().getResource("background_music.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(backgroundMusic);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Make it continuously play

        // Play the background music
        mediaPlayer.play();

        // Create buttons
        Button playButton = new Button("Play");
        Button exitButton = new Button("Exit");
        Button helpButton = new Button("Help");

        // Apply styles to the buttons
        String buttonStyle = "-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-padding: 10px 20px; " +
                "-fx-border-radius: 5px;";
        playButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
        helpButton.setStyle(buttonStyle);

        // Set button sizes
        playButton.setPrefSize(120, 40);
        exitButton.setPrefSize(120, 40);
        helpButton.setPrefSize(120, 40);

        double centerX = WIDTH / 2.9;
        double centerY = HEIGHT / 2.9;

        playButton.setTranslateX(centerX - playButton.getWidth() / 2);
        playButton.setTranslateY(centerY - playButton.getHeight() / 2);

        exitButton.setTranslateX(centerX - exitButton.getWidth() / 2);
        exitButton.setTranslateY(centerY - exitButton.getHeight() / 2 + 50);

        helpButton.setTranslateX(centerX - helpButton.getWidth() / 2);
        helpButton.setTranslateY(centerY - helpButton.getHeight() / 2 + 100);

        Label welcomeLabel = new Label("THE MAVITY TREE");
        welcomeLabel.setTextFill(Color.YELLOW);
        welcomeLabel.setPrefWidth(WIDTH);
        welcomeLabel.setAlignment(javafx.geometry.Pos.CENTER);
        welcomeLabel.setTranslateY(-70);
        welcomeLabel.setTranslateX(-20);
        welcomeLabel.setFont(Font.font(50));
        welcomeLabel.setFont(Font.font("Jokerman", 50));

        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(playButton, exitButton, helpButton, welcomeLabel);

        Random random = new Random();
        for (int i = 0; i < NUM_PACMANS; i++) {
            ImageView pacmanView = new ImageView(new Image("/pacman-png-18.png"));
            pacmanView.setFitWidth(PACMAN_SIZE);
            pacmanView.setFitHeight(PACMAN_SIZE);
            pacmanView.setLayoutX(random.nextInt(WIDTH - PACMAN_SIZE));
            pacmanView.setLayoutY(random.nextInt(HEIGHT - PACMAN_SIZE));
            root.getChildren().add(pacmanView);

            AnimationTimer animationTimer = new AnimationTimer() {
                double dx = 1 + random.nextDouble() * 3;
                double dy = 1 + random.nextDouble() * 3;

                @Override
                public void handle(long now) {
                    double newX = pacmanView.getLayoutX() + dx;
                    double newY = pacmanView.getLayoutY() + dy;

                    if (newX <= 0 || newX >= WIDTH - PACMAN_SIZE) {
                        dx = -dx;
                    }
                    if (newY <= 0 || newY >= HEIGHT - PACMAN_SIZE) {
                        dy = -dy;
                    }

                    pacmanView.setLayoutX(newX);
                    pacmanView.setLayoutY(newY);
                }
            };
            animationTimer.start();
        }
 
       
        playButton.setOnAction(e -> {
            CopyOfSpinWheelMain2 gameWindow = new CopyOfSpinWheelMain2();
            gameWindow.start(new Stage());
            primaryStage.close();
            mediaPlayer.stop();
        });

        exitButton.setOnAction(e -> System.exit(0));

        helpButton.setOnAction(e -> {
            HelpWindow helpWindow = new HelpWindow();
            Scene helpScene = helpWindow.createScene();
            primaryStage.setScene(helpScene);
            primaryStage.show();
        });

        AnchorPane.setTopAnchor(buttonBox, 10.0);
        AnchorPane.setLeftAnchor(buttonBox, 10.0);

        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.setTitle("The Mavity Tree");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}