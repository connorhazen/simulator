package View;

import cellsociety.Main;
import cellsociety.Model;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.util.ResourceBundle;

public class UserInterface {
    private static final String TITLE = "Simulation_Team05";
    private static final int HEIGHT = 300; // temp
    private static final int WIDTH = 300; //temp

    private static final String RESOURCES = "resources";
    public static final String DEFAULT_RESOURCE_PACKAGE = RESOURCES + ".";
    public static final String DEFAULT_RESOURCE_FOLDER = "/" + RESOURCES + "/";
    public static final String STYLESHEET = "resources/default.css";
    public static final String BLANK = " ";

    private ResourceBundle myResources;

    private Button myPauseButton = new Button();
    private Button myPlayButton = new Button();
    private Button myResetButton = new Button();
    private Button myStepButton = new Button();
    private Slider mySlider = new Slider();
    private ComboBox<String> myDropDown = new ComboBox();

    private String[] mySims;
    private Timeline myAnimation;

    public boolean isPaused;
    public boolean isReset;

    public UserInterface(Stage stage, String language, String[] simNames, Timeline animation) {
        stage.setTitle(TITLE);
        myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + language);
        this.mySims = simNames;
        this.isPaused = true;
        this.myAnimation = animation;
    }
    public Scene setupUI(Group viewGroup) {
        BorderPane bp = new BorderPane();
        initSimSelect(event -> setSim(), mySims);
        bp.setTop(myDropDown);
        bp.setCenter(viewGroup);
        bp.setBottom(initControls());
        Scene myScene = new Scene(bp, 600, 600);
        myScene.getStylesheets().add(STYLESHEET);
        return myScene;
    }
    private Node initControls() {
        VBox v = new VBox();
        HBox h = new HBox();
        myPlayButton = makeButton("PLAYCOMMAND", event -> checkPlay());
        myPauseButton = makeButton("PAUSECOMMAND", event -> checkPause());
        myResetButton = makeButton("RESETCOMMAND", event -> checkReset());
        myStepButton = makeButton("UPDATECOMMAND", event -> checkUpdate());
        mySlider = makeSlider(event -> handleSlider());
        v.getChildren().add(myPlayButton);
        v.getChildren().add(myPauseButton);
        v.getChildren().add(myStepButton);
        v.getChildren().add(myResetButton);
        h.getChildren().add(v);
        h.getChildren().add(mySlider);
        return h;
    }
    private Slider makeSlider(EventHandler<MouseEvent> handler) {
        Slider mySlider = new Slider();
        mySlider.setOnMouseReleased(handler);
        mySlider.setMin(0);
        mySlider.setMax(25);
        mySlider.setValue(1);
        mySlider.setShowTickLabels(true);
        mySlider.setShowTickMarks(true);
        mySlider.setMajorTickUnit(5);
        mySlider.setMinorTickCount(1);
        mySlider.setBlockIncrement(10);
        return mySlider;
    }
    private void handleSlider() {
        myAnimation.setRate(mySlider.getValue());
    }
    private void initSimSelect(EventHandler<ActionEvent> handler, String[] simNames) {
        myDropDown.setOnAction(handler);
        for (String s : simNames) {
            myDropDown.getItems().add(s);
        }
    }
    public String setSim() {
        String st = myDropDown.getValue();
        System.out.println(st);
        return st;
    }

    private Button makeButton (String property, EventHandler<ActionEvent> handler) {
        // represent all supported image suffixes
        final String IMAGEFILE_SUFFIXES = String.format(".*\\.(%s)", String.join("|", ImageIO.getReaderFileSuffixes()));
        Button result = new Button();
        String label = myResources.getString(property);
        if (label.matches(IMAGEFILE_SUFFIXES)) {
            result.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(DEFAULT_RESOURCE_FOLDER + label))));
        }
        else {
            result.setText(label);
        }
        result.setOnAction(handler);
        return result;
    }
    private void checkPause() {
        this.isPaused = true;
    }
    private void checkPlay() {
        this.isPaused = false;
    }
    private void checkReset() {
        this.isReset = true;
    }
    private void checkUpdate() {
    }
}
