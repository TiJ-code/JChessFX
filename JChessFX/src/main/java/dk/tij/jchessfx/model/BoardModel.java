package dk.tij.jchessfx.model;

import com.almasb.fxgl.core.math.Vec2;
import dk.tij.jchessfx.Configuration;
import dk.tij.jchessfx.dialogs.PromotionDialog;
import dk.tij.jchessfx.dialogs.WinDialog;
import dk.tij.jchessfx.model.pieces.*;
import dk.tij.jchessfx.struct.RecordedMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicReference;

public class BoardModel {
    private boolean promoting;
    private boolean showPointing;
    private int promotionSide;
    private Vec2 pointer;
    private Vec2 selection;
    private TileModel[][] tiles;
    private TileModel selectedTile;
    private final Vec2[] positions;
    private List<Vec2> moves;
    private final List<RecordedMove> recordedMoves;
    private final Piece[] actualPieces;

    public BoardModel(Vec2 pointer) {
        this.pointer = pointer;
        showPointing = true;
        promoting = false;
        promotionSide = 0;
        selection = null;
        tiles = new TileModel[Configuration.squareCount][Configuration.squareCount];
        selectedTile = null;
        moves = new ArrayList<>();
        positions = new Vec2[3];
        actualPieces = new Piece[3];
        recordedMoves = new ArrayList<>();
        for (int x = 0; x < Configuration.squareCount; x++) {
            for (int y = 0; y < Configuration.squareCount; y++) {
                tiles[x][y] = new TileModel(x, y);
            }
        }
        colourPieces();
    }

    public BoardModel() {
        this(new Vec2());
    }

    public void updateObservers() {
        Arrays.stream(tiles)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(Observable::notifyObservers));
    }

    public TileModel[][] getTileModels() {
        return tiles;
    }

    public int getCurrentTurn() {
        if (recordedMoves.isEmpty()) {
            return 0; // Default to player 0 if no moves have been made
        }
        RecordedMove lastRecord = recordedMoves.getLast();
        return (lastRecord.getPieceOrigin() == null || lastRecord.getPieceOrigin().getSide() == 1) ? 0 : 1;
    }

    private Vec2 getKing(int side) {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(tiles)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(tile -> {
                            if (!isEnemy(tile.getPosition(), side) && tile.getPiece() instanceof King)
                                res.set(tile.getPosition());
                        }));
        return res.get();
    }

    public void back() {
        if (!promoting && !recordedMoves.isEmpty()) {
            RecordedMove lastRecord = recordedMoves.getLast();
            tiles[(int) lastRecord.getOrigin().x][(int) lastRecord.getOrigin().y].setPiece(lastRecord.getPieceOrigin());
            tiles[(int) lastRecord.getDestination().x][(int) lastRecord.getDestination().y].setPiece(lastRecord.getPieceDestination());
            recordedMoves.remove(recordedMoves.size() - 1);

            if (lastRecord.getPieceOrigin() instanceof Pawn && lastRecord.getPieceDestination() == null
                && lastRecord.getOrigin().x != lastRecord.getDestination().x) {
                tiles[(int) lastRecord.getDestination().x][(int) lastRecord.getOrigin().y].setPiece(new Pawn(lastRecord.getPieceOrigin().getSide() * (-1) + 1));
            }

            if (lastRecord.getPieceOrigin() instanceof King && Math.abs(lastRecord.getOrigin().x - lastRecord.getDestination().x) == 2) {
                if (lastRecord.getOrigin().x - lastRecord.getOrigin().x < 0) {
                    tiles[(int) lastRecord.getDestination().x + 1][(int) lastRecord.getDestination().y].setPiece(new Rook(lastRecord.getPieceOrigin().getSide()));
                    tiles[(int) lastRecord.getDestination().x - 1][(int) lastRecord.getDestination().y].setPiece(null);
                } else {
                    tiles[(int) lastRecord.getDestination().x - 2][(int) lastRecord.getDestination().y].setPiece(new Rook(lastRecord.getPieceOrigin().getSide()));
                    tiles[(int) lastRecord.getDestination().x + 1][(int) lastRecord.getDestination().y].setPiece(null);
                }
            }
        }
    }

    private void colourPieces() {
        for (int i = 0; i < 8; i++) {
            tiles[i][6].setPiece(new Pawn(0));
            tiles[i][1].setPiece(new Pawn(1));
        }

        tiles[7][7].setPiece(new Rook(0));
        tiles[0][7].setPiece(new Rook(0));
        tiles[7][0].setPiece(new Rook(1));
        tiles[0][0].setPiece(new Rook(1));

        tiles[2][7].setPiece(new Bishop(0));
        tiles[5][7].setPiece(new Bishop(0));
        tiles[2][0].setPiece(new Bishop(1));
        tiles[5][0].setPiece(new Bishop(1));

        tiles[1][7].setPiece(new Knight(0));
        tiles[6][7].setPiece(new Knight(0));
        tiles[1][0].setPiece(new Knight(1));
        tiles[6][0].setPiece(new Knight(1));

        tiles[3][7].setPiece(new Queen(0));
        tiles[3][0].setPiece(new Queen(1));

        tiles[4][7].setPiece(new King(0));
        tiles[4][0].setPiece(new King(1));
    }

    private boolean detectChecks() {
        if (!promoting) {
            return isInCheck(getCurrentTurn());
        } else return false;
    }

    private void movePiece() {
        movePiece(pointer, selection);
        quitSelection();
    }

    public void movePiece(Vec2 destination, Vec2 orientation) {
        Piece pieceOrigin = tiles[(int) orientation.x][(int) orientation.y].getPiece() != null ?
                tiles[(int) orientation.x][(int) orientation.y].getPiece().copy() : null;
        Piece pieceDestination = tiles[(int) destination.x][(int) destination.y].getPiece() != null ?
                tiles[(int) destination.x][(int) destination.y].getPiece().copy() : null;

        RecordedMove recordedMove = new RecordedMove(orientation, pieceOrigin, destination, pieceDestination);
        recordedMoves.add(recordedMove);

        if (recordedMove.getPieceOrigin() instanceof Pawn && recordedMove.getPieceDestination() != null &&
            recordedMove.getOrigin().x != recordedMove.getDestination().x) {
            tiles[(int) recordedMove.getDestination().x][(int) recordedMove.getDestination().y].setPiece(null);
        }

        if (recordedMove.getPieceOrigin() instanceof King && Math.abs(recordedMove.getOrigin().x - recordedMove.getDestination().x) == 2) {
            if (recordedMove.getOrigin().x - recordedMove.getDestination().x < 0) {
                tiles[(int) recordedMove.getDestination().x + 1][(int) recordedMove.getDestination().y].setPiece(null);
                tiles[(int) recordedMove.getDestination().x - 1][(int) recordedMove.getDestination().y].setPiece(new Rook(recordedMove.getPieceOrigin().getSide()));
            } else {
                tiles[(int) recordedMove.getDestination().x - 2][(int) recordedMove.getDestination().y].setPiece(null);
                tiles[(int) recordedMove.getDestination().x + 1][(int) recordedMove.getDestination().y].setPiece(new Rook(recordedMove.getPieceOrigin().getSide()));
            }
        }

        tiles[(int) destination.x][(int) destination.y].setPiece(pieceOrigin);
        tiles[(int) orientation.x][(int) orientation.y].setPiece(null);
    }

    public void quitSelection() {
        selection = null;
        selectedTile = null;
        moves.clear();
    }

    public void select() {
        select(pointer);
    }

    private void select(Vec2 position) {
        if (!promoting) {
            if (selection != null && moves.stream().anyMatch(move -> move.equals(position))) {
                pointer = position.copy();
                movePiece();
            } else {
                pointer = position.copy();
                selection = position.copy();
                selectedTile = tiles[(int) selection.x][(int) selection.y];
                if (selectedTile.getPiece() == null ||
                        selectedTile.getPiece().getSide() != getCurrentTurn()) {
                    quitSelection();
                }
            }
        } else {
            // Handle promotion selection
            if (Arrays.asList(positions).contains(position)) {
                // This assumes positions are set correctly for the promotion pieces
                chooseCoronation(selection); // Use the selection to promote
            }
        }

        Vec2 pawnPromoted = pawnPromoted();
        if (pawnPromoted != null) {
            promoting = true;
            chooseCoronation(pawnPromoted);
        }

        if (!promoting) moves.clear();
        if (selection != null) {
            if (tiles[(int) selection.x][(int) selection.y].getPiece() != null) {
                moves = tiles[(int) selection.x][(int) selection.y].getPiece().calculateMoves(this, selection, true);
            }
        }

        // Update tile states
        Arrays.stream(tiles)
                .flatMap(Arrays::stream)
                .forEach(e2 -> {
                    e2.setPointing(showPointing && e2.getPosition().equals(pointer));
                    e2.setSelected(e2.getPosition().equals(selection));
                    e2.setPlaying((e2.getPiece() instanceof King && e2.getPiece().getSide() == getCurrentTurn() && isInCheck(getCurrentTurn()))
                            || moves.stream().anyMatch(m -> m.equals(e2.getPosition())));
                });

        if (possibleMoves(getCurrentTurn()).isEmpty()) {
            // Assuming you have a way to determine the winner's name
            String winnerName = "";
            if (detectChecks())
                winnerName = ((getCurrentTurn() == 0) ? "BLACK" : "WHITE");
            else
                winnerName = "STALE MATE";
            WinDialog.showWinDialog(winnerName);
        }
    }

    private void chooseCoronation(Vec2 pawnPromoted) {
        // Determine the side of the promoting pawn
        promotionSide = tiles[(int) pawnPromoted.x][(int) pawnPromoted.y].getPiece().getSide();

        // Show the promotion dialog to the player
        Piece newPiece = PromotionDialog.showPromotionDialog(promotionSide);
        if (newPiece != null) {
            // Set the new piece in the pawn's position
            tiles[(int) pawnPromoted.x][(int) pawnPromoted.y].setPiece(newPiece);
        }

        // Clear the promotion state
        promoting = false;

        // Optionally, you can record the move if needed
        RecordedMove recordedMove = new RecordedMove(pawnPromoted, tiles[(int) pawnPromoted.x][(int) pawnPromoted.y].getPiece().copy(), pawnPromoted, newPiece);
        recordedMoves.add(recordedMove);
    }

    private Vec2 pawnPromoted() {
        AtomicReference<Vec2> res = new AtomicReference<>();
        Arrays.stream(tiles)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(tile -> {
                            if (tile.getPiece() instanceof Pawn && (tile.getPosition().y == 0 || tile.getPosition().y == Configuration.squareCount - 1))
                                res.set(tile.getPosition());
                        }));
        return res.get();
    }

    private List<Vec2> possibleMoves(int side) {
        List<Vec2> moves = new ArrayList<>();
        Arrays.stream(tiles)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(tile -> {
                            Piece piece = tile.getPiece();
                            if (piece != null && tile.getPosition() != null && piece.getSide() == side) {
                                moves.addAll(piece.calculateMoves(this, tile.getPosition(), true));
                            }
                        }));
        return moves;
    }

    public void mouseSelect(Vec2 position) {
        mouseSelect(position, true);
    }

    public void mouseSelect(Vec2 position, boolean normal) {
        if (normal || !promoting) {
            showPointing = false;
            Vec2 mousePosition = new Vec2((int) (position.x / ((float) Configuration.height / Configuration.squareCount)), (int) (position.y / ((float) Configuration.height / Configuration.squareCount)));
            if (isInside(mousePosition))
                select(mousePosition);
        }
    }

    public void onUpdate() {
        updateObservers();
    }

    public void goUp() {
        showPointing = true;
        if (!promoting && pointer.y > 0
                || pointer.y > 4 && promotionSide == 1
                || pointer.y > 0 && promotionSide == 0) {
            pointer.y--;
        }
    }

    public void goDown() {
        showPointing = true;
        if (!promoting && pointer.y < Configuration.squareCount - 1
                || promoting && pointer.y < Configuration.squareCount - 1 - 4 && promotionSide == 0
                || promoting && pointer.y < Configuration.squareCount - 1 && promotionSide == 1) {
            pointer.y++;
        }
    }

    public void goLeft() {
        showPointing = true;
        if (pointer.x > 0 && !promoting) {
            pointer.x--;
        }
    }

    public void goRight() {
        showPointing = true;
        if (pointer.x < Configuration.squareCount - 1 && !promoting) {
            pointer.x++;
        }
    }

    public boolean wasMoved(Vec2 position) {
        return recordedMoves.stream().anyMatch(r -> r.getOrigin().equals(position));
    }

    private boolean isInside(Vec2 position) {
        return position.x >= 0 && position.x <= Configuration.squareCount - 1 &&
                position.y >= 0 && position.y <= Configuration.squareCount - 1;
    }

    public boolean isPawn(Vec2 position) {
        return isInside(position) && tiles[(int) position.x][(int) position.y].getPiece() instanceof Pawn;
    }

    public boolean isEmpty(Vec2 position) {
        return isInside(position) && tiles[(int) position.x][(int) position.y].getPiece() == null;
    }

    public boolean isEnemy(Vec2 position, int side) {
        return isInside(position) && tiles[(int) position.x][(int) position.y].getPiece() != null &&
                tiles[(int) position.x][(int) position.y].getPiece().getSide() != side;
    }

    public boolean isInCheck(int side) {
        return isAttacked(getKing(side), side);
    }

    public boolean isLastPlaying(Vec2 origin, Vec2 destination) {
        return recordedMoves.getLast().getOrigin().equals(origin) && recordedMoves.getLast().getDestination().equals(destination);
    }

    public boolean isAttacked(Vec2 position, int side) {
        List<Vec2> attacks = new ArrayList<Vec2>();
        Arrays.stream(tiles)
                .toList()
                .forEach(e1 -> Arrays.stream(e1)
                        .toList()
                        .forEach(tile -> {
                            if (isEnemy(tile.getPosition(), side))
                                attacks.addAll(tile.getPiece().calculateMoves(this, tile.getPosition(), false));
                        }));
        return attacks.stream().anyMatch(attack -> attack.equals(position));
    }
}
