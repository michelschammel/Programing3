package quellen.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import quellen.MainApp;
import quellen.model.Quelle;
import quellen.model.Zitat;

public class QuellenOverviewController {
    @FXML
    private TableView<Quelle> quellenTable;
    @FXML
    private TableView<Zitat> zitatTable;
    @FXML
    private TableColumn<Quelle, String> titelColumn;
    @FXML
    private TableColumn<Quelle, String> autorColumn;
    @FXML
    private TableColumn<Zitat, String> zitatColumn;

    @FXML
    private Label titelLabel;
    @FXML
    private Label autorLabel;
    @FXML
    private Label jahrLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public QuellenOverviewController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the quellen table with the two columns.
        titelColumn.setCellValueFactory(cellData -> cellData.getValue().titelProperty());
        autorColumn.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
        // Initialize the zitat table with the column.
        zitatColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        
        // Clear quellen details.
        showQuellenDetails(null);

        // Listen for selection changes and show the quelle details when changed.
        quellenTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuellenDetails(newValue));
        
    }
    
    /**
     * Fills all text fields to show details about the quelle.
     * If the specified quelle is null, all text fields are cleared.
     * 
     * @param quelle the quelle or null
     */
    private void showQuellenDetails(Quelle quelle) {
        if (quelle != null) {
            // Fill the labels with info from the person object.
            titelLabel.setText(quelle.getTitel());
            autorLabel.setText(quelle.getAutor());
            //jahrLabel.setText(quelle.getJahr());
            
            //Add observable list date to zitat table for every quelle.
            zitatTable.setItems(quelle.getZitatList());
        } else {
            // Person is null, remove all the text.
            titelLabel.setText("");
            autorLabel.setText("");
            //jahrLabel.setText("");
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteQuelle() {
        int selectedIndex = quellenTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            quellenTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Quelle Selected");
            alert.setContentText("Please select a quelle in the table.");

            alert.showAndWait();
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
        // Add observable list data to the quellen table.
        quellenTable.setItems(mainApp.getQuellenList());
    }
}

