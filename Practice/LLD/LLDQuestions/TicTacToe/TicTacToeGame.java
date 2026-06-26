package Practice.LLD.LLDQuestions.TicTacToe;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class TicTacToeGame {
    Deque<Player> players;
    GameBoard gameBoard;

    public void initGame() {
        players = new LinkedList<>();

        Player p1 = new Player("Anmol", new PieceO());
        Player p2 = new Player("Shreya", new PieceX());
        players.add(p1);
        players.add(p2);

        gameBoard = new GameBoard(3);
    }

    public String startGame() {
        while(true) {
            gameBoard.printBoard();
            Player player = players.pollFirst();
            System.out.println(player.getName() + " has a chance, Enter ");

            if(gameBoard.isGameBoardFull()) {
                return "Game ended in a tie";
            }

            Scanner scan = new Scanner(System.in);
                String s = scan.nextLine();
                String[] parts = s.split(",");

                int x = Integer.valueOf(parts[0]);
                int y = Integer.valueOf(parts[1]);

                boolean pieceAdded = gameBoard.addPiece(x, y, player.piece);
                if(!pieceAdded) {
                    System.out.println("Taken wrong step, please try again");
                    players.offerFirst(player);
                    continue;
                }
                players.offerLast(player);

                if(checkWinner(x, y, player.piece)) {
                    return player.getName() + " is the winner";
                }
        }
    }

    private boolean checkWinner(int x, int y, Piece piece) {
        boolean row = true;
        boolean col = true;
        boolean diag = true;
        boolean reverseDiag = true;
        int n = gameBoard.size;

        for(int i = 0; i < n; i++) {
            if(gameBoard.board[i][y] == null || gameBoard.board[i][y].pieceType != piece.pieceType) {
                col = false;
            }
        }

        for(int j = 0; j < n; j++) {
            if(gameBoard.board[x][j] == null || gameBoard.board[x][j].pieceType != piece.pieceType) {
                row = false;
            }
        }

        for(int i = 0, j = 0; i < n; i++, j++) {
            if(gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != piece.pieceType) {
                diag = false;
            }
        }

        for(int i = 0, j = n - 1; i < n; i++, j--) {
            if(gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != piece.pieceType) {
                reverseDiag = false;
            }
        }

        return row || col || diag || reverseDiag;
    }
    
}
