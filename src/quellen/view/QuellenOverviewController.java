package quellen.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import quellen.MainApp;
import quellen.model.Zitat;

public class QuellenOverviewController {
    @FXML
    private TableView<Zitat> quellenTable;
    @FXML
    private TableColumn<Zitat, String> buchColumn;
    @FXML
    private TableColumn<Zitat, String> autorColumn;

    @FXML
    private Label buchLabel;
    @FXML
    private Label autorLabel;
    @FXML
    private Label textLabel;

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
        buchColumn.setCellValueFactory(cellData -> cellData.getValue().buchProperty());
        autorColumn.setCellValueFactory(cellData -> cellData.getValue().autorProperty());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        quellenTable.setItems(mainApp.getZitatData());
    }
}

