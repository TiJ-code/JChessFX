package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(int side) {
        super(2, side);
    }

    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check) {
        Vec2 newMove;
        moves.clear();

        newMove = new Vec2(position.x + 1, position.y + 1);
        while (addMove(board, newMove)) {
            newMove.x++;
            newMove.y++;
        }

        newMove = new Vec2(position.x - 1, position.y + 1);
        while (addMove(board, newMove)) {
            newMove.x--;
            newMove.y++;
        }

        newMove = new Vec2(position.x + 1, position.y - 1);
        while (addMove(board, newMove)) {
            newMove.x++;
            newMove.y--;
        }

        newMove = new Vec2(position.x - 1, position.y - 1);
        while (addMove(board, newMove)) {
            newMove.x--;
            newMove.y--;
        }

        if (check)
            checkMoves(board, position);

        return moves;
    }

    @Override
    public Piece copy() {
        return new Bishop(side);
    }
}
