package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.cell.*;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class GameBoard {

  private final Cell[][] board;
  private final int landMineCount;
  private GameStatus gameStatus;

  public GameBoard(GameLevel gameLevel) {
    int colSize = gameLevel.getColSize();
    int rowSize = gameLevel.getRowSize();
    board = new Cell[rowSize][colSize];
    landMineCount = gameLevel.getLandMineCount();
    initializeGamesStatus();
  }

  public void initializeGame() {
    initializeGamesStatus();
    CellPositions cellPositions = CellPositions.from(board);

    initializeEmptyCells(cellPositions);

    List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
    initializeLandMineCells(landMinePositions);

    List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
    initializeNumberCells(numberPositionCandidates);
  }

  public void openAt(CellPosition cellPosition) {
    if (isLandMineCellAt(cellPosition)) {
      openOneCellAt(cellPosition);
      changeGameStatusToLose();
      return;
    }
    openSurroundedCells2(cellPosition);
    checkIfGameIsOver();
  }

  public void flagAt(CellPosition cellPosition) {
    findCell(cellPosition).flag();
    checkIfGameIsOver();
  }

  public boolean isInvalidCellPosition(CellPosition cellPosition) {
    int colSize = getColSize();
    int rowSize = getRowSize();

    return cellPosition.isRowIndexMoreThanOrEqual(rowSize) || cellPosition.isColIndexMoreThanOrEqual(colSize);
  }

  public boolean isInProgress() {
    return gameStatus == GameStatus.IN_PROGRESSO;
  }

  public boolean isWinStatus() {
    return gameStatus == GameStatus.WIN;
  }

  public boolean isLoseStatus() {
    return gameStatus == GameStatus.LOSE;
  }

  public CellSnapshot getSnapshot(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.getSnapshot();
  }

  public int getRowSize() {
    return board.length;
  }

  public int getColSize() {
    return board[0].length;
  }

  private void initializeGamesStatus() {
    gameStatus = GameStatus.IN_PROGRESSO;
  }

  private void initializeEmptyCells(CellPositions cellPositions) {
    List<CellPosition> allPositions = cellPositions.getPositions();
    for (CellPosition position : allPositions) {
      updateCellAt(position, new EmptyCell());
    }
  }

  private void initializeLandMineCells(List<CellPosition> landMinePositions) {
    for (CellPosition position : landMinePositions) {
      updateCellAt(position, new LandMineCell());
    }
  }

  private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
    for (CellPosition candidatePosition : numberPositionCandidates) {
      int count = countNearbyLandMines(candidatePosition);
      if (count != 0) {
        updateCellAt(candidatePosition, new NumberCell(count));
      }
    }
  }

  private boolean isLandMineCellAt(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.isLandMine();
  }

  private void openOneCellAt(CellPosition cellPosition) {
    findCell(cellPosition).open();
  }

  private void changeGameStatusToLose() {
    gameStatus = GameStatus.LOSE;
  }

  private void openSurroundedCells(CellPosition cellPosition) {
    if (isOpenedCell(cellPosition)) {
      return;
    }
    if (isLandMineCellAt(cellPosition)) {
      return;
    }

    openOneCellAt(cellPosition);

    if (doesCellHaveLandMineCount(cellPosition)) {
      return;
    }

    List<CellPosition> surroundedPositions = calculateSurroundedPositions(cellPosition, getRowSize(), getColSize());
    surroundedPositions.forEach(this::openSurroundedCells);
  }

  private void openSurroundedCells2(CellPosition cellPosition) {
    Deque<CellPosition> deque = new ArrayDeque<>();
    deque.push(cellPosition);

    while (!deque.isEmpty()) {
      openAndPushCellAt(deque);
    }
  }

  private void openAndPushCellAt(Deque<CellPosition> deque) {
    CellPosition currentCellPosition = deque.pop();
    if (isOpenedCell(currentCellPosition)) {
      return;
    }
    if (isLandMineCellAt(currentCellPosition)) {
      return;
    }

    openOneCellAt(currentCellPosition);

    if (doesCellHaveLandMineCount(currentCellPosition)) {
      return;
    }

    List<CellPosition> surroundedPositions =
      calculateSurroundedPositions(currentCellPosition, getRowSize(), getColSize());
    for (CellPosition surroundedPosition : surroundedPositions) {
      deque.push(surroundedPosition);
    }
  }

  private void checkIfGameIsOver() {
    if (isAllCellChecked()) {
      changeGameStatusToWin();
    }
  }

  private void updateCellAt(CellPosition position, Cell cell) {
    board[position.getRowIndex()][position.getColIndex()] = cell;
  }

  private int countNearbyLandMines(CellPosition cellPosition) {
    int rowSize = getRowSize();
    int colSize = getColSize();

    long count = calculateSurroundedPositions(cellPosition, rowSize, colSize)
      .stream()
      .filter(this::isLandMineCellAt)
      .count();

    return (int) count;
  }

  private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.hasLandMineCount();
  }

  private boolean isOpenedCell(CellPosition cellPosition) {
    Cell cell = findCell(cellPosition);
    return cell.isOpened();
  }

  private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition, int rowSize, int colSize) {
    return RelativePosition.SURROUNDED_POSITIONS
      .stream()
      .filter(cellPosition::canCalculatePositionBy)
      .map(cellPosition::calculatePositionBy)
      .filter(position -> position.isRowIndexLessThan(rowSize))
      .filter(position -> position.isColIndexLessThan(colSize))
      .toList();
  }

  private boolean isAllCellChecked() {
    Cells cells = Cells.from(board);
    return cells.isAllChecked();
  }

  private void changeGameStatusToWin() {
    gameStatus = GameStatus.WIN;
  }

  private Cell findCell(CellPosition cellPosition) {
    return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
  }
}
