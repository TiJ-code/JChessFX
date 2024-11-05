package dk.tij.jchessfx.struct;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.pieces.Piece;

public class RecordedMove {
    private Vec2 origin;
    private Piece pieceOrigin;
    private Vec2 destination;
    private Piece pieceDestination;

    public RecordedMove(Vec2 origin, Piece pieceOrigin, Vec2 destination, Piece pieceDestination) {
        this.origin = origin;
        this.pieceOrigin = pieceOrigin;
        this.destination = destination;
        this.pieceDestination = pieceDestination;
    }

    public Vec2 getOrigin() {
        return origin;
    }

    public void setOrigin(Vec2 origin) {
        this.origin = origin;
    }

    public Piece getPieceOrigin() {
        return pieceOrigin;
    }

    public void setPieceOrigin(Piece pieceOrigin) {
        this.pieceOrigin = pieceOrigin;
    }

    public Vec2 getDestination() {
        return destination;
    }

    public void setDestination(Vec2 destination) {
        this.destination = destination;
    }

    public Piece getPieceDestination() {
        return pieceDestination;
    }

    public void setPieceDestination(Piece pieceDestination) {
        this.pieceDestination = pieceDestination;
    }
}
