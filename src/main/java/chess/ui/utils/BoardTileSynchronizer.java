/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ui.utils;

import chess.gameplay.world.Board;
import chess.gameplay.world.BoardCell;
import chess.gameplay.world.ChessPiece;

/**
 * Used for synchronizing the image shown on the board tiles.
 * 
 * @author Gabriel
 */
public class BoardTileSynchronizer {

    private static final String WHITE_COLOR_SUFFIX = "_white";

    private final BoardTile[][] boardTiles;

    public BoardTileSynchronizer(BoardTile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }

    public void synchronizeToChessBoard(Board chessBoard) {
        // Go through all available positions of the game board.
        for (int column = 0; column < Board.SIDE_SIZE; column++) {
            for (int row = 0; row < Board.SIDE_SIZE; row++) {
                // Retrieve tile.
                BoardTile tile = boardTiles[column][row];
                // Retrieve cell.
                BoardCell cell = chessBoard.getCell(column, row);

                // Update the visual state of the tile, according to
                // the state of the cell.
                SynchronizeTileToCellStatus(tile, cell);
            }
        }
    }

    private void SynchronizeTileToCellStatus(BoardTile tile,
            BoardCell cell) {
        // Check whether or not there's a piece on the cell.
        if (cell.hasPiece()) {
            // Retrieve the image name for the piece.
            String pieceImageName = retrievePieceImageNameFromCell(cell);

            // Set the image for the piece located on the cell.
            tile.setPieceOn(pieceImageName);
        } else {
            // Set the image for an empty tile.
            tile.setEmpty();
        }
    }

    //
    // Retrieve the image name for the piece located at the specified cell.
    //
    private String retrievePieceImageNameFromCell(BoardCell cell) {
        if (cell.hasPiece()) {
            // Retrieve piece name by making usage of its name.
            String piece = convertPieceTypeToLowerCaseName(
                    cell.getPiece().getType());
            // Retrieve piece's color suffix.
            String pieceColorSuffix = retrieveColorSuffixFromPiece(
                    cell.getPiece());

            return String.format("%s%s", piece, pieceColorSuffix);
        } else {
            // Cell must have a piece on it.
            throw new IllegalArgumentException("Cell has no chess piece.");
        }
    }

    //
    // Converts the specified piece type name to lower case.
    //
    private String convertPieceTypeToLowerCaseName(ChessPiece.PieceType pieceType) {
        // Returns the piece type name lower-cased.
        return pieceType.toString().toLowerCase();
    }

    //
    // Retrieves the color suffix from a piece.
    //
    private String retrieveColorSuffixFromPiece(ChessPiece piece) {
        // If the owner of the piece is using the WHITE pieces then
        // there's a suffix.
        if (piece.getOwner().getPiecesColor() == ChessPiece.PieceColor.WHITE) {
            return WHITE_COLOR_SUFFIX;
        } else {
            // No suffix needed for black pieces.
            return "";
        }
    }
}
