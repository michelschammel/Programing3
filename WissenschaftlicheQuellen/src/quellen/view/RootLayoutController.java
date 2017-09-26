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

    @FXML
    private void handleShowBarChart() {
      mainApp.showBarChart();
    }

    @FXML
    private void handleShowPieChart() { mainApp.showPieChart(); }

    @FXML
    private void handlePDFOpen () { mainApp.openPDF (); }

    @FXML
    private void handleShowTutorial ()  { mainApp.showTutorial(); }

    @FXML
    private void handleShowAbout () { mainApp.showAbout(); }
}