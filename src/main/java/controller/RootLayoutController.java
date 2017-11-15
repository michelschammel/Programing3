package controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main_app.MainApp;
import utilities.ViewUtillities;

import java.io.IOException;

import static main_app.constants.MainAppConstants.*;

/**
 * @author Bjoern Schmidt
 * @author Roman Berezin
 */
public class RootLayoutController {

    // Reference to the main application
    //private MainApp mainApp;
    @FXML
    private BorderPane rootBorderPane;
    private Stage stage;

    public void showSourceOverview() {
        try {
            // Load source overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(QUELLEN_OVERVIEW_FXML));
            AnchorPane quellenOverview = loader.load();

            // Set source overview into the center of root layout.
            rootBorderPane.setCenter(quellenOverview);

            // Give the controller access to the main app.
            SourceOverviewController controller = loader.getController();
            controller.setStage(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleShowPieChart() {
        ViewUtillities.showScreen(PIE_CHART_FXML, KUCHEN_DIAGRAMM, this.stage);
    }


    @FXML
    private void handleShowAbout() {
        ViewUtillities.showScreen(ABOUT_FXML, TUTORIAL, this.stage);
    }


    @FXML
    private void handleShowTutorial() {
        ViewUtillities.showScreen(TUTORIAL_FXML, ABOUT, this.stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}