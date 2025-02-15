package cleancode.minesweeper.tobe;

import cleancode.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;
import cleancode.minesweeper.tobe.position.CellPosition;
import cleancode.minesweeper.tobe.user.UserAction;


public class Minesweeper implements GameInitializable, GameRunnable {

  private final GameBoard gameBoard;
  private final InputHandler inputHandler;
  private final OutputHandler outputHandler;
  private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

  public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
    gameBoard = new GameBoard(gameLevel);
    this.inputHandler = inputHandler;
    this.outputHandler = outputHandler;
  }

  @Override
  public void initialize() {
    gameBoard.initializeGame();
  }

  @Override
  public void run() {
    outputHandler.showGameStartComments();

    while (true) {
      try {
        outputHandler.showBoard(gameBoard);

        if (doesUserWinTheGame()) {
          outputHandler.showGameWinningComment();
          break;
        }
        if (doesUserLoseTheGame()) {
          outputHandler.showGameLosingComment();
          break;
        }

        CellPosition cellPosition = getCellInputFromUser();
        UserAction userAction = getUserActionInputFrom();
        actOnCell(cellPosition, userAction);
      } catch (GameException e) {
        outputHandler.showExceptionMessage(e);
      } catch (Exception e) {
        outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
      }
    }
  }

  private void actOnCell(CellPosition cellPosition, UserAction userAction) {
    if (doesUserChooseToPlantFlag(userAction)) {
      gameBoard.flagAt(cellPosition);
      checkIfGameIsOver();
      return;
    }

    if (doseUserChooseToOpenCell(userAction)) {
      if (gameBoard.isLandMineCellAt(cellPosition)) {
        gameBoard.openAt(cellPosition);
        changeGameStatusToLose();
        return;
      }

      gameBoard.openSurroundedCells(cellPosition);
      checkIfGameIsOver();
      return;
    }
    throw new GameException("잘못된 번호를 선택하셨습니다.");
  }

  private void changeGameStatusToLose() {
    gameStatus = -1;
  }



  private boolean doseUserChooseToOpenCell(UserAction userAction) {
    return userAction == UserAction.OPEN;
  }

  private boolean doesUserChooseToPlantFlag(UserAction userAction) {
    return userAction == UserAction.FLAG;
  }



  private UserAction getUserActionInputFrom() {
    outputHandler.showCommentForUserAction();
    return inputHandler.getUserActionFrom();
  }

  private CellPosition getCellInputFromUser() {
    outputHandler.showCommentForSelectingCell();
    CellPosition cellPosition = inputHandler.getCellPositionFromUser();
    if (gameBoard.isInvalidCellPosition(cellPosition)) {
      throw new GameException("잘못된 좌표를 선택하셨습니다.");
    }
    return cellPosition;
  }

  private boolean doesUserLoseTheGame() {
    return gameStatus == -1;
  }

  private boolean doesUserWinTheGame() {
    return gameStatus == 1;
  }

  private void checkIfGameIsOver() {
    if (gameBoard.isAllCellChecked()) {
      changeGameStatusToWin();
    }
  }

  private void changeGameStatusToWin() {
    gameStatus = 1;
  }





}
