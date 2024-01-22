import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("Welcome to tic-tac-toe, Player 0 starts\n");
    System.out.print("Player 0 please enter your name: ");
    String name = input.nextLine();
    System.out.print(name + " please enter your mark: ");
    String mark = input.nextLine();
    Player p0 = new Player(name, mark);
    System.out.print("Player 1 please enter your name: ");
    name = input.nextLine();
    System.out.print(name + " please enter your mark: ");
    mark = input.nextLine();
    Player p1 = new Player(name, mark);

    GameController game = new GameController(p0, p1);

    game.startGame();
  }
}

class GameController {
  // players stored in an array to use the index as an identifier
  private Player[] players = new Player[2];
  // positions on board stored in a 2-dimensional array to use row/col coordinated for position
  private Grid[][] grids = new Grid[3][3];
  private int turn;
  // result variable that shows the current games status
  // 1 - player1 won
  // 0 - player0 won
  // -1 - no result yet
  // -2 - draw
  private int result = -1;

  GameController(Player player1, Player player2) {
    this.players[0] = player1;
    this.players[1] = player2;

    this.initializeGame();
  }

  public void initializeGame () {
    // method for setting the variables to begin the game and filling the game board
    // can be rerun to reset the game, usefull for future development of the game
    this.turn = 0;
    this.result = -1;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        this.grids[i][j] = new Grid();
      }
    }
  }

  public void startGame () {
    Scanner input = new Scanner(System.in);

    // runs until someone has won or there is a draw
    while (this.result == -1) {
      // setup for doing a turn
      int currentPlayerIndex = this.turn % 2;
      Player currentPlayer = this.players[currentPlayerIndex];
      boolean validMove = false;

      // reruns if the move prompt if the move is on a already marked square
      while (!validMove) {
        showBoard();
        System.out.print(currentPlayer.getName() + " it is your turn. Where would you like to place your mark? Row/Col: ");
        String choice = input.nextLine();
        // the input is in the format row/col
        // the input gets split by the slash and put in an array
        String[] sCoords = choice.split("/");
        // the array of strings gets parsed into an array of ints
        int[] coords = new int[2];
        for (int i = 0; i < 2; i++) {
          coords[i] = Integer.parseInt(sCoords[i]);
        }
        // checks if move is valid
        if (this.grids[coords[0]][coords[1]].getMarking() != -1) {
          System.out.print("Invalid move please try again\n");
        } else {
          this.grids[coords[0]][coords[1]].mark(currentPlayerIndex);
          validMove = true;
        }
      }
      // ending the turn
      this.checkBoard();
      this.turn++;
    }

    // if the result gets changed from -1 show end game text.
    if (this.result == -2) {
      System.out.print("Its a draw.\nFinal board:\n");
      this.showBoard();
    } else {
      System.out.print(this.players[result].getName() + " won!\nFinal board:\n");
      this.showBoard();
    }
  }

  public void checkBoard () {
    // checks if rows are 3 in a row
    for (int i = 0; i < 3; i++) {
      if (this.grids[i][0].getMarking() == this.grids[i][1].getMarking() && this.grids[i][1].getMarking() == this.grids[i][2].getMarking()) {
        if(this.grids[i][0].getMarking() != -1) {
          this.result = this.grids[i][0].getMarking();
          return;
        }
      }
    }
    // checks if columns are 3 in a row
    for (int i = 0; i < 3; i++) {
      if (this.grids[0][i].getMarking() == this.grids[1][i].getMarking() && this.grids[1][i].getMarking() == this.grids[2][i].getMarking()) {
        if (this.grids[0][i].getMarking() != -1) {
          this.result = this.grids[0][i].getMarking();
          return;
        }
      }
    }
    // checks if diagonal starting from top left has 3 in a row
    if (this.grids[0][0].getMarking() == this.grids[1][1].getMarking() && this.grids[1][1].getMarking() == this.grids[2][2].getMarking()) {
      if (this.grids[0][0].getMarking() != -1) {
        this.result = this.grids[0][0].getMarking();
        return;
      }
    }
    // checks if diagonal starting from bottom left has 3 in a row
    if (this.grids[2][0] == this.grids[1][1] && this.grids[1][1] == this.grids[0][2]) {
      if (this.grids[2][0].getMarking() != -1) {
        this.result = this.grids[2][0].getMarking();
        return;
      }
    }
    // checks if its turn 9 meaning that there aren't any more positions
    if (this.turn >= 8) {
      this.result = -2;
    }

  }

  public void showBoard() {
    // top info
    System.out.print("   0   1   2 \n");
    for (int i = 0; i < 3; i++) {
      // skips the first horizontal border
      if (i != 0) {
        System.out.print("  ---|---|---\n");
      }
      // padding on left col numbers
      System.out.print(i + " ");
      for (int j = 0; j < 3; j++) {
        // skips the first vertical border
        if (j != 0) {
          System.out.print("|");
        }
        // marks the position if there is a mark
        if (this.grids[i][j].getMarking() == -1) {
          System.out.print("   ");
        } else {
          System.out.print(" " + this.players[this.grids[i][j].getMarking()].getMark()  + " ");
        }
      }
      System.out.print("\n");
    }
  }

}

class Player {
  private String name;
  private String mark;

  Player (String name, String mark) {
    this.name = name;
    this.mark = mark;
  }

  public String getName () {
    return this.name;
  }
  public String getMark () {
    return this.mark;
  }
}

class Grid {
  private int marking;

  Grid () {
    this.marking = -1;
  }

  void mark(int marking) {
    this.marking = marking;
  }

  int getMarking() {
    return this.marking;
  }
}