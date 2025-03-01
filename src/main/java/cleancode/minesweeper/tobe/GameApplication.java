package cleancode.minesweeper.tobe;

import cleancode.minesweeper.gamelevel.*;
import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameApplication {

  public static void main(String[] args) {
//    GameLevel gameLevel = new Beginner();
//    GameLevel gameLevel = new VeryBeginner();
//    GameLevel gameLevel = new Middle();
    GameConfig gameConfig = new GameConfig(new Advanced(), new ConsoleInputHandler(), new ConsoleOutputHandler());

    Minesweeper minesweeper = new Minesweeper(gameConfig);
    minesweeper.initialize();
    minesweeper.run();
  }
}
