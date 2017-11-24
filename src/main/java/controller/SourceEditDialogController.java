package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main_app.MainApp;
import models.interfaces.ObjectTemplateInterface;
import utilities.SourceUtillities;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.io.IOException;
import java.util.List;

import static dao.constants.DB_Constants.*;
import static controller.constants.SourceEditDialogConstants.*;


/**
 * Dialog to edit details of a source.
 * @author Cedric Schreiner, Roman Berezin
 */
public class SourceEditDialogController {

    @FXML
    private TextField autorField;
    @FXML
    private TextField titelField;
    @FXML
    private TextField jahrField;
    @FXML
    private GridPane gridPane;
//    @FXML
//    private AnchorPane anchorPane;
//    @FXML
//    private Button okButton;
//    @FXML
//    private Button cancelButton;
    @FXML
    private TableView<QuoteViewInterface> zitatTable;
    @FXML
    private TableView<TagViewInterface> tagTable;
    @FXML
    private TableColumn<QuoteViewInterface, String> zitatColumn;
    @FXML
    private TableColumn<TagViewInterface, String> tagColumn;
    @FXML
    private ChoiceBox<String> subCategory;

    private Stage dialogStage;
    private SourceViewInterface source;
    private SourceViewInterface sourceEdited;
    private boolean okClicked = false;
    private ObservableList<QuoteViewInterface> quoteList;
    //    private SourceViewInterface editedSource;
    private List<ObjectTemplateInterface> template;

    //all custom textFields
//    private TextField herausgeberTextField;
//    private TextField auflageTextField;
//    private TextField monatTextField;
//    private TextField isbnTextField;
//    private TextField ausgabeTextField;
//    private TextField aufrufDatumTextField;
//    private TextField urlTextField;
//    private TextField einrichtungsTextField;
//    private TextField magazinTextField;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the zitat table with the two columns.
        this.zitatColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        this.tagColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());

        //Add a Cell Factory to be able to edit a column
        this.zitatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.tagColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Clear zitat details.
        showQuoteDetails(null);

        // Listen for selection changes and show the source details when changed.
        this.zitatTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showQuoteDetails(newValue));

        //initialize the choicebox
        subCategory.setItems((FXCollections.observableArrayList(
                SC_NONE, SC_ARTIKEL, SC_BUECHER, SC_OQUELLEN, SC_WARBEITEN, SC_ANDERES)));
        subCategory.setValue(SC_NONE);

        //set up a ChangeListener to choicebox
        subCategory.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> adjustNewDialog(newValue))
        );

        //Set Rowfactory for zitatTable
        this.zitatTable.setRowFactory(tv -> {
            TableRow<QuoteViewInterface> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
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

    private void initializeList() {
        this.quoteList = FXCollections.observableArrayList();
        quoteList.addAll(sourceEdited.getQuoteList());
    }

    void disableSubCategory(boolean disable) {
        this.subCategory.setDisable(disable);
    }

    /**
     * Fills all text fields to show details about the source.
     * If the specified source is null, all text fields are cleared.
     *
     * @param quote the zitat or null
     */
    private void showQuoteDetails(QuoteViewInterface quote) {
        if (quote != null) {
            tagTable.setItems(quote.getTagList());
        }
    }

    void setEditmode(boolean enable) {
        boolean editmode = enable;
    }

    /**
     * gets called if the user requests a context Menu
     */
    public void contextMenuTagTable() {
        if (this.zitatTable.getSelectionModel().getSelectedItem() != null) {
            //Create a new Contextmenu
            ContextMenu contextMenu = new ContextMenu();

            //create needed menuitems for the contextmenu
            MenuItem newTagItem = new MenuItem(NEUER_TAG);
            MenuItem deleteTagItem = new MenuItem(LOESCHE_TAG);
            MenuItem addTag = new MenuItem(FUEGE_EXISTIERENDEN_TAG_HINZU);

            //add the menuitems to the contextmenu
            contextMenu.getItems().addAll(newTagItem, deleteTagItem, addTag);

            //add the contextmenu to the table
            this.tagTable.setContextMenu(contextMenu);


            //add eventlistener for all menuitems
            newTagItem.setOnAction((ActionEvent event) -> {
                if (zitatTable.getSelectionModel().getSelectedItem() != null) {
                    //1. get zitatList of source
                    //2. get the selected itemindex of zitattable
                    //3. get the taglist of the selected zitat
                    //4. add a new tag to the zitat
                    TagViewInterface tag = new viewmodels.TagView();
                    tag.setText(NEU);
                    sourceEdited.getQuoteList().get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().add(tag);
                    //zitatList = sourceEdited.getZitatList();
                    //zitatList.get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().add(tag);
                }
            });

            deleteTagItem.setOnAction((ActionEvent event) -> {
                TagViewInterface tag = tagTable.getSelectionModel().getSelectedItem();
                sourceEdited.getQuoteList().get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().remove(tag);
            });

            addTag.setOnAction((ActionEvent event) -> {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource(ADD_TAGS_FXML));
                    AnchorPane page = loader.load();

                    // Create the addTag Stage.
                    Stage addTagStage = new Stage();
                    addTagStage.setTitle(FUEGE_TAG_HINZU);
                    addTagStage.initModality(Modality.WINDOW_MODAL);
                    Scene scene = new Scene(page);
                    addTagStage.setScene(scene);
                    addTagStage.initOwner(dialogStage);

                    // Set the source into the controller.
                    AddTagsController controller = loader.getController();
                    ObservableList<TagViewInterface> tagList = FXCollections.observableArrayList();
                    controller.setQuote(zitatTable.getSelectionModel().getSelectedItem());
                    //prevent duplicates
                    quoteList.forEach(zitat ->
                            zitat.getTagList().forEach(tag -> {
                                boolean addnewTag = true;
                                for (TagViewInterface aTagList : tagList) {
                                    if (tag.getText().equals(aTagList.getText())) {
                                        addnewTag = false;
                                    }
                                }
                                if (addnewTag) {
                                    tagList.add(tag);
                                }
                            })
                    );
                    controller.setTagList(tagList);

                    // Show the dialog and wait until the user closes it
                    addTagStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * gets called if the user requests a context Menu
     */
    public void contextMenuZitatTable() {
        //Create a new Contextmenu
        ContextMenu contextMenu = new ContextMenu();

        //create needed menuitems for the contextmenu
        MenuItem newZitatItem = new MenuItem(NEUES_ZITAT);
        MenuItem deleteZitatItem = new MenuItem(LOESCHE_ZITAT);
        MenuItem addZitatItem = new MenuItem(FUEGE_EXISTIERENDEN_ZITAT_HINZU);

        //add the menuitems to the contextmenu
        contextMenu.getItems().addAll(newZitatItem, deleteZitatItem, addZitatItem);

        //add the contextmenu to the table
        this.zitatTable.setContextMenu(contextMenu);

        //add eventlistener for both menuitems
        newZitatItem.setOnAction((ActionEvent event) -> {
            sourceEdited.getQuoteList().add(new viewmodels.QuoteView(BEARBEITE_MICH, sourceEdited.getId()));
            zitatTable.setItems(sourceEdited.getQuoteList());
        });

        deleteZitatItem.setOnAction((ActionEvent event) -> {
            QuoteViewInterface quote = zitatTable.getSelectionModel().getSelectedItem();
            sourceEdited.getQuoteList().remove(quote);
        });

        addZitatItem.setOnAction((ActionEvent event) -> {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource(ADD_ZITATE_FXML));
                AnchorPane page = loader.load();

                // Create the addTag Stage.
                Stage addZitatStage = new Stage();
                addZitatStage.setTitle(FUEGE_ZITAT_HINZU);
                addZitatStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(page);
                addZitatStage.setScene(scene);
                addZitatStage.initOwner(dialogStage);
                addZitatStage.setResizable(false);

                // Set the source into the controller.
                AddQuoteController controller = loader.getController();
                controller.setZitatList(quoteList);
                controller.setQuelle(sourceEdited);
                zitatTable.setItems(sourceEdited.getQuoteList());
                controller.setStage(addZitatStage);

                // Show the dialog and wait until the user closes it
                addZitatStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage stage for the dialog
     */
    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    SourceViewInterface getUpdatedSource() {
        if (okClicked) {
            SourceViewInterface source = SourceUtillities.convertUIGridPaneToSource(this.template, this.gridPane);
            source.getQuoteList().addAll(this.sourceEdited.getQuoteList());
//            return SourceUtillities.convertUIGridPaneToSource(this.template, this.gridPane);
            return source;
        }

        //the user canceld return the unchanged source
        return this.source;
    }

    /**
     * Sets the source to be edited in the dialog.
     * 
     * @param source this source is shown with it it attributes, Quotes and tags
     */
    public void setSource(SourceViewInterface source) {
        this.source = source;
        this.sourceEdited = SourceUtillities.copySourceView(source);

        this.zitatTable.setItems(this.sourceEdited.getQuoteList());

        autorField.setText(source.getAuthor());
        titelField.setText(source.getTitle());
        jahrField.setText(source.getYear());

        //adjustGridPane();
        this.template = SourceUtillities.getUIGridPane(source, this.gridPane);
        //SourceViewInterface s = (SourceViewInterface)SourceUtillities.convertUIGridPaneToSource(this.template, this.gridPane);
        initializeList();
    }

    public void setQuoteList(ObservableList<QuoteViewInterface> quoteList) {
        this.quoteList = quoteList;
    }


    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
//        if (isInputValid()) {
            source.setAuthor(autorField.getText());
            source.setTitle(titelField.getText());
            source.setYear(jahrField.getText());
            //source.setUnterkategorie(getSubCategory());

            okClicked = true;
            dialogStage.close();
//        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

//    /**
//     * Validates the user input in the text fields.
//     *
//     * @return true if the input is valid
//     */
//    private boolean isInputValid() {
//        String errorMessage = ERROR_MESSAGE;
//
//        if (autorField.getText() == null || autorField.getText().length() == 0) {
//            errorMessage += AUTOR_NICHT_AUSGEFUELLT;
//        }
//        if (titelField.getText() == null || titelField.getText().length() == 0) {
//            errorMessage += TITEL_NICHT_AUSGEFUELLT;
//        }
//        if (jahrField.getText() == null || jahrField.getText().length() == 0) {
//            errorMessage += JAHR_NICHT_AUSGEFUELLT;
//        }
//
//        if (errorMessage.length() == 0) {
//            return true;
//        } else {
//            // Show the error message.
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.initOwner(dialogStage);
//            alert.setTitle(FLASCHE_FELDER);
//            alert.setHeaderText(KORRIGIERE_FALSCHE_FELDER);
//            alert.setContentText(errorMessage);
//
//            alert.showAndWait();
//
//            return false;
//        }
//    }

//    private String getSubCategory() {
//        return subCategory.getValue();
//    }

    private void adjustNewDialog(String category) {
//        //child node count
//        int count = this.gridPane.getChildren().size();
//        //Create a RowContraints
//        RowConstraints rowConstraint = new RowConstraints();
//        rowConstraint.setMinHeight(ROW_CONTRAINTS_HEIGHT);
//        rowConstraint.setPrefHeight(ROW_CONTRAINTS_HEIGHT);
//        rowConstraint.setVgrow(Priority.SOMETIMES);
//        //Remove all items from another source
//        if (!editmode) {
//            if (count >= 8) {
//                this.gridPane.getChildren().remove(8, count);
//            }
//            switch (category) {
//                case SC_ARTIKEL:
//                    this.source = new Artikel(0, "", "", "", "", "", this.sourceEdited.getZitatList());
//                    this.sourceEdited = new Artikel((Artikel)this.source);
//                    this.adjustGridPane();
//                    break;
//
//                case SC_BUECHER:
//                    this.source = new Buch(0, "",  "", "", "", "", "", "", this.sourceEdited.getZitatList());
//                    this.sourceEdited = new Buch((Buch)this.source);
//                    this.adjustGridPane();
//                    break;
//
//                case SC_OQUELLEN:
//                    this.source = new Onlinequelle(0, "", "", "", "", "", this.sourceEdited.getZitatList());
//                    this.sourceEdited = new Onlinequelle((Onlinequelle)this.source);
//                    this.adjustGridPane();
//                    break;
//
//                case SC_WARBEITEN:
//                    this.source = new WissenschaftlicheArbeit(0, "", "", "", "", "", this.sourceEdited.getZitatList());
//                    this.sourceEdited = new WissenschaftlicheArbeit((WissenschaftlicheArbeit)this.source);
//                    this.adjustGridPane();
//                    break;
//
//                case SC_ANDERES:
//                    this.source = new Anderes("", "", "", "", "", "");
//                    this.sourceEdited = new Anderes((Anderes)this.source);
//                    this.adjustGridPane();
//                    break;
//
//                case SC_NONE:
//                    this.source = new Quelle("", "", "");
//                    this.source.setZitatListe(this.sourceEdited.getZitatList());
//                    this.sourceEdited = new Quelle(this.source);
//                    this.adjustGridPane();
//            }
//        }
    }
}
