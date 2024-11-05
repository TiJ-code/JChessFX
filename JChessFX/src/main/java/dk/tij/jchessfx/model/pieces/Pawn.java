package dk.tij.jchessfx.model.pieces;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.model.BoardModel;

import java.util.List;

public class Pawn extends Piece {
    public Pawn(int side) {
        super(5, side);
    }

    @Override
    public List<Vec2> calculateMoves(BoardModel board, Vec2 position, boolean check) {
        moves.clear();

        if (side == 0) {
            if (addMove(board, new Vec2(position.x, position.y - 1)) && position.y == 6) {
                addMove(board, new Vec2(position.x, position.y - 2));
            }
            addAttack(board, new Vec2(position.x - 1, position.y - 1));
            addAttack(board, new Vec2(position.x + 1, position.y - 1));
        } else {
            if (addMove(board, new Vec2(position.x, position.y + 1)) && position.y == 1) {
                addMove(board, new Vec2(position.x, position.y + 2));
            }
            addAttack(board, new Vec2(position.x - 1, position.y + 1));
            addAttack(board, new Vec2(position.x + 1, position.y + 1));
        }
        if (check) {
            checkMoves(board, position);
        }
        return moves;
    }

    @Override
    protected boolean addMove(BoardModel board, Vec2 position) {
        if (board.isEmpty(position)) {
            moves.add(position.copy());
        }
        return board.isEmpty(position);
    }

    private void addAttack(BoardModel board, Vec2 position) {
        if (board.isEnemy(position, side)) {
            moves.add(position.copy());
        }

        if (side == 0) {
            if (position.y == 2 && board.isPawn(new Vec2(position.x, position.y + 1))
                && board.isLastPlaying(new Vec2(position.x, position.y - 1), new Vec2(position.x, position.y + 1))) {
                moves.add(position.copy());
            }
        }

        if (side == 1) {
            if (position.y == 5 && board.isPawn(new Vec2(position.x, position.y - 1))
                && board.isLastPlaying(new Vec2(position.x, position.y + 1), new Vec2(position.x, position.y - 1))) {
                moves.add(position.copy());
            }
        }
    }

    @Override
    public Piece copy() {
        return new Pawn(side);
    }
}
