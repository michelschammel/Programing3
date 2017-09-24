package quellen.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import quellen.MainApp;
import quellen.model.Datenbank;
import quellen.model.Quelle;
import quellen.model.Zitat;
import quellen.model.Tag;

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
    private TableView<Tag> tagTable;
    @FXML
    private TableColumn<Tag, String> tagColumn;

    @FXML
    private Label titelLabel;
    @FXML
    private Label autorLabel;
    @FXML
    private Label jahrLabel;
    @FXML
    private TextField searchTextField;

    @FXML
    private CheckBox checkBoxTag;
    @FXML
    private CheckBox checkBoxSource;
    @FXML
    private CheckBox checkBoxAuthor;
    @FXML
    private CheckBox checkBoxQuote;

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
        // Initialize the tag table with the column.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());

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
        ObservableList<Tag> tagList = FXCollections.observableArrayList();
        if (quelle != null) {
            zitatTable.setEditable(true);
            // Fill the labels with info from the person object.
            titelLabel.setText(quelle.getTitel());
            autorLabel.setText(quelle.getAutor());
            //jahrLabel.setText(quelle.getJahr());

            //Add observable list date to zitat table for every quelle.
            zitatTable.setItems(quelle.getZitatList());

            //Iterate over the quelle list because every quelle can have multiple zitate
            //Add all tags to a new observable
            quelle.getZitatList().forEach((zitat) ->
                tagList.addAll(zitat.getTagList())
            );
            //Add the new observable to the tagTable
            tagTable.setItems(tagList);
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
            nothingSelected("No Selection", "No Quelle Selected", "Please select a quelle in the table.");
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new quelle.
     */
    @FXML
    private void handleNewQuelle() {
        Quelle tempQuelle = new Quelle("", "", "");
        boolean okClicked = mainApp.showQuellenEditDialog(tempQuelle);
        try {
            if (okClicked) {
                Datenbank.updateDatabase("INSERT INTO Quellen(Autor, Titel, Jahr) VALUES ('" + tempQuelle.getAutor() + "', '" + tempQuelle.getTitel() + "', '" + tempQuelle.getJahr() + "')");
                mainApp.getQuellenList().add(tempQuelle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected quelle.
     */
    @FXML
    private void handleEditQuelle() {
        Quelle selectedQuelle = quellenTable.getSelectionModel().getSelectedItem();

        if (selectedQuelle != null) {
            boolean okClicked = mainApp.showQuellenEditDialog(selectedQuelle);
            if (okClicked) {
                selectedQuelle =  mainApp.getUpdatedQuelle();
                //update the Zitat table
                this.quellenTable.getSelectionModel().getSelectedItem().setZitatListe(selectedQuelle.getZitatList());
                showQuellenDetails(selectedQuelle);
            }

        } else {
            nothingSelected("No Selection", "No Quelle Selected", "Please select a quelle in the table.");
        }
    }

    private void nothingSelected(String title, String header, String content) {
        // Nothing selected.
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
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

	@FXML
	private void handleShowBarChart() {
	      mainApp.showBarChart();
	}

	@FXML
	private void handleShowPieChart() {
	      mainApp.showPieChart();
	}

    /**
     * Is called when the user clicks the search Button
     */
    @FXML
    private void handleSearch() {
        //get the values of the checkboxes
        boolean searchAuthor = checkBoxAuthor.isSelected();
        boolean searchSource = checkBoxSource.isSelected();
        boolean searchQuote = checkBoxQuote.isSelected();
        boolean searchTag = checkBoxTag.isSelected();
        //Get the text to search for
        String searchText = searchTextField.getCharacters().toString();
        //if the text is empty throw a message
        if(searchText.isEmpty()) {
            nothingSelected("No Search Text", "No search possible", "Please enter a text to search for");
        } else {
            this.mainApp.search(searchText, searchAuthor, searchTag, searchSource, searchQuote);
        }
    }


}

