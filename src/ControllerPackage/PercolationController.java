package ControllerPackage;

import cellsociety.Cell;
import cellsociety.FileReader;
import javafx.scene.Group;
import javafx.scene.paint.Color;



public class PercolationController extends Controller {

  public PercolationController(Group simGroup, FileReader reader) {
    super(simGroup, reader);
  }

  @Override
  protected void initializeModel() {
    for (int i = 0; i < WIDTH_CELLS * HEIGHT_CELLS; i++) {
      int x = i / WIDTH_CELLS;
      int y = i % WIDTH_CELLS;
      Cell cell = currentModel.getCell(x, y);d
      cell.setCurrentState("OPEN");
      calcNewDisplay(cell);
    }
  }

  @Override
  protected void updateGrid() {
    for (int i = 0; i < WIDTH_CELLS * HEIGHT_CELLS; i++) {
      int x = i % WIDTH_CELLS;
      int y = i / WIDTH_CELLS;
      updateCell(x, y);
    }
  }

  @Override
  protected void updateCell(int x, int y) {
    Cell current = currentModel.getCell(x, y);
    if (current.getCurrentState().equals("CLOSED")){
      current.setNextState("CLOSED");
      return;
    }
    if (y == 0 && current.getCurrentState().equals("OPEN")) {
      current.setNextState("PERC");
      return;
    }

    /*
    for (Cell c : currentModel.getNeighborhood(x, y)) {
      if (c.getCurrentState().equals("PERC")) {
        current.setNextState("PERC");
        return;
      }
    }*/
    current.setNextState(current.getCurrentState());
  }

  @Override
  protected void calcNewDisplay(Cell cell) {
    if (cell.getCurrentState().equals("OPEN")) {
      cell.setDisplayColor(Color.WHITE);
    } else if (cell.getCurrentState().equals("PERC")) {
      cell.setDisplayColor(Color.BLUE);
    }
  }
}
