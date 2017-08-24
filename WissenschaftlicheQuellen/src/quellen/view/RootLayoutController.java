package quellen.view;


import javafx.fxml.FXML;
import quellen.MainApp;


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

    //TODO make this work
    @FXML
    private void handleShowBarChart() {
      mainApp.showBarChart();
    }

    //TODO and this aswell
    @FXML
    private void handleShowPieChart() {
    	mainApp.showBarChart();
    }

}