package controller;


import javafx.fxml.FXML;
import main_app.MainApp;

/**
 * @author Bjoern Schmidt
 * @author Roman Berezin
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void handleShowPieChart() {
        mainApp.showPieChart();
    }


    @FXML
    private void handleShowAbout() {
        mainApp.showAbout();
    }


    @FXML
    private void handleShowTutorial() {
        mainApp.showTutorial();
    }


}