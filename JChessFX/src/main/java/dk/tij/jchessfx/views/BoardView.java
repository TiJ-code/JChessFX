package dk.tij.jchessfx.views;

import dk.tij.jchessfx.Configuration;
import dk.tij.jchessfx.model.TileModel;
import javafx.scene.paint.Color;

public class BoardView {

    public BoardView(TileModel[][] tileModels) {
        Tile[][] tiles = new Tile[Configuration.squareCount][Configuration.squareCount];

        for (int x = 0; x < Configuration.squareCount; x++) {
            for (int y = 0; y < Configuration.squareCount; y++) {
                Color colour = (((x + y) & 1) == 0) ? Color.WHITE : Color.DARKGRAY;
                tiles[x][y] = new Tile(x, y, (double) Configuration.height / Configuration.squareCount, colour);
                tileModels[x][y].addObserver(tiles[x][y]);
            }
        }
    }
}
