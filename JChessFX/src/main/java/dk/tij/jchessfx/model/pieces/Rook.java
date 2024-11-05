package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.List;

public class Rook extends Piece {
    public Rook(int side) {
        super(4, side);
    }

    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check) {
        Vec2 newMove;
        moves.clear();

        newMove = new Vec2(position.x, position.y - 1);
        while (addMove(board, newMove))
            newMove.y--;

        newMove = new Vec2(position.x, position.y + 1);
        while (addMove(board, newMove))
            newMove.y++;

        newMove = new Vec2(position.x + 1, position.y);
        while (addMove(board, newMove))
            newMove.x++;

        newMove = new Vec2(position.x - 1, position.y);
        while (addMove(board, newMove))
            newMove.x--;

        if (check)
            checkMoves(board, position);

        return moves;
    }

    @Override
    public Piece copy() {
        return new Rook(side);
    }
}
