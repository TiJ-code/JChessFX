package dk.tij.jchessfx.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WinDialog {
    public static void showWinDialog(String winnerName) {
        // Create a new alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Game Over!");
        alert.setContentText(winnerName + " has won the game!");

        // Add an OK button to close the dialog
        alert.getButtonTypes().setAll(ButtonType.OK);

        // Show the dialog and wait for a response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Logic to stop the game
                stopGame();
            }
        });
    }

    private static void stopGame() {
        // Implement your game stopping logic here
        // For example, you might want to close the game window or reset the game state
        System.out.println("Game has been stopped.");
        // You may also want to exit the application or return to a main menu
    }
}
