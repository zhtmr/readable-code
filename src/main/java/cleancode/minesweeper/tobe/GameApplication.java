package cleancode.minesweeper.tobe;

import cleancode.minesweeper.gamelevel.*;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameApplication {

  public static void main(String[] args) {
//    GameLevel gameLevel = new Beginner();
//    GameLevel gameLevel = new VeryBeginner();
//    GameLevel gameLevel = new Middle();
    GameLevel gameLevel = new Advanced();
    ConsoleInputHandler inputHandler = new ConsoleInputHandler();
    ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();
    Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
    minesweeper.initialize();
    minesweeper.run();
  }
}
