package cleancode.minesweeper.tobe.minesweeper.board.position;

import java.util.Objects;

public class CellPosition {
  private final int rowIndex;
  private final int columnIndex;

  public CellPosition(int rowIndex, int columnIndex) {
    if (rowIndex < 0 || columnIndex < 0) {
      throw new IllegalArgumentException("Invalid rowIndex or columnIndex");
    }
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
  }

  public static CellPosition of(int rowIndex, int columnIndex) {
    return new CellPosition(rowIndex, columnIndex);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CellPosition that))
      return false;
    return rowIndex == that.rowIndex && columnIndex == that.columnIndex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rowIndex, columnIndex);
  }

  public boolean isRowIndexMoreThanOrEqual(int rowIndex) {
    return this.rowIndex >= rowIndex;
  }

  public boolean isColIndexMoreThanOrEqual(int columnIndex) {
    return this.columnIndex >= columnIndex;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getColIndex() {
    return columnIndex;
  }

  public boolean canCalculatePositionBy(RelativePosition relativePosition) {
    return this.rowIndex + relativePosition.getDeltaRow() >= 0 && this.columnIndex + relativePosition.getDeltaCol() >= 0;
  }

  public CellPosition calculatePositionBy(RelativePosition relativePosition) {
    if (canCalculatePositionBy(relativePosition)) {
      return CellPosition.of(this.rowIndex + relativePosition.getDeltaRow(),
        this.columnIndex + relativePosition.getDeltaCol());
    }
    throw new IllegalArgumentException("움직일 수 있는 좌표가 아닙니다.");
  }

  public boolean isRowIndexLessThan(int rowIndex) {
    return this.rowIndex < rowIndex;
  }

  public boolean isColIndexLessThan(int columnIndex) {
    return this.columnIndex < columnIndex;
  }

}
