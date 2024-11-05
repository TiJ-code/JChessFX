package dk.tij.jchessfx;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Configuration {
    public static final int squareCount = 8;
    public static final double cooldown = 0.15d;
    public static final int width = 800;
    public static final int height = 800;
    public static final String tilesetURL = "chessTileset.png";

    public static WritableImage getTile(int x, int y) {
        Image tileset = new Image(tilesetURL);

        int tileWidth = (int) tileset.getWidth() / 6;
        int tileHeight = (int) tileset.getHeight() / 2;

        int startX = x * tileWidth;
        int startY = y * tileHeight;

        return new WritableImage(tileset.getPixelReader(), startX, startY, tileWidth, tileHeight);
    }
}
