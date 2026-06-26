package Practice.LLD.LLDQuestions.TicTacToe;

public class Main {
    public static void main(String args[]) {
        TicTacToeGame game = new TicTacToeGame();
        game.initGame();
        System.out.println("Result of the game is: " + game.startGame());
    }
}
