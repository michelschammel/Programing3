package quellen.view;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import quellen.constants.*;

import static quellen.constants.DB_Constants.*;

/**
 * Dialog to edit details of a quelle.
 * 
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

    /**
     * gets called if the user requests a context Menu
     */
    public void contextMenuTagTable() {
        if (this.zitatTable.getSelectionModel().getSelectedItem() != null) {
            //Create a new Contextmenu
            ContextMenu contextMenu = new ContextMenu();

            //create needed menuitems for the contextmenu
            MenuItem newTagItem = new MenuItem("new Tag");
            MenuItem deleteTagItem = new MenuItem("delete Tag");
            MenuItem addTag = new MenuItem("add existing Tag");

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
                        quelleEdited.getZitatList().get(zitatTable.getSelectionModel().getSelectedIndex()).getTagList().add(new Tag("new"));
                        zitatList = quelleEdited.getZitatList();
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
                        loader.setLocation(MainApp.class.getResource("view/AddTags.fxml"));
                        AnchorPane page = (AnchorPane) loader.load();

                        // Create the addTag Stage.
                        Stage addTagStage = new Stage();
                        addTagStage.setTitle("Add Tag");
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
        MenuItem newZitatItem = new MenuItem("new Zitat");
        MenuItem deleteZitatItem = new MenuItem("delete Zitat");
        MenuItem addZitatItem = new MenuItem("add existing Zitat");

        //add the menuitems to the contextmenu
        contextMenu.getItems().addAll(newZitatItem, deleteZitatItem, addZitatItem);

        //add the contextmenu to the table
        this.zitatTable.setContextMenu(contextMenu);

        //add eventlistener for both menuitems
        newZitatItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                quelleEdited.getZitatList().add(new Zitat("edit me!", quelleEdited.getId()));
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
                    loader.setLocation(MainApp.class.getResource("view/AddZitate.fxml"));
                    AnchorPane page = (AnchorPane) loader.load();

                    // Create the addTag Stage.
                    Stage addTagStage = new Stage();
                    addTagStage.setTitle("Add Zitat");
                    addTagStage.initModality(Modality.WINDOW_MODAL);
                    Scene scene = new Scene(page);
                    addTagStage.setScene(scene);
                    addTagStage.initOwner(dialogStage);

                    // Set the quelle into the controller.
                    AddZitateController controller = loader.getController();
                    controller.setZitatList(zitatList);
                    controller.setQuelle(quelleEdited);

                    // Show the dialog and wait until the user closes it
                    addTagStage.showAndWait();
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

        //Create a RowContraints
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setMinHeight(30);
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);

        //check what quelle it is exactly
        //the editdialog gets adjusted for every sort of Source
        if (quelle instanceof Buch) {
            adjustDialogForBuch(rowConstraint);
        } else if (quelle instanceof Artikel){
            adjustDialogForArtikel(rowConstraint);
        } else if (quelle instanceof Onlinequelle){
            adjustDialogForOnlinequelle(rowConstraint);
        } else if (quelle instanceof Anderes) {
            adjustDialogForAnderes(rowConstraint);
        } else if (quelle instanceof  WissenschaftlicheArbeit) {
            adjustDialogForWissenschaftlicheArbeit(rowConstraint);
        } else {
            //normal quelle
            //set zitat and tag Table position new
            zitatTable.setLayoutY(13);
            tagTable.setLayoutY(13);
        }
    }

    /**
     * Adds ne content to the GridPane
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

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(320);
        this.okButton.setLayoutY(285);
        this.cancelButton.setLayoutY(285);
        this.zitatTable.setPrefHeight(273);
        this.tagTable.setPrefHeight(273);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label("Herausgeber");
        Label auflageLabel = new Label("Auflage");
        Label monatLabel = new Label("Monat");
        Label isbnLabel = new Label("ISBN");

        //create all needed textfields for Buch
        this.herausgeberTextField = new TextField(buch.getHerausgeber());
        this.auflageTextField = new TextField(buch.getAuflage());
        this.monatTextField = new TextField(buch.getMonat());
        this.isbnTextField = new TextField(buch.getIsbn());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 4,0);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, 5,0);
        addContentTOGridPane(rowConstraint, monatLabel, this.monatTextField, 6,0);
        addContentTOGridPane(rowConstraint, isbnLabel, this.isbnTextField, 7,0);
        subCategory.setValue(SC_BUECHER);
        subCategory.setDisable(true);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForArtikel(RowConstraints rowConstraint) {
        Artikel artikel = (Artikel)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(250);
        this.okButton.setLayoutY(215);
        this.cancelButton.setLayoutY(215);
        this.zitatTable.setPrefHeight(203);
        this.tagTable.setPrefHeight(203);

        //Create all needed labels for Artikel
        Label ausgabeLabel = new Label("Ausgabe");
        Label magazinLabel = new Label("Magazin");

        //Create all needed textfields for Artikel
        this.ausgabeTextField = new TextField(artikel.getAusgabe());
        this.magazinTextField = new TextField(artikel.getMagazin());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, 4, 0);
        addContentTOGridPane(rowConstraint, magazinLabel, this.magazinTextField, 5, 0);
        subCategory.setValue(SC_ARTIKEL);
        subCategory.setDisable(true);
    }


    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForOnlinequelle(RowConstraints rowConstraint) {
        Onlinequelle onlinequelle = (Onlinequelle)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(255);
        this.okButton.setLayoutY(220);
        this.cancelButton.setLayoutY(220);
        this.zitatTable.setPrefHeight(203);
        this.tagTable.setPrefHeight(203);

        //Create all needed labels for Onlinequelle
        Label aufrufDatumLabel = new Label("Aufrufdatum");
        Label urlLabel = new Label("URL");

        //Create all  needed textfields for Onlinequelle
        this.aufrufDatumTextField = new TextField(onlinequelle.getAufrufdatum());
        this.urlTextField = new TextField(onlinequelle.getUrl());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, aufrufDatumLabel, this.aufrufDatumTextField, 4, 0);
        addContentTOGridPane(rowConstraint, urlLabel, this.urlTextField, 5, 0);
        subCategory.setValue(SC_OQUELLEN);
        subCategory.setDisable(true);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForAnderes(RowConstraints rowConstraint) {
        Anderes anderes = (Anderes)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(295);
        this.okButton.setLayoutY(260);
        this.cancelButton.setLayoutY(260);
        this.zitatTable.setPrefHeight(238);
        this.tagTable.setPrefHeight(238);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label("Herausgeber");
        Label auflageLabel = new Label("Auflage");
        Label ausgabeLabel = new Label("Ausgabe");

        //create all needed textfields for Buch
        this.herausgeberTextField = new TextField(anderes.getHerausgeber());
        this.auflageTextField = new TextField(anderes.getAuflage());
        this.ausgabeTextField = new TextField(anderes.getAusgabe());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 4,0);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, 5,0);
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, 6,0);
        subCategory.setValue(SC_ANDERES);
        subCategory.setDisable(true);
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
        this.anchorPane.setPrefHeight(265);
        this.okButton.setLayoutY(225);
        this.cancelButton.setLayoutY(225);
        this.zitatTable.setPrefHeight(203);
        this.tagTable.setPrefHeight(203);

        //Create all needed labels for Onlinequelle
        Label herausgeberLabel = new Label("Herausgeber");
        Label einrichtungsLabel = new Label("Einrichtung");

        //Create all  needed textfields for Onlinequelle
        this.herausgeberTextField= new TextField(wissenschaftlicheArbeit.getHerausgeber());
        this.einrichtungsTextField = new TextField(wissenschaftlicheArbeit.getEinrichtung());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 4, 0);
        addContentTOGridPane(rowConstraint, einrichtungsLabel, this.einrichtungsTextField, 5, 0);
        subCategory.setValue(SC_WARBEITEN);
        subCategory.setDisable(true);
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
        String errorMessage = "";

        if (autorField.getText() == null || autorField.getText().length() == 0) {
            errorMessage += "No vaid autor!\n"; 
        }
        if (titelField.getText() == null || titelField.getText().length() == 0) {
            errorMessage += "No valid titel!\n"; 
        }
        if (jahrField.getText() == null || jahrField.getText().length() == 0) {
            errorMessage += "No valid jahr!\n"; 
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private String getSubCategory() {
        return subCategory.getValue();
    }

    private void adjustNewDialog(String category) {
        //Create a RowContraints
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setMinHeight(30);
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);

        switch (category) {
            case SC_ARTIKEL:
                //Create all needed labels for Artikel
                Label ausgabeLabel = new Label("Ausgabe");
                Label magazinLabel = new Label("Magazin");

                //Create all needed textfields for Artikel
                this.ausgabeTextField = new TextField();
                this.magazinTextField = new TextField();

                //Add the content to the Gridpane
                addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, 4, 0);
                addContentTOGridPane(rowConstraint, magazinLabel, this.magazinTextField, 5, 0);
                subCategory.setDisable(true);
                break;

            case SC_BUECHER:
                //Create all needed labels for Buch
                Label herausgeberLabel = new Label("Herausgeber");
                Label auflageLabel = new Label("Auflage");
                Label monatLabel = new Label("Monat");
                Label isbnLabel = new Label("ISBN");

                //create all needed textfields for Buch
                this.herausgeberTextField = new TextField();
                this.auflageTextField = new TextField();
                this.monatTextField = new TextField();
                this.isbnTextField = new TextField();

                //Add the content to the Gridpane
                addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 4,0);
                addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, 5,0);
                addContentTOGridPane(rowConstraint, monatLabel, this.monatTextField, 6,0);
                addContentTOGridPane(rowConstraint, isbnLabel, this.isbnTextField, 7,0);
                subCategory.setDisable(true);
                break;

            case SC_OQUELLEN:
                //Create all needed labels for Onlinequelle
                Label aufrufDatumLabel = new Label("Aufrufdatum");
                Label urlLabel = new Label("URL");

                //Create all  needed textfields for Onlinequelle
                this.aufrufDatumTextField = new TextField();
                this.urlTextField = new TextField();

                //Add the content to the Gridpane
                addContentTOGridPane(rowConstraint, aufrufDatumLabel, this.aufrufDatumTextField, 4, 0);
                addContentTOGridPane(rowConstraint, urlLabel, this.urlTextField, 5, 0);
                subCategory.setDisable(true);
                break;

            case SC_WARBEITEN:
                Label publisherLabel = new Label("Herausgeber");
                Label einrichtungsLabel = new Label("Einrichtung");

                //Create all  needed textfields for Onlinequelle
                this.herausgeberTextField= new TextField();
                this.einrichtungsTextField = new TextField();

                //Add the content to the Gridpane
                addContentTOGridPane(rowConstraint, publisherLabel, this.herausgeberTextField, 4, 0);
                addContentTOGridPane(rowConstraint, einrichtungsLabel, this.einrichtungsTextField, 5, 0);
                subCategory.setDisable(true);
                break;

            case SC_ANDERES:
                //Create all needed labels for Buch
                Label herausgeberLabel1 = new Label("Herausgeber");
                Label auflageLabel1 = new Label("Auflage");
                Label ausgabeLabel1 = new Label("Ausgabe");

                //create all needed textfields for Buch
                this.herausgeberTextField = new TextField();
                this.auflageTextField = new TextField();
                this.ausgabeTextField = new TextField();

                //Add the content to the Gridpane
                addContentTOGridPane(rowConstraint, herausgeberLabel1, this.herausgeberTextField, 4,0);
                addContentTOGridPane(rowConstraint, auflageLabel1, this.auflageTextField, 5,0);
                addContentTOGridPane(rowConstraint, ausgabeLabel1, this.ausgabeTextField, 6,0);
                subCategory.setDisable(true);
                break;




        }
    }




}

