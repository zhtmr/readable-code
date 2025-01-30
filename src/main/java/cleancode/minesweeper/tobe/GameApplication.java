package cleancode.minesweeper.tobe;

import cleancode.minesweeper.gamelevel.*;

public class GameApplication {

  public static void main(String[] args) {
//    GameLevel gameLevel = new Beginner();
//    GameLevel gameLevel = new VeryBeginner();
//    GameLevel gameLevel = new Middle();
    GameLevel gameLevel = new Advanced();
    Minesweeper minesweeper = new Minesweeper(gameLevel);
    minesweeper.initialize();
    minesweeper.run();
  }
}
