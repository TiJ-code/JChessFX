package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece {
    protected int pieceCounter;
    protected int side;
    protected List<Vec2> moves;

    protected Piece(int pieceCounter, int side) {
        moves = new ArrayList<>();
        this.pieceCounter = pieceCounter;
        this.side = side;
    }

    public int getPieceCounter() {
        return pieceCounter;
    }

    public void setPieceCounter(int pieceCounter) {
        this.pieceCounter = pieceCounter;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public abstract List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check);

    protected boolean addMove(BoardModel board, Vec2 position) {
        if (board.isEmpty(position) || board.isEnemy(position, side)) {
            moves.add(position.copy());
        }
        return board.isEmpty(position);
    }

    public abstract Piece copy();

    protected void checkMoves(BoardModel board, Vec2 position) {
        moves = moves.stream()
                .filter(move -> {
                    board.movePiece(move.copy(), position.copy());
                    boolean res = !board.isInCheck(side);
                    board.back();
                    return res;
                })
                .collect(Collectors.toList());
    }
}
