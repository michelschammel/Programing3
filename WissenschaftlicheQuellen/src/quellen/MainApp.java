package quellen;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quellen.Interfaces.SourceDatabaseInterface;
import quellen.dao.SourceDatabaseControl;
import quellen.model.*;
import quellen.controller.QuellenEditDialogController;
import quellen.controller.QuellenOverviewController;
import quellen.controller.RootLayoutController;

import static quellen.constants.Controller_Constants.*;


/**
 * @author Björn Schmidt
 * @author Michel Schammel
 * @author Roman Berezin
 * @author Cedric Schreiner
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Quelle updatedQuelle;
    private Quelle selectedQuelleForEdit;

    /**
     * The data as an observable list of Quelle.
     */
    private ObservableList<Quelle> quellenData= FXCollections.observableArrayList();

    /**
     * The Constructor.
     * @throws SQLException
     */
    public MainApp() throws SQLException {
        //quellenData = Datenbank.getInstance().getQuellenFromDataBase();
        SourceDatabaseInterface quellenService = new SourceDatabaseControl();
        quellenData = quellenService.getQuellenFromDataBase();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WISSENSCHAFTLICHE_QUELLEN);

        initRootLayout();

        showQuellenOverview();
    }




    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(ROOT_LAYOUT_FXML));
            rootLayout = (BorderPane) loader.load();
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the quellen overview inside the root layout.
     */
    public void showQuellenOverview() {
        try {
            // Load quellen overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(QUELLEN_OVERVIEW_FXML));
            AnchorPane quellenOverview = (AnchorPane) loader.load();

            // Set quellen overview into the center of root layout.
            rootLayout.setCenter(quellenOverview);

            // Give the controller access to the main app.
            QuellenOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified quelle. If the user
     * clicks OK, the changes are saved into the provided quellen object and true
     * is returned.
     *
     * @param quelle the quelle object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showQuellenEditDialog(Quelle quelle, boolean editmode, String titel) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(QUELLEN_EDIT_DIALOG_FXML));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(titel);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the quelle into the controller.
            QuellenEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setQuelle(quelle);

            if (editmode) {
                controller.disableSubCategory(true);
            } else {
                controller.setEditmode(false);
            }
            //

            ObservableList<Zitat> zitatList = FXCollections.observableArrayList();
            quellenData.forEach( quelleList ->
                zitatList.addAll(quelleList.getZitatList())
            );
            controller.setZitatList(zitatList);

            //set selectedQuelle for the edit dialig
            this.selectedQuelleForEdit = quelle;

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            //get the updateded Quelle
            this.updatedQuelle = controller.getUpdatedQuelle();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Quelle getUpdatedQuelle() {
        return this.updatedQuelle;
    }

    public Quelle getSelectedQuelle() {
        return this.selectedQuelleForEdit;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Quelle> getQuellenList() {
        return this.quellenData;
    }

    public void setQuellenList(ObservableList<Quelle> quellenData) {
        this.quellenData = quellenData;
    }

    public void showPieChart() {
        showScreen(PIE_CHART_FXML, KUCHEN_DIAGRAMM);
    }


    public void showTutorial() {
        showScreen(TUTORIAL_FXML, TUTORIAL);
    }

    public void showAbout() {
        showScreen(ABOUT_FXML, ABOUT);
    }

    private void showScreen(String resource, String titel) {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(resource));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(titel);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);


            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
