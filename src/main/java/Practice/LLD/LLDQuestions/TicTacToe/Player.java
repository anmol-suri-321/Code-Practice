package Practice.LLD.LLDQuestions.TicTacToe;

public class Player {
    String name;
    Piece piece;

    public Player(String name, Piece piece) {
        this.name = name;
        this.piece = piece;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPieceType(Piece piece) {
        this.piece = piece;
    }
}
