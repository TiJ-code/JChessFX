package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.List;

public class Knight extends Piece {
    public Knight(int side) {
        super(3, side);
    }

    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check) {
        moves.clear();

        addMove(board, new Vec2(position.x - 1, position.y + 2));
        addMove(board, new Vec2(position.x + 1, position.y + 2));
        addMove(board, new Vec2(position.x - 1, position.y - 2));
        addMove(board, new Vec2(position.x + 1, position.y - 2));
        addMove(board, new Vec2(position.x + 2, position.y - 1));
        addMove(board, new Vec2(position.x + 2, position.y + 1));
        addMove(board, new Vec2(position.x - 2, position.y - 1));
        addMove(board, new Vec2(position.x - 2, position.y + 1));

        if (check)
            checkMoves(board, position);

        return moves;
    }

    @Override
    public Piece copy() {
        return new Knight(side);
    }
}
