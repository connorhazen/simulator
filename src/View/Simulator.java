package View;

import controllerPackage.Controller;
import controllerPackage.FireController;
import controllerPackage.GameOfLifeController;
import controllerPackage.PercolationController;
import controllerPackage.PredPreyController;
import controllerPackage.SegregationController;
import View.UserInterface;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import utils.Cell;
import utils.FileReader;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.XMLException;

import javax.naming.ldap.Control;
import java.io.File;
import java.util.Optional;


/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Simulator {


  public static final int FRAMES_PER_SECOND = 1;
  private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

  private static final String EXTENSION = ".xml";

  private Controller currentController;
  private UserInterface UI;
  private ControlPanel myControlPanel;
  private final Group viewGroup = new Group();
  private Scene myScene;

  private String mySim;
  private String myNewSim;
  private ArrayList<String> simNames;
  private Timeline myAnimation;

  private boolean b;

  /**
   * Start of the program.
   */
  public Simulator(Stage stage, boolean b, String sim) {
    getFileNames();
    myAnimation = new Timeline();
    myControlPanel = new ControlPanel(myAnimation);
    UI = new UserInterface(stage, "English", simNames, myControlPanel, b);
    myScene = UI.setupUI(viewGroup);
    stage.setScene(myScene);
    stage.show();
    if (!b) {
      initialize(UI.getSim());
    }
    else {
      initialize(sim);
    }
    this.b = b;
  }
  public void initialize(String sim){
      FileReader reader = new FileReader(sim + EXTENSION);
      mySim = reader.getSimType();
      checkSimName(mySim, reader, false);

    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
        step();

    });

    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(frame);
    myAnimation.play();
  }
  private void step(){
      myNewSim = UI.getSim();
      if (!myNewSim.equals("Switch Simulation") && !myNewSim.equals(mySim)) {
        currentController.clear();
        myControlPanel.setPause();
        FileReader reader = new FileReader(myNewSim + EXTENSION);
        mySim = reader.getSimType();
        checkSimName(mySim, reader, true);
      }
    if (myControlPanel.getSimLoadStatus() && mySim != null) {
      if (!myControlPanel.getPauseStatus() || myControlPanel.getUpdateStatus()) {
        currentController.updateSim();
        myControlPanel.resetControl();
      }
      if (myControlPanel.getResetStatus()) {
        if (myGraph != null) {
          myGraph.clear();
          myGraph.reinit();
        }
        currentController.resetSim();
        myControlPanel.resetControl();
      }
    }
  }

  private void getFileNames() {
    File folder = new File("data/");
    File[] listOfFiles = folder.listFiles();
    simNames = new ArrayList<>();
    assert listOfFiles != null;
    for (File listOfFile : listOfFiles) {
      if (listOfFile.isFile() && listOfFile.getName().contains(EXTENSION)) {
        simNames.add(listOfFile.getName().split(EXTENSION)[0]);
      }
    }
  }
  private PredPreyGraph myGraph = null;
  private void checkSimName(String name, FileReader reader, boolean isUpdating) {
    switch (name) {
      case "Percolation":
        currentController = new PercolationController(viewGroup, reader);
        break;
      case "Segregation":
        currentController = new SegregationController(viewGroup, reader);
        break;
      case "Fire":
        currentController = new FireController(viewGroup, reader);
        break;
      case "GameOfLife":
        currentController = new GameOfLifeController(viewGroup, reader);
        break;
      case "PredatorPrey":
        myGraph = UI.addPredChart();
        currentController = new PredPreyController(viewGroup, reader, myGraph);
        break;

    }
    if (isUpdating) {
      mySim = name.toLowerCase();
    }
    if (! name.equals("PredatorPrey") && myGraph != null) {
      UI.removeGraph();
    }

  }
//  public void checkCellClick(CellVisual cv, Cell cell, Controller c, FileReader r) {
//    ChoiceDialog<String> cd = new ChoiceDialog<>(r.getString("state0Color"));
//    cd.setTitle("State");
//    cd.setContentText("Choose cell state");
//    Optional<String> res = cd.showAndWait();
//    res.ifPresent(event -> {
//      c.updateCell(cell.getX(), cell.getY());
//    });
//  }
}
