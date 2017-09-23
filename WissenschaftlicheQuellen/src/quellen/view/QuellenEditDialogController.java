package quellen.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import quellen.model.*;

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
    private TableView zitatTable;

    private Stage dialogStage;
    private Quelle quelle;
    private boolean okClicked = false;

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
            //update die Quelle
            this.quelle.setAutor(this.autorField.getText());
            this.quelle.setJahr(this.jahrField.getText());
            this.quelle.setTitel(this.titelField.getText());
            if (quelle instanceof Buch) {
                ((Buch) quelle).setAuflage(this.auflageTextField.getText());
                ((Buch) quelle).setHerausgeber(this.herausgeberTextField.getText());
                ((Buch) quelle).setMonat(this.monatTextField.getText());
                ((Buch) quelle).setIsbn(this.isbnTextField.getText());
                return this.quelle;
            } else if (quelle instanceof Artikel){
                ((Artikel) quelle).setAusgabe(this.ausgabeTextField.getText());
                ((Artikel) quelle).setMagazin(this.magazinTextField.getText());
                return this.quelle;
            } else if (quelle instanceof Onlinequelle){
                ((Onlinequelle) quelle).setUrl(this.urlTextField.getText());
                ((Onlinequelle) quelle).setAufrufdatum(this.aufrufDatumTextField.getText());
                return this.quelle;
            } else if (quelle instanceof Anderes) {
                ((Anderes) quelle).setAuflage(this.auflageTextField.getText());
                ((Anderes) quelle).setAusgabe(this.ausgabeTextField.getText());
                ((Anderes) quelle).setHerausgeber(this.herausgeberTextField.getText());
                return this.quelle;
            } else if (quelle instanceof  WissenschaftlicheArbeit) {
                ((WissenschaftlicheArbeit) quelle).setEinrichtung(this.einrichtungsTextField.getText());
                ((WissenschaftlicheArbeit) quelle).setHerausgeber(this.herausgeberTextField.getText());
                return this.quelle;
            }
        }
        return this.quelle;
    }

    /**
     * Sets the quelle to be edited in the dialog.
     * 
     * @param quelle
     */
    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;

        autorField.setText(quelle.getAutor());
        titelField.setText(quelle.getTitel());
        jahrField.setText(quelle.getJahr());

        //Create a RowContraints
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setMinHeight(30);
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);

        //Set Vertical Gap between rows
        //this.gridPane.setVgap(22);

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
            //set zitat Table position new
            zitatTable.setLayoutY(13);
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
        //add new RowConstraint
        //this.gridPane.getRowConstraints().add(constraints);
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
        this.anchorPane.setPrefHeight(290);
        this.okButton.setLayoutY(255);
        this.cancelButton.setLayoutY(255);
        this.zitatTable.setPrefHeight(273);

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
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 3,0);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, 4,0);
        addContentTOGridPane(rowConstraint, monatLabel, this.monatTextField, 5,0);
        addContentTOGridPane(rowConstraint, isbnLabel, this.isbnTextField, 6,0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForArtikel(RowConstraints rowConstraint) {
        Artikel artikel = (Artikel)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(220);
        this.okButton.setLayoutY(185);
        this.cancelButton.setLayoutY(185);
        this.zitatTable.setPrefHeight(203);

        //Create all needed labels for Artikel
        Label ausgabeLabel = new Label("Ausgabe");
        Label magazinLabel = new Label("Magazin");

        //Create all needed textfields for Artikel
        this.ausgabeTextField = new TextField(artikel.getAusgabe());
        this.magazinTextField = new TextField(artikel.getMagazin());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, 3, 0);
        addContentTOGridPane(rowConstraint, magazinLabel, this.magazinTextField, 4, 0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForOnlinequelle(RowConstraints rowConstraint) {
        Onlinequelle onlinequelle = (Onlinequelle)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(220);
        this.okButton.setLayoutY(185);
        this.cancelButton.setLayoutY(185);
        this.zitatTable.setPrefHeight(203);

        //Create all needed labels for Onlinequelle
        Label aufrufDatumLabel = new Label("Aufrufdatum");
        Label urlLabel = new Label("URL");

        //Create all  needed textfields for Onlinequelle
        this.aufrufDatumTextField = new TextField(onlinequelle.getAufrufdatum());
        this.urlTextField = new TextField(onlinequelle.getUrl());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, aufrufDatumLabel, this.aufrufDatumTextField, 3, 0);
        addContentTOGridPane(rowConstraint, urlLabel, this.urlTextField, 4, 0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForAnderes(RowConstraints rowConstraint) {
        Anderes anderes = (Anderes)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(255);
        this.okButton.setLayoutY(220);
        this.cancelButton.setLayoutY(220);
        this.zitatTable.setPrefHeight(238);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label("Herausgeber");
        Label auflageLabel = new Label("Auflage");
        Label ausgabeLabel = new Label("Ausgabe");

        //create all needed textfields for Buch
        this.herausgeberTextField = new TextField(anderes.getHerausgeber());
        this.auflageTextField = new TextField(anderes.getAuflage());
        this.ausgabeTextField = new TextField(anderes.getAusgabe());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 3,0);
        addContentTOGridPane(rowConstraint, auflageLabel, this.auflageTextField, 4,0);
        addContentTOGridPane(rowConstraint, ausgabeLabel, this.ausgabeTextField, 5,0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    public void adjustDialogForWissenschaftlicheArbeit(RowConstraints rowConstraint) {
        WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(220);
        this.okButton.setLayoutY(185);
        this.cancelButton.setLayoutY(185);
        this.zitatTable.setPrefHeight(203);

        //Create all needed labels for Onlinequelle
        Label herausgeberLabel = new Label("Herausgeber");
        Label einrichtungsLabel = new Label("Einrichtung");

        //Create all  needed textfields for Onlinequelle
        this.herausgeberTextField= new TextField(wissenschaftlicheArbeit.getHerausgeber());
        this.einrichtungsTextField = new TextField(wissenschaftlicheArbeit.getEinrichtung());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, this.herausgeberTextField, 3, 0);
        addContentTOGridPane(rowConstraint, einrichtungsLabel, this.einrichtungsTextField, 4, 0);
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
}
