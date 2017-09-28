package quellen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quellen.MainApp;
import quellen.model.*;
import java.io.IOException;


import static quellen.constants.DB_Constants.*;
import static quellen.constants.Controller_Constants.*;


/**
 * Dialog to edit details of a quelle.
 * @author Cedric Schreiner,
 */
public class QuellenEditDialogController {

    @FXML
    private TextField autorField;
    @FXML
    private TextField titelField;
    @FXML
    private TextField jahrField;
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TableView<Zitat> zitatTable;
    @FXML
    private TableView<Tag> tagTable;
    @FXML
    private TableColumn<Zitat, String> zitatColumn;
    @FXML
    private TableColumn<Tag, String> tagColumn;
    @FXML
    private ChoiceBox<String> subCategory;

    private Stage dialogStage;
    private Quelle quelle;
    private Quelle quelleEdited;
    private boolean okClicked = false;
    private ObservableList<Zitat> zitatList;
    private boolean editmode = true;
    private String newQuelleType = "";

    //all custom textFields
    private TextField herausgeberTextField;
    private TextField auflageTextField;
    private TextField monatTextField;
    private TextField isbnTextField;
    private TextField ausgabeTextField;
    private TextField aufrufDatumTextField;
    private TextField urlTextField;
    private TextField einrichtungsTextField;
    private TextField magazinTextField;
    private TextField subCategoryField;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the zitat table with the two columns.
        this.zitatColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        this.tagColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());

        //Add a Cell Factory to be able to edit a column
        this.zitatColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        this.tagColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Clear zitat details.
        showZitatDeatils(null);

        // Listen for selection changes and show the quelle details when changed.
        this.zitatTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZitatDeatils(newValue));

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
    }

    public void disableSubCategory(boolean disable) {
        this.subCategory.setDisable(disable);
    }

    /**
     * Fills all text fields to show details about the quelle.
     * If the specified quelle is null, all text fields are cleared.
     *
     * @param zitat the zitat or null
     */
    private void showZitatDeatils(Zitat zitat) {
        if (zitat != null) {
            tagTable.setItems(zitat.getTagList());
        }
    }

    public void setEditmode(boolean enable) {
        this.editmode = enable;
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
            newTagItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (zitatTable.getSelectionModel().getSelectedItem() != null) {
                        //1. get zitatList of quelle
                        //2. get the selected itemindex of zitattable
                        //3. get the taglist of the selected zitat
                        //4. add a new tag to the zitat
                        Tag tag = new Tag(NEU);
                        quelleEdited.getZitatList().get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().add(tag);
                        //zitatList = quelleEdited.getZitatList();
                        //zitatList.get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().add(tag);
                    }
                }
            });

            deleteTagItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Tag tag = tagTable.getSelectionModel().getSelectedItem();
                    quelleEdited.getZitatList().get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().remove(tag);
                }
            });

            addTag.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        // Load the fxml file and create a new stage for the popup dialog.
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(MainApp.class.getResource(ADDTAGSFXML));
                        AnchorPane page = (AnchorPane) loader.load();

                        // Create the addTag Stage.
                        Stage addTagStage = new Stage();
                        addTagStage.setTitle(FUEGE_TAG_HINZU);
                        addTagStage.initModality(Modality.WINDOW_MODAL);
                        Scene scene = new Scene(page);
                        addTagStage.setScene(scene);
                        addTagStage.initOwner(dialogStage);

                        // Set the quelle into the controller.
                        AddTagsController controller = loader.getController();
                        ObservableList<Tag> tagList = FXCollections.observableArrayList();
                        controller.setZitat(zitatTable.getSelectionModel().getSelectedItem());
                        //prevent duplicates
                        zitatList.forEach(zitat ->
                                zitat.getTagList().forEach(tag -> {
                                    boolean addTag = true;
                                    for (int i = 0; i < tagList.size(); i++) {
                                        if (tag.getText().equals(tagList.get(i).getText())) {
                                            addTag = false;
                                        }
                                    }
                                    if (addTag) {
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
        newZitatItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                quelleEdited.getZitatList().add(new Zitat(BEARBEITE_MICH, quelleEdited.getId()));
                zitatTable.setItems(quelleEdited.getZitatList());
            }
        });

        deleteZitatItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Zitat zitat = zitatTable.getSelectionModel().getSelectedItem();
                quelleEdited.getZitatList().remove(zitat);
            }
        });

        addZitatItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Load the fxml file and create a new stage for the popup dialog.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource(ADDZITATEFXML));
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the addTag Stage.
                    Stage addZitatStage = new Stage();
                    addZitatStage.setTitle(FUEGE_ZITAT_HINZU);
                    addZitatStage.initModality(Modality.WINDOW_MODAL);
                    Scene scene = new Scene(page);
                    addZitatStage.setScene(scene);
                    addZitatStage.initOwner(dialogStage);
                    addZitatStage.setResizable(false);

                    // Set the quelle into the controller.
                    AddZitateController controller = loader.getController();
                    controller.setZitatList(zitatList);
                    controller.setQuelle(quelleEdited);
                    controller.setStage(addZitatStage);

                    // Show the dialog and wait until the user closes it
                    addZitatStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Quelle getUpdatedQuelle() {
        if (okClicked) {
            quelleEdited.setTitel(this.titelField.getText());
            quelleEdited.setAutor(this.autorField.getText());
            quelleEdited.setJahr(this.jahrField.getText());
            if (quelle instanceof Buch) {
                ((Buch) quelleEdited).setAuflage(this.auflageTextField.getText());
                ((Buch) quelleEdited).setHerausgeber(this.herausgeberTextField.getText());
                ((Buch) quelleEdited).setMonat(this.monatTextField.getText());
                ((Buch) quelleEdited).setIsbn(this.isbnTextField.getText());
                return this.quelleEdited;
            } else if (quelle instanceof Artikel){
                ((Artikel) quelleEdited).setAusgabe(this.ausgabeTextField.getText());
                ((Artikel) quelleEdited).setMagazin(this.magazinTextField.getText());
            } else if (quelle instanceof Onlinequelle){
                ((Onlinequelle) quelleEdited).setUrl(this.urlTextField.getText());
                ((Onlinequelle) quelleEdited).setAufrufdatum(this.aufrufDatumTextField.getText());
            } else if (quelle instanceof Anderes) {
                ((Anderes) quelleEdited).setAuflage(this.auflageTextField.getText());
                ((Anderes) quelleEdited).setAusgabe(this.ausgabeTextField.getText());
                ((Anderes) quelleEdited).setHerausgeber(this.herausgeberTextField.getText());
            } else if (quelle instanceof  WissenschaftlicheArbeit) {
                ((WissenschaftlicheArbeit) quelleEdited).setEinrichtung(this.einrichtungsTextField.getText());
                ((WissenschaftlicheArbeit) quelleEdited).setHerausgeber(this.herausgeberTextField.getText());
            }
            return this.quelleEdited;
        }

        //the user canceld return the unchanged quelle
        return this.quelle;
    }

    /**
     * Sets the quelle to be edited in the dialog.
     * 
     * @param quelle
     */
    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;
        //create a copy of quelle to work with
        if (quelle instanceof Buch) {
            this.quelleEdited = new Buch((Buch)quelle);
        } else if (quelle instanceof Artikel){
            this.quelleEdited = new Artikel((Artikel) quelle);
        } else if (quelle instanceof Onlinequelle){
            this.quelleEdited = new Onlinequelle((Onlinequelle)quelle);
        } else if (quelle instanceof Anderes) {
            this.quelleEdited = new Anderes((Anderes)quelle);
        } else if (quelle instanceof  WissenschaftlicheArbeit) {
            this.quelleEdited = new WissenschaftlicheArbeit((WissenschaftlicheArbeit)quelle);
        } else {
            this.quelleEdited = new Quelle(quelle);
        }
        this.zitatTable.setItems(this.quelleEdited.getZitatList());

        autorField.setText(quelle.getAutor());
        titelField.setText(quelle.getTitel());
        jahrField.setText(quelle.getJahr());

        adjustGridPane();
    }

    private void adjustGridPane() {
        //Create a RowContraints
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setMinHeight(ROW_CONTRAINTS_HEIGHT);
        rowConstraint.setPrefHeight(ROW_CONTRAINTS_HEIGHT);
        rowConstraint.setVgrow(Priority.SOMETIMES);

        //check what quelle it is exactly
        //the editdialog gets adjusted for every sort of Source
        if (quelle instanceof Buch) {
            adjustDialogForBuch(rowConstraint);
        } else if (quelle instanceof Artikel || newQuelleType.equals(SC_ARTIKEL)){
            adjustDialogForArtikel(rowConstraint);
        } else if (quelle instanceof Onlinequelle){
            adjustDialogForOnlinequelle(rowConstraint);
        } else if (quelle instanceof Anderes) {
            adjustDialogForAnderes(rowConstraint);
        } else if (quelle instanceof  WissenschaftlicheArbeit) {
            adjustDialogForWissenschaftlicheArbeit(rowConstraint);
        } else {
            //normal quelle
            this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_QUELLE);
            this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_QUELLE);
            this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_QUELLE);
            this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_QUELLE);
            this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_QUELLE);
            this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_QUELLE);
        }
    }

    /**
     * Adds new content to the GridPane
     * @param constraints RowConstraint to add for the new Row
     * @param label Label to add
     * @param textField TextField to add
     * @param row row to insert
     * @param col col to insert
     */
    private void addContentTOGridPane(RowConstraints constraints, Label label, TextField textField, int row, int col) {
        //add content to the Gridpane
        this.gridPane.add(label, col,row);
        this.gridPane.add(textField, col + 1,row);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForBuch(RowConstraints rowConstraint) {
        Buch buch = (Buch)this.quelle;
        subCategory.setValue(SC_BUECHER);

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_BUCH);
        this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FPR_BUCH);
        this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FPR_BUCH);
        this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_BUCH);
        this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_BUCH);
        this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_BUCH);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label(HERAUSGEBER);
        Label auflageLabel = new Label(AUFLAGE);
        Label monatLabel = new Label(MONAT);
        Label isbnLabel = new Label(ISBN);

        //create all needed textfields for Buch
        this.herausgeberTextField = new TextField(buch.getHerausgeber());
        this.auflageTextField = new TextField(buch.getAuflage());
        this.monatTextField = new TextField(buch.getMonat());
        this.isbnTextField = new TextField(buch.getIsbn());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, HERAUSGEBER_TEXT_FIELD_ROW,HERAUSGEBER_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, AUFLAGE_TEXT_FIELD_ROW,AUFLAGE_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, monatLabel, this.monatTextField, MONAT_TEXT_FIELD_ROW,MONAT_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, isbnLabel, this.isbnTextField, ISBN_TEXT_FIELD_ROW,ISBN_TEXT_FIELD_COL);
        //subCategory.setDisable(true);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForArtikel(RowConstraints rowConstraint) {
        Artikel artikel = (Artikel)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_ARTIKEL);
        this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ARTIKEL);
        this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ARTIKEL);
        this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ARTIKEL);
        this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ARTIKEL);
        this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_ARTIKEL);

        //Create all needed labels for Artikel
        Label ausgabeLabel = new Label(AUSGABE);
        Label magazinLabel = new Label(MAGAZIN);

        //Create all needed textfields for Artikel
        this.ausgabeTextField = new TextField(artikel.getAusgabe());
        this.magazinTextField = new TextField(artikel.getMagazin());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, AUSGABE_TEXT_FIELD_ROW, AUSGABE_DATUM_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, magazinLabel, this.magazinTextField, MAGAZIN_TEXT_FIELD_ROW, MAGAZIN_TEXT_FIELD_COL);
        //subCategory.setDisable(true);
        subCategory.setValue(SC_ARTIKEL);
    }


    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForOnlinequelle(RowConstraints rowConstraint) {
        Onlinequelle onlinequelle = (Onlinequelle)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_ONLINEQUELLE);
        this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ONLINEQUELLE);
        this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ONLINEQUELLE);
        this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ONLINEQUELLE);
        this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ONLINEQUELLE);
        this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_ONLINEQUELLE);

        //Create all needed labels for Onlinequelle
        Label aufrufDatumLabel = new Label(AUFRUFDATUM);
        Label urlLabel = new Label(URL);

        //Create all  needed textfields for Onlinequelle
        this.aufrufDatumTextField = new TextField(onlinequelle.getAufrufdatum());
        this.urlTextField = new TextField(onlinequelle.getUrl());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, aufrufDatumLabel, this.aufrufDatumTextField, AUFRUF_DATUM_TEXT_FIELD_ROW, AUFRUF_DATUM_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, urlLabel, this.urlTextField, URL_TEXT_FIELD_ROW, URL_TEXT_FIELD_COL);
        //subCategory.setDisable(true);
        subCategory.setValue(SC_OQUELLEN);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForAnderes(RowConstraints rowConstraint) {
        Anderes anderes = (Anderes)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_ANDERES);
        this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ANDERES);
        this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_ANDERES);
        this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ANDERES);
        this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_ANDERES);
        this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_ANDERES);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label(HERAUSGEBER);
        Label auflageLabel = new Label(AUFLAGE);
        Label ausgabeLabel = new Label(AUSGABE);

        //create all needed textfields for Buch
        this.herausgeberTextField = new TextField(anderes.getHerausgeber());
        this.auflageTextField = new TextField(anderes.getAuflage());
        this.ausgabeTextField = new TextField(anderes.getAusgabe());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, HERAUSGEBER_TEXT_FIELD_ROW,HERAUSGEBER_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, AUFLAGE_TEXT_FIELD_ROW,AUFLAGE_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, AUSGABE_TEXT_FIELD_ROW_FOR_ANDERES,AUSGABE_DATUM_TEXT_FIELD_COL);
        //subCategory.setDisable(true);
        subCategory.setValue(SC_ANDERES);
    }

    public void setZitatList(ObservableList<Zitat> zitatList) {
        this.zitatList = zitatList;
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    public void adjustDialogForWissenschaftlicheArbeit(RowConstraints rowConstraint) {
        WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(ANCHOR_PANE_SET_PREF_HEIGHT_FOR_WISSENSCHAFTLICHE_ARBEIT);
        this.okButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_WISSENSCHAFTLICHE_ARBEIT);
        this.cancelButton.setLayoutY(OK_AND_CANCEL_BUTTON_SET_LAYOUT_Y_FOR_WISSENSCHAFTLICHE_ARBEIT);
        this.zitatTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_WISSENSCHAFTLICHE_ARBEIT);
        this.tagTable.setPrefHeight(ZITAT_AND_TAG_TABLE_SET_PREF_HEIGHT_FOR_WISSENSCHAFTLICHE_ARBEIT);
        this.zitatTable.getScene().getWindow().setHeight(SET_WINDOW_HEIGHT_FOR_WISSENSCHAFTLICHE_ARBEIT);

        //Create all needed labels for Onlinequelle
        Label herausgeberLabel = new Label(HERAUSGEBER);
        Label einrichtungsLabel = new Label(EINRICHTUNG);

        //Create all  needed textfields for Onlinequelle
        this.herausgeberTextField= new TextField(wissenschaftlicheArbeit.getHerausgeber());
        this.einrichtungsTextField = new TextField(wissenschaftlicheArbeit.getEinrichtung());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, HERAUSGEBER_TEXT_FIELD_ROW, HERAUSGEBER_TEXT_FIELD_COL);
        addContentTOGridPane(rowConstraint, einrichtungsLabel, this.einrichtungsTextField, EINRICHTUNGS_TEXT_FIELD_ROW, EINRICHTUNGS_TEXT_FIELD_COL);
        //subCategory.setDisable(true);
        subCategory.setValue(SC_WARBEITEN);
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            quelle.setAutor(autorField.getText());
            quelle.setTitel(titelField.getText());
            quelle.setJahr(jahrField.getText());
            quelle.setUnterkategorie(getSubCategory());

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     * 
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = ERROR_MESSAGE;

        if (autorField.getText() == null || autorField.getText().length() == 0) {
            errorMessage += AUTOR_NICHT_AUSGEFUELLT;
        }
        if (titelField.getText() == null || titelField.getText().length() == 0) {
            errorMessage += TITEL_NICHT_AUSGEFUELLT;
        }
        if (jahrField.getText() == null || jahrField.getText().length() == 0) {
            errorMessage += JAHR_NICHT_AUSGEFUELLT;
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle(FLASCHE_FELDER);
            alert.setHeaderText(KORRIGIERE_FALSCHE_FELDER);
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private String getSubCategory() {
        return subCategory.getValue();
    }

    private void adjustNewDialog(String category) {
        //child node count
        int count = this.gridPane.getChildren().size();
        //Create a RowContraints
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setMinHeight(ROW_CONTRAINTS_HEIGHT);
        rowConstraint.setPrefHeight(ROW_CONTRAINTS_HEIGHT);
        rowConstraint.setVgrow(Priority.SOMETIMES);
        //Remove all items from another quelle
        if (!editmode) {
            if (count >= 8) {
                this.gridPane.getChildren().remove(8, count);
            }
            switch (category) {
                case SC_ARTIKEL:
                    this.quelle = new Artikel(0, "", "", "", "", "", this.quelleEdited.getZitatList());
                    this.quelleEdited = new Artikel((Artikel)this.quelle);
                    this.adjustGridPane();
                    break;

                case SC_BUECHER:
                    this.quelle = new Buch(0, "",  "", "", "", "", "", "", this.quelleEdited.getZitatList());
                    this.quelleEdited = new Buch((Buch)this.quelle);
                    this.adjustGridPane();
                    break;

                case SC_OQUELLEN:
                    this.quelle = new Onlinequelle(0, "", "", "", "", "", this.quelleEdited.getZitatList());
                    this.quelleEdited = new Onlinequelle((Onlinequelle)this.quelle);
                    this.adjustGridPane();
                    break;

                case SC_WARBEITEN:
                    this.quelle = new WissenschaftlicheArbeit(0, "", "", "", "", "", this.quelleEdited.getZitatList());
                    this.quelleEdited = new WissenschaftlicheArbeit((WissenschaftlicheArbeit)this.quelle);
                    this.adjustGridPane();
                    break;

                case SC_ANDERES:
                    this.quelle = new Anderes("", "", "", "", "", "");
                    this.quelleEdited = new Anderes((Anderes)this.quelle);
                    this.adjustGridPane();
                    break;

                case SC_NONE:
                    this.quelle = new Quelle("", "", "");
                    this.quelle.setZitatListe(this.quelleEdited.getZitatList());
                    this.quelleEdited = new Quelle(this.quelle);
                    this.adjustGridPane();
            }
        }
    }
}
