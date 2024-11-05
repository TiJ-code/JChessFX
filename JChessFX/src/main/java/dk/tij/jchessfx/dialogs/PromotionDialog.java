package dk.tij.jchessfx.dialogs;

import dk.tij.jchessfx.Configuration;
import dk.tij.jchessfx.model.pieces.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PromotionDialog {
    public static Piece showPromotionDialog(int side) {
        // Create a new dialog
        Dialog<Piece> dialog = new Dialog<>();
        dialog.setTitle("Promote Pawn");
        dialog.setHeaderText("Choose a piece to promote your pawn to:");

        // Create buttons for each promotion option with icons
        Button queenButton = createPieceButton(new Queen(side), "Queen", Configuration.getTile(1, side));
        Button rookButton = createPieceButton(new Rook(side), "Rook", Configuration.getTile(4, side));
        Button bishopButton = createPieceButton(new Bishop(side), "Bishop", Configuration.getTile(2, side));
        Button knightButton = createPieceButton(new Knight(side), "Knight", Configuration.getTile(3, side));

        // Set button actions
        queenButton.setOnAction(e -> dialog.setResult(new Queen(side)));
        rookButton.setOnAction(e -> dialog.setResult(new Rook(side)));
        bishopButton.setOnAction(e -> dialog.setResult(new Bishop(side)));
        knightButton.setOnAction(e -> dialog.setResult(new Knight(side)));

        // Create a layout and add buttons
        HBox buttonBox = new HBox(10, queenButton, rookButton, bishopButton, knightButton);
        dialog.getDialogPane().setContent(buttonBox);

        // Show the dialog and wait for a response
        dialog.showAndWait();

        // Return the selected piece or null if canceled
        return dialog.getResult();
    }

    private static Button createPieceButton(Piece piece, String pieceName, Image icon) {
        Button button = new Button(pieceName);
        ImageView imageView = new ImageView(icon); // Use the icon from Configuration
        imageView.setFitWidth(50); // Set desired width
        imageView.setFitHeight(50); // Set desired height
        button.setGraphic(imageView); // Set the icon to the button
        return button;
    }
}
