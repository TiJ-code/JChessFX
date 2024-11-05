package dk.tij.jchessfx.model;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.pieces.Piece;

import java.util.Observable;

public class TileModel extends Observable {
    private final Vec2 position;
    private boolean playing;
    private boolean selected;
    private boolean pointing;
    private Piece piece;

    public TileModel(int x, int y) {
        this.playing = false;
        this.selected = false;
        this.pointing = false;
        piece = null;
        position = new Vec2(x, y);
    }

    public Vec2 getPosition() {
        return position;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        if (this.playing != playing) setChanged();
        this.playing = playing;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selecting) {
        if (this.selected != selecting) setChanged();
        this.selected = selecting;
    }

    public boolean isPointing() {
        return pointing;
    }

    public void setPointing(boolean pointing) {
        if (this.pointing != pointing) setChanged();
        this.pointing = pointing;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        if (this.piece != piece) setChanged();
        this.piece = piece;
    }
}
