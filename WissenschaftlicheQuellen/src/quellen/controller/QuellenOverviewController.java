package quellen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import quellen.MainApp;
import quellen.model.Quelle;
import quellen.model.Zitat;
import quellen.model.Tag;
import quellen.service.QuellenService;
import static quellen.constants.Controller_Constants.*;

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
    private ObservableList<Quelle> tmpList;
    private ObservableList<Quelle> searchQuelleList;
    private ObservableList<Quelle> quelleList;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     * @author Cedric Schreiner
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

        //Set Rowfactory for zitatTable
        this.zitatTable.setRowFactory(tv -> {
            TableRow<Zitat> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                final Zitat zitat = row.getItem();
                if (row.isHover() && zitat != null) {
                    Tooltip zitatToolTip = new Tooltip(zitat.getText());
                    zitatToolTip.setWrapText(true);
                    zitatToolTip.setMaxWidth(160);
                    row.setTooltip(zitatToolTip);
                }
            });
            return row;
        });

        //initialize lists
        searchQuelleList = FXCollections.observableArrayList();
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
            //prevent duplicates in tagList
            quelle.getZitatList().forEach( zitat ->
                    zitat.getTagList().forEach(tag -> {
                        boolean addTag = true;
                        for (int i = 0; i < tagList.size(); i++) {
                            if (tag.getText().equals(tagList.get(i).getText())) {
                                addTag = false;
                            }
                        }
                        if(addTag) {
                            tagList.add(tag);
                        }
                    })
            );
            //Add the new observable to the tagTable
            tagTable.setItems(tagList);
        } else {
            // Person is null, remove all the text.
            titelLabel.setText(LEERER_STRING);
            autorLabel.setText(LEERER_STRING);
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
            QuellenService quellenService = new QuellenService();
            quellenService.deleteQuelle(quellenTable.getSelectionModel().getSelectedItem());
            quellenTable.getItems().remove(selectedIndex);
        } else {
            nothingSelected(NICHTS_AUSGEWAEHLT, KEINE_QUELLE_AUSGEWAEHLT, BITTE_QUELLE_AUS_TABELLE);
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new quelle.
     */
    @FXML
    private void handleNewQuelle() {
        Quelle tempQuelle = new Quelle(LEERER_STRING, LEERER_STRING, LEERER_STRING);
        boolean okClicked = mainApp.showQuellenEditDialog(tempQuelle, false, "Neue Quelle");
        try {
            if (okClicked) {
                //Get edited quelle
                tempQuelle = this.mainApp.getUpdatedQuelle();
                QuellenService quellenService = new QuellenService();
                //Insert edited quelle into DB and return the id of quelle
                tempQuelle.setId(quellenService.insertNewQuelle(tempQuelle));
                //insert quelle into mainApp
                this.mainApp.getQuellenList().add(tempQuelle);
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
        //index is needed to insert the new Object at the same place
        int index;
        Quelle selectedQuelle = quellenTable.getSelectionModel().getSelectedItem();
        index = quellenTable.getSelectionModel().getSelectedIndex();

        if (selectedQuelle != null) {
            boolean okClicked = mainApp.showQuellenEditDialog(selectedQuelle, true, "Bearbeite Quelle");
            if (okClicked) {
                //remove the old quelle
                this.mainApp.getQuellenList().remove(selectedQuelle);
                selectedQuelle =  mainApp.getUpdatedQuelle();
                //add the edited version into table
                this.mainApp.getQuellenList().add(index, selectedQuelle);
                this.quellenTable.setItems(this.mainApp.getQuellenList());
                //because the selected quelle was deleted we have to set the selection on the new quelle
                this.quellenTable.getSelectionModel().select(index);
                try {
                    QuellenService quellenService = new QuellenService();
                    quellenService.updateQuelle(selectedQuelle);
                    //Datenbank.getInstance().updateQuery(selectedQuelle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showQuellenDetails(selectedQuelle);
            }

        } else {
            nothingSelected(NICHTS_AUSGEWAEHLT, KEINE_QUELLE_AUSGEWAEHLT, BITTE_QUELLE_AUS_TABELLE);
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
        tmpList = mainApp.getQuellenList();
        quelleList = mainApp.getQuellenList();
        // Add observable list data to the quellen table.
        quellenTable.setItems(mainApp.getQuellenList());
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
        String searchText = searchTextField.getText().toLowerCase();
        //if the text is empty throw a message
        if(searchText.isEmpty() || !searchTag && !searchSource && !searchQuote && !searchAuthor) {
            nothingSelected(ERROR, KEINE_SUCHE_MOEGLICH, KEINE_EINGABE_ZUM_SUCHEN_MOEGLICH);
        } else {
            if (tmpList != null) {
                quelleList = tmpList;
            }
            this.searchQuelleList = FXCollections.observableArrayList();
            mainApp.getQuellenList().forEach( quelle -> {
                Boolean addQuelle = false;
                if (searchAuthor) {
                    if (quelle.getAutor().toLowerCase().contains(searchText)) {
                        //Add to list
                        addQuelle = true;
                    }
                }

                if (searchSource && !addQuelle) {
                    if (quelle.getTitel().toLowerCase().contains(searchText)) {
                        addQuelle = true;
                    }
                }

                if (searchQuote && !addQuelle) {
                    for (int i = 0; i < quelle.getZitatList().size(); i++) {
                        if (quelle.getZitatList().get(i).getText().toLowerCase().contains(searchText)) {
                            addQuelle = true;
                            //quelle contains a zitat that fits the search criteria so we dont need
                            //to search any further
                            break;
                        }
                    }
                }

                if (searchTag && !addQuelle) {
                    for (Zitat zitat : quelle.getZitatList()) {
                        for (Tag tag : zitat.getTagList()) {
                            if (tag.getText().toLowerCase().contains(searchText)) {
                                addQuelle = true;
                                break;
                            }
                        }
                        if (addQuelle) {
                            break;
                        }
                    }
                }

                if(addQuelle) {
                    searchQuelleList.add(quelle);
                }
            });
            tmpList = quelleList;
            quelleList = searchQuelleList;
            this.quellenTable.setItems(quelleList);
            this.quellenTable.getSelectionModel().select(0);
        }
    }

    public void reset() {
        this.quelleList = tmpList;
        this.quellenTable.setItems(this.quelleList);
    }

}

