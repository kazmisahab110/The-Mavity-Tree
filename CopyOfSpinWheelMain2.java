import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

import java.util.Random;

public class CopyOfSpinWheelMain2 extends Application {
    private static final int NUM_SEGMENTS = 6;
    private static final double PANEL_SIZE = 400.0;
    private static final double START_ANGLE = 0.0;
    private static final double ARC_ANGLE = 360.0 / NUM_SEGMENTS;
    private static final String[] segmentValues = {"1", "2", "3", "4", "5", "6"};

    private Text selectedText;
    private AudioClip spinSound;
    private int numDrops;

    @Override
    public void start(Stage primaryStage) {
        spinSound = new AudioClip(getClass().getResource("spin_sound.mp3").toExternalForm());
        spinSound.setCycleCount(1);
        Group root = new Group();
        Scene scene = new Scene(root, PANEL_SIZE, PANEL_SIZE, Color.LIGHTGRAY);

        Group wheelGroup = new Group();
        Group buttonGroup = new Group();
        Group pointerGroup = new Group();
        Group infoGroup = new Group();

        Arc[] arcs = new Arc[NUM_SEGMENTS];
        Text[] texts = new Text[NUM_SEGMENTS];
        Color[] colors = {Color.rgb(255, 0, 0, 0.8), Color.rgb(0, 255, 0, 0.8), Color.rgb(0, 0, 255, 0.8),
                Color.rgb(255, 255, 0, 0.8), Color.rgb(255, 165, 0, 0.8), Color.rgb(128, 0, 128, 0.8)};
        String[] segmentLabels = {"1", "2", "3", "4", "5", "6"};

        for (int i = 0; i < NUM_SEGMENTS; i++) {
            Arc arc = new Arc(PANEL_SIZE / 2, PANEL_SIZE / 2, PANEL_SIZE / 2 - 50, PANEL_SIZE / 2 - 50,
                    START_ANGLE + i * ARC_ANGLE, ARC_ANGLE);
            arc.setType(ArcType.ROUND);
            arc.setFill(colors[i]);
            arcs[i] = arc;

            Text text = new Text();
            text.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            text.setFill(Color.WHITE);
            text.setText(segmentLabels[i]);

            double angle = START_ANGLE + i * ARC_ANGLE + ARC_ANGLE / 2;
            double x = PANEL_SIZE / 2 + Math.cos(Math.toRadians(angle)) * (PANEL_SIZE / 2 - 100);
            double y = PANEL_SIZE / 2 + Math.sin(Math.toRadians(angle)) * (PANEL_SIZE / 2 - 100);
            text.setX(x - text.getBoundsInLocal().getWidth() / 2);
            text.setY(y + text.getBoundsInLocal().getHeight() / 4);
            texts[i] = text;
        }

        Button spinButton = new Button("Spin");
        spinButton.setLayoutX(PANEL_SIZE / 2 - 30);
        spinButton.setLayoutY(PANEL_SIZE - 50);
        spinButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ff4500; -fx-text-fill: white; -fx-background-radius: 13;");
        spinButton.setOnAction(event -> {
            if (!spinSound.isPlaying()) {
                spinSound.play();
            }
            spinWheel(wheelGroup);
            spinButton.setDisable(true);
        });

        Button nextButton = new Button("Next");
        nextButton.setLayoutX(PANEL_SIZE - 120);
        nextButton.setLayoutY(PANEL_SIZE - 60);
        nextButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 13;");
        nextButton.setOnAction(event -> {
            openNextSpinWheelWindow(primaryStage);
        });

        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(new Double[]{
                20.0, 0.0,
                0.0, 5.0,
                0.0, -5.0
        });
        arrow.setFill(Color.rgb(50, 50, 50));
        arrow.setLayoutX(PANEL_SIZE / 2);
        arrow.setLayoutY(PANEL_SIZE / 2);
        arrow.setRotate(0);

        pointerGroup.getChildren().addAll(arrow);

        selectedText = new Text();
        selectedText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        selectedText.setFill(Color.BLACK);
        selectedText.setX(PANEL_SIZE / 2 - 100);
        selectedText.setY(PANEL_SIZE / 2 - 165);
        infoGroup.getChildren().add(selectedText);

        wheelGroup.getChildren().addAll(arcs);
        wheelGroup.getChildren().addAll(texts);
        buttonGroup.getChildren().addAll(spinButton, nextButton);

        root.getChildren().addAll(wheelGroup, buttonGroup, pointerGroup, infoGroup);

        primaryStage.setTitle("Spin Wheel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void spinWheel(Group wheelGroup) {
        double angle = new Random().nextInt(360 * 5) + 360 * 5;
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), wheelGroup);
        rotateTransition.setByAngle(angle);
        rotateTransition.setOnFinished(event -> {
            double pointerAngle = (wheelGroup.getRotate() + 360) % 360;
            double normalizedAngle = (pointerAngle + 360) % 360;
            int arcIndex = (int) ((NUM_SEGMENTS - normalizedAngle / ARC_ANGLE) % NUM_SEGMENTS);
            selectedText.setText("Number Of Drops: " + segmentValues[arcIndex]);
            numDrops = arcIndex + 1;
        });
        rotateTransition.play();
    }

    private void openNextSpinWheelWindow(Stage currentStage) {
        currentStage.close();
        Stage nextSpinWheelStage = new Stage();
        CopyOfSpinWheelMain nextSpinWheel = new CopyOfSpinWheelMain(this.numDrops);
        nextSpinWheel.start(nextSpinWheelStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
