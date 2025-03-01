package cleancode.minesweeper.tobe;

import cleancode.minesweeper.gamelevel.*;
import cleancode.minesweeper.tobe.minesweeper.Minesweeper;
import cleancode.minesweeper.tobe.minesweeper.config.GameConfig;
import cleancode.minesweeper.tobe.minesweeper.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.minesweeper.io.ConsoleOutputHandler;

public class GameApplication {

  public static void main(String[] args) {
//    GameLevel gameLevel = new Beginner();
//    GameLevel gameLevel = new VeryBeginner();
//    GameLevel gameLevel = new Middle();
    GameConfig gameConfig = new GameConfig(new VeryBeginner(), new ConsoleInputHandler(), new ConsoleOutputHandler());

    Minesweeper minesweeper = new Minesweeper(gameConfig);
    minesweeper.initialize();
    minesweeper.run();
  }
}
