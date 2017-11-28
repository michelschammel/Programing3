package controller;

import Services.SourceService;
import factories.SourceViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main_app.MainApp;
import utilities.StringUtilities;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.io.IOException;

import static controller.constants.SourceOverviewControllerConstants.*;
import static main_app.constants.MainAppConstants.QUELLEN_EDIT_DIALOG_FXML;

public class SourceOverviewController {
    @FXML
    private TableView<SourceViewInterface> sourceTable;
    @FXML
    private TableView<QuoteViewInterface> quoteTable;
    @FXML
    private TableColumn<SourceViewInterface, String> titelColumn;
    @FXML
    private TableColumn<SourceViewInterface, String> autorColumn;
    @FXML
    private TableColumn<QuoteViewInterface, String> zitatColumn;
    @FXML
    private TableView<TagViewInterface> tagTable;
    @FXML
    private TableColumn<TagViewInterface, String> tagColumn;

    @FXML
    private Label titelLabel;
    @FXML
    private Label autorLabel;
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
    //private MainApp mainApp;
    private ObservableList<SourceViewInterface> tmpList;
    private ObservableList<SourceViewInterface> searchSourceList;
    private ObservableList<SourceViewInterface> sourceList;
    private Stage stage;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        //initialize all lists that are used
        initializeSourceList();
        // Initialize the source table with the two columns.
        titelColumn.setCellValueFactory(cellData -> cellData.getValue().getTitleProperty());
        autorColumn.setCellValueFactory(cellData -> cellData.getValue().getAuthorProperty());
        // Initialize the zitat table with the column.
        zitatColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        // Initialize the tag table with the column.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());

        // Clear source details.
        showQuellenDetails(null);

        // Listen for selection changes and show the quelle details when changed.
        sourceTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuellenDetails(newValue));

        //Set Rowfactory for zitatTable
        this.quoteTable.setRowFactory(tv -> {
            TableRow<QuoteViewInterface> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
//                final Zitat zitat = row.getItem();
                final QuoteViewInterface quote = row.getItem();
                if (row.isHover() && quote != null) {
                    Tooltip zitatToolTip = new Tooltip(quote.getText());
                    zitatToolTip.setWrapText(true);
                    zitatToolTip.setMaxWidth(160);
                    row.setTooltip(zitatToolTip);
                }
            });
            return row;
        });
    }

    private void initializeSourceList() {
//        SourceDatabaseInterface sourceDatabase = new SourceDatabaseImpl();
        //initialize a specific list just to show sources from a search
        searchSourceList = FXCollections.observableArrayList();
        //initialize the list that contains all existing sources
        this.sourceList = FXCollections.observableArrayList();
        //fill the source list with the data from the database
//        this.sourceList = sourceDatabase.getQuellenFromDataBase();
        this.sourceList = SourceService.getSourcesFromDatabase();
        //just a temporary list
        this.tmpList = sourceList;
        //set the list for the table
        sourceTable.setItems(sourceList);
    }

    /**
     * Fills all text fields to show details about the quelle.
     * If the specified quelle is null, all text fields are cleared.
     *
     * @param source the quelle or null
     */
    private void showQuellenDetails(SourceViewInterface source) {
        ObservableList<TagViewInterface> tagList = FXCollections.observableArrayList();
        if (source != null) {
            quoteTable.setEditable(true);
            // Fill the labels with info from the person object.
            titelLabel.setText(source.getTitle());
            autorLabel.setText(source.getAuthor());
            //Add observable list date to zitat table for every quelle.
            quoteTable.setItems(source.getQuoteList());
            //Iterate over the quelle list because every quelle can have multiple zitate
            //Add all tags to a new observable
            //prevent duplicates in tagList
            source.getQuoteList().forEach( quote ->
                    quote.getTagList().forEach(tag -> {
                        boolean addTag = true;
                        for (TagViewInterface aTagList : tagList) {
                            if (tag.getText().equals(aTagList.getText())) {
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
            //jahrLabel.setTitel("");
        }
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteQuelle() {
        int selectedIndex = sourceTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
//            SourceDatabaseInterface quellenService = new SourceDatabaseImpl();
//            quellenService.deleteQuelle(sourceTable.getSelectionModel().getSelectedItem());
            SourceService.deleteSource(sourceTable.getSelectionModel().getSelectedItem());
            sourceTable.getItems().remove(selectedIndex);
        } else {
            nothingSelected(NICHTS_AUSGEWAEHLT, KEINE_QUELLE_AUSGEWAEHLT, BITTE_QUELLE_AUS_TABELLE);
        }
    }

    @FXML
    private void handleNewSource() {
        SourceViewInterface source = SourceViewFactory.produceSourceView(SourceViewFactory.SOURCE_VIEW);
        showEditSource("Neue Quelle", source, false, true);
    }

    @FXML
    private void handleEditSource() {
        SourceViewInterface selectedSource = sourceTable.getSelectionModel().getSelectedItem();
        if (selectedSource != null) {
            showEditSource("Bearbeite Quelle", selectedSource, true, false);
        } else {
            nothingSelected(NICHTS_AUSGEWAEHLT, KEINE_QUELLE_AUSGEWAEHLT, BITTE_QUELLE_AUS_TABELLE);
        }
    }

    private void showEditSource(String title, SourceViewInterface selectedSource, boolean editmode, boolean subcategory) {
        int index = this.sourceList.indexOf(selectedSource);
        if (index < 0) {
            index = 0;
        }
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(QUELLEN_EDIT_DIALOG_FXML));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SourceEditDialogController controller = loader.getController();
            controller.disableSubCategory(!subcategory);
            //controller.setEditmode(editmode);
            controller.setDialogStage(dialogStage);
            controller.setSource(selectedSource);

            ObservableList<QuoteViewInterface> quoteList = FXCollections.observableArrayList();

            this.sourceList.forEach(source ->
                    quoteList.addAll(source.getQuoteList())
            );

            controller.setQuoteList(quoteList);

            dialogStage.showAndWait();
            if (controller.isOkClicked()) {
                this.sourceList.remove(selectedSource);
                SourceViewInterface source = controller.getUpdatedSource();
                this.sourceList.add(index, source);
                this.sourceTable.setItems(this.sourceList);
                SourceService.updateSource(source);
            }
//            SourceDatabaseInterface quellenService = new SourceDatabaseImpl();
//            quellenService.updateQuery(quelle);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nothingSelected(String title, String header, String content) {
        // Nothing selected.
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
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
                sourceList = tmpList;
            }
            this.searchSourceList = FXCollections.observableArrayList();
            Boolean addSource;
            for (SourceViewInterface source : this.sourceList) {
                addSource = false;
                if (searchAuthor) {
                    addSource = doesAuthorContainString(source, searchText);
                }

                if (searchSource && !addSource) {
                    addSource = doesSourceContainString(source, searchText);
                }

                if (searchQuote && !addSource) {
                    addSource = doesQuoteContainString(source, searchText);
                }

                if (searchTag && !addSource) {
                    addSource = doesTagContainString(source, searchText);
                }

                if (addSource) {
                    searchSourceList.add(source);
                }
            }
            tmpList = sourceList;
            sourceList = searchSourceList;
            this.sourceTable.setItems(sourceList);
            this.sourceTable.getSelectionModel().select(0);
        }
    }

    private boolean doesAuthorContainString(SourceViewInterface source, String searchString) {
        return StringUtilities.doesStringContain(source.getAuthor(), searchString);
    }

    private boolean doesSourceContainString(SourceViewInterface source, String searchString) {
        return StringUtilities.doesStringContain(source.getTitle(), searchString);
    }

    private boolean doesQuoteContainString(SourceViewInterface source, String searchStrring) {
        for (QuoteViewInterface quote : source.getQuoteList()) {
            if (StringUtilities.doesStringContain(quote.getText(), searchStrring)) {
                return true;
            }
        }
        return false;
    }

    private boolean doesTagContainString(SourceViewInterface source, String searchString) {
        for (QuoteViewInterface quote : source.getQuoteList()) {
            for (TagViewInterface tag : quote.getTagList()) {
                if (StringUtilities.doesStringContain(tag.getText(), searchString)) {
                    return true;
                }
            }
        }
        return false;
    }


    @FXML
    private void reset() {
        this.sourceList = tmpList;
        this.sourceTable.setItems(this.sourceList);
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}