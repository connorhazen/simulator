package controllerPackage;

import java.util.HashMap;
import javafx.scene.Group;
import utils.Cell;
import utils.FileReader;

/**
 * This class creates the Percolation simulation.
 *
 * It uses 3 states, empty, percolated, and blocked.
 *
 * The specific params are initial percent blocked.
 *
 * The percolation starts from the northern border.
 */
public class PercolationController extends Controller {

  private double percentBlocked;


  //EMPTY = 0 : PERC = 1 : BLOCKED : 2;
  public PercolationController(Group simGroup, FileReader reader, Group simUIGroup) {
    super(simGroup, reader, simUIGroup);
  }

  @Override
  protected HashMap<String, Object> getSimParamsForUi() {
    HashMap<String, Object> ret = new HashMap<>();
    ret.put("percentBlocked", percentBlocked);
    return ret;
  }

  @Override
  protected void setSimParamsFromUI() {
    HashMap<String, Object> values = (HashMap<String, Object>) simUI.getValues();
    percentBlocked = (double) values.get("percentBlocked");
  }


  @Override
  protected void initializeCellState(Cell current) {
    if (probabilityChecker(percentBlocked)) {
      current.setCurrentState(new State(2));
    } else {
      current.setCurrentState(new State(0));
    }
  }

  @Override
  protected void setSimParams() {
    percentBlocked = reader.getDoubleValue("percentBlocked");
    spacing = reader.getDoubleValue("spacing");
    maxState = 2;
    modelType = reader.getString("modelType");
  }

  @Override
  protected void updateCell(int x, int y) {
    Cell current = currentModel.getCell(x, y);
    if (current.getCurrentState().getState() == 2 || current.getCurrentState().getState() == 1) {
      current.setNextState(new State(current.getCurrentState().getState()));
    } else if (checkWater(current)) {
      current.setNextState(new State(1));
    } else {
      current.setNextState(new State(0));
    }
  }

  private boolean checkWater(Cell current) {
    if (current.getY() == 0) {
      return true;
    }
    for (Cell c : currentModel.getMooreNeighborhood(current.getX(), current.getY())) {
      if (c.getCurrentState().getState() == 1) {
        return true;
      }
    }
    return false;
  }
}
