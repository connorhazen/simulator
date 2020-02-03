package cellsociety;



import ControllerPackage.*;

import ControllerPackage.Controller;
import ControllerPackage.FireController;
import ControllerPackage.GameOfLifeController;
import ControllerPackage.PercolationController;
import ControllerPackage.PredPreyController;
import ControllerPackage.SegregationController;
import View.UserInterface;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;


/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main extends Application {


  public static final int FRAMES_PER_SECOND = 1;
  private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

  private static final String EXTENSION = ".xml";

  private Controller currentController;
  private UserInterface UI;
  private Group viewGroup = new Group();

  private String mySim;
  private String myNewSim;
  private ArrayList<String> simNames;

  /**
   * Start of the program.
   */
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {

    File folder = new File("data/");
    File[] listOfFiles = folder.listFiles();

    simNames = new ArrayList<>();
    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(EXTENSION)) {
        simNames.add(listOfFiles[i].getName().split(EXTENSION)[0]);
      }
    }
    
    Timeline myAnimation = new Timeline();
    UI = new UserInterface(stage, "English", simNames, myAnimation);
    stage.setScene(UI.setupUI(viewGroup));
    stage.show();



    FileReader reader = new FileReader(UI.setSim()+EXTENSION);



    mySim = reader.getSimType();

    if (mySim.equals("Percolation")) {
        currentController = new PercolationController(viewGroup, reader);
    }
    else if (mySim.equals("Segregation")) {
        currentController = new SegregationController(viewGroup, reader);
    }
    else if (mySim.equals("Fire")){
        currentController = new FireController(viewGroup, reader);
    }
    else if (mySim.equals("GameOfLife")) {
        currentController = new GameOfLifeController(viewGroup, reader);
    }
    else if (mySim.equals("PredatorPrey")) {
        currentController = new PredPreyController(viewGroup, reader);
    }
    else {
        stage.close();
    }

    KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
        try {
            step();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(frame);
    myAnimation.play();
  }

  private void step() throws Exception {
    myNewSim = UI.setSim();
    if (! myNewSim.equals(mySim)) {
        currentController.clear();
        UI.isPaused=true;
        FileReader reader = new FileReader(myNewSim+EXTENSION);
        mySim = reader.getSimType();
        if (mySim.equals("Percolation")) {
            mySim = mySim.toLowerCase();
            currentController = new PercolationController(viewGroup, reader);
        }
        else if (mySim.equals("Segregation")) {
            mySim = mySim.toLowerCase();
            currentController = new SegregationController(viewGroup, reader);
        }
        else if (mySim.equals("Fire")){
            mySim = mySim.toLowerCase();
            currentController = new FireController(viewGroup, reader);
        }
        else if (mySim.equals("GameOfLife")) {
            mySim = "gameOfLife";
            currentController = new GameOfLifeController(viewGroup, reader);
        }
        else if (mySim.equals("PredatorPrey")) {
            mySim = "predatorPrey";
            currentController = new PredPreyController(viewGroup, reader);
        }
    }
    if (UI.isSimLoaded && mySim != null) {
      if (!UI.isPaused || (UI.isPaused && UI.isStep)) {

        currentController.updateSim();
        UI.isStep = false;
      }
      if (UI.isReset) {
        currentController.resetSim();
        UI.isReset = false;
      }
    }
  }

}
