import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGame extends Application {

    private static final int GRID_SIZE = 4;

    private List<Image> cards;
    private Button[] cardButtons = new Button[GRID_SIZE * GRID_SIZE];
    private int flippedCount = 0;
    private Image flippedImage1;
    private Image flippedImage2;
    private List<Button> matchedButtons = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeCards();
        Collections.shuffle(cards);

        GridPane gridPane = createAndConfigureGridPane();

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setTitle("Memory Card Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeCards() {
        cards = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String imagePath = "/images/image" + i + ".png";
            cards.add(new Image(getClass().getResource(imagePath).toExternalForm()));
        }
        // Duplicate cards to create pairs
        cards.addAll(new ArrayList<>(cards));
    }

    private GridPane createAndConfigureGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int index = i * GRID_SIZE + j;
                Button button = createCardButton(index);
                gridPane.add(button, j, i);
                cardButtons[index] = button;
            }
        }

        return gridPane;
    }

    private Button createCardButton(int index) {
        Button button = new Button();
        button.setMinSize(70, 70);
        button.setOnAction(e -> handleCardButtonClick(index));
        return button;
    }

    private void handleCardButtonClick(int index) {
        if (flippedCount < 2 && cardButtons[index].getGraphic() == null) {
            Image cardImage = cards.get(index);
            ImageView imageView = new ImageView(cardImage);
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            cardButtons[index].setGraphic(imageView);
            flippedCount++;

            if (flippedCount == 1) {
                flippedImage1 = cardImage;
            } else if (flippedCount == 2) {
                flippedImage2 = cardImage;
                checkForMatch(index);
            }
        }
    }

    private void checkForMatch(int currentIndex) {
        if (flippedCount < 2 || cardButtons[currentIndex].getGraphic() == null) {
            return;
        }
    
        if (flippedImage1.equals(flippedImage2)) {
            // Matched
            matchedButtons.add(cardButtons[currentIndex]);
            flippedCount = 0;
            checkGameEnd();
        } else {
            // Not matched, reset cards after a short delay
            Platform.runLater(() -> {
                resetFlippedCards();
                flippedCount = 0;
            });
        }
    }
    
    
    

    private void resetFlippedCards() {
        for (Button button : cardButtons) {
            if (button.getGraphic() != null) {
                button.setGraphic(null);
            }
        }
    }

    private void checkGameEnd() {
        if (matchedButtons.size() == GRID_SIZE * GRID_SIZE) {
            showWinAlert();
        }
    }
    
    private void showWinAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText("You Win!");
        alert.setContentText("You matched all pairs!");
        alert.showAndWait();
        resetGame();
    }
    

    private void resetGame() {
        initializeCards();
        Collections.shuffle(cards);
        for (int i = 0; i < cardButtons.length; i++) {
            cardButtons[i].setGraphic(null);
        }
        matchedButtons.clear();
    }
    
    
}
