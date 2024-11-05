package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.List;

public class King extends Piece {
    public King(int side) {
        super(0, side);
    }

    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check) {
        moves.clear();

        addMove(board, new Vec2(position.x + 1, position.y));
        addMove(board, new Vec2(position.x - 1, position.y));
        addMove(board, new Vec2(position.x, position.y + 1));
        addMove(board, new Vec2(position.x, position.y - 1));
        addMove(board, new Vec2(position.x + 1, position.y + 1));
        addMove(board, new Vec2(position.x + 1, position.y - 1));
        addMove(board, new Vec2(position.x - 1, position.y + 1));
        addMove(board, new Vec2(position.x - 1, position.y - 1));

        if (check) {
            addCastling(board, position);
            checkMoves(board, position);
        }

        return moves;
    }

    private void addCastling(BoardModel board, Vec2 position) {
        if (side == 0 && position.y == 7 || side == 1 && position.y == 0) {
            Vec2 position1 = new Vec2(position.x + 1, position.y);
            Vec2 position2 = new Vec2(position.x + 2, position.y);

            if (!board.wasMoved(position) && !board.wasMoved(new Vec2(position.x + 3, position.y))
                && board.isEmpty(position1) && board.isEmpty(position2)
                && !board.isAttacked(position1, side) && !board.isAttacked(position2, side)) {
                moves.add(position2);
            }

            position1 = new Vec2(position.x - 1, position.y);
            position2 = new Vec2(position.x - 2, position.y);
            Vec2 position3 = new Vec2(position.x - 3, position.y);

            if (!board.wasMoved(position) && !board.wasMoved(new Vec2(position.x - 4, position.y))
                && board.isEmpty(position1) && board.isEmpty(position2) && board.isEmpty(position3)
                && !board.isAttacked(position1, side) && !board.isAttacked(position2, side) && !board.isAttacked(position3, side)) {
                moves.add(position2);
            }
        }
    }

    @Override
    public Piece copy() {
        return new King(side);
    }
}
