package dk.tij.jchessfx.views;

import com.almasb.fxgl.dsl.FXGL;
import dk.tij.jchessfx.Configuration;
import dk.tij.jchessfx.model.TileModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Observable;
import java.util.Observer;

public class Tile implements Observer {
    private Color pointerColour = Color.rgb(20, 160, 20, 1.0f);
    private Color playColour = Color.rgb(130, 200, 255, 0.75f);
    private Color selectionColour = Color.rgb(120, 120, 120, 1.0f);
    private Color attackColour = Color.rgb(255, 55, 0, 0.75f);
    private final Rectangle rectFilter;
    private final ImageView image;

    public Tile(double x, double y, double size, Color baseColour) {
        x *= size;
        y *= size;
        Rectangle rectBase = new Rectangle(size, size, baseColour);
        rectFilter = new Rectangle(size, size, Color.color(0, 0, 0, 0));
        image = new ImageView();
        image.setFitHeight((double) Configuration.height / Configuration.squareCount);
        image.setFitWidth((double) Configuration.height / Configuration.squareCount);
        FXGL.entityBuilder().at(x, y).view(rectBase).buildAndAttach();
        FXGL.entityBuilder().at(x, y).view(rectFilter).buildAndAttach();
        FXGL.entityBuilder().at(x, y).view(image).buildAndAttach();
    }

    public void setPieceImage(Image pieceImage) {
        image.setImage(pieceImage);
    }

    @Override
    public void update(Observable o, Object arg) {
        TileModel model = (TileModel) o;
        rectFilter.setFill(Color.color(0, 0, 0, 0));
        if (model.isPlaying()) {
            if (model.getPiece() == null) rectFilter.setFill(playColour);
            else rectFilter.setFill(attackColour);
        }
        if (model.isSelected()) rectFilter.setFill(selectionColour);
        if (model.isPointing()) rectFilter.setFill(pointerColour);
        if (model.getPiece() != null)
            image.setImage(Configuration.getTile(model.getPiece().getPieceCounter(), model.getPiece().getSide()));
        else image.setImage(null);
    }
}
