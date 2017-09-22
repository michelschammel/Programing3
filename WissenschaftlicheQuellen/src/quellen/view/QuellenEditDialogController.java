package quellen.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import quellen.model.*;

import javax.xml.soap.Text;

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

    private Stage dialogStage;
    private Quelle quelle;
    private boolean okClicked = false;

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
        rowConstraint.setMinHeight(10);
        rowConstraint.setPrefHeight(30);
        rowConstraint.setVgrow(Priority.SOMETIMES);

        //Set Vertical Gap between rows
        this.gridPane.setVgap(22);

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
        this.gridPane.getRowConstraints().add(constraints);
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
        this.anchorPane.setPrefHeight(260);
        this.okButton.setLayoutY(225);
        this.cancelButton.setLayoutY(225);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label("Herausgeber");
        Label auflageLabel = new Label("Auflage");
        Label monatLabel = new Label("Monat");
        Label isbnLabel = new Label("ISBN");

        //create all needed textfields for Buch
        TextField herausgeberTextField = new TextField(buch.getHerausgeber());
        TextField auflageTextField = new TextField(buch.getAuflage());
        TextField monatTextField = new TextField(buch.getMonat());
        TextField isbnTextField = new TextField(buch.getIsbn());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, herausgeberTextField, 3,0);
        addContentTOGridPane(rowConstraint, auflageLabel, auflageTextField, 4,0);
        addContentTOGridPane(rowConstraint, monatLabel, monatTextField, 5,0);
        addContentTOGridPane(rowConstraint, isbnLabel, isbnTextField, 6,0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForArtikel(RowConstraints rowConstraint) {
        Artikel artikel = (Artikel)this.quelle;

        //Create all needed labels for Artikel
        Label ausgabeLabel = new Label("Ausgabe");

        //Create all needed textfields for Artikel
        TextField ausgabeTextField = new TextField(artikel.getAusgabe());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, ausgabeLabel, ausgabeTextField, 3, 0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForOnlinequelle(RowConstraints rowConstraint) {
        Onlinequelle onlinequelle = (Onlinequelle)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(200);
        this.okButton.setLayoutY(165);
        this.cancelButton.setLayoutY(165);

        //Create all needed labels for Onlinequelle
        Label aufrufDatumLabel = new Label("Aufrufdatum");
        Label urlLabel = new Label("URL");

        //Create all  needed textfields for Onlinequelle
        TextField aufrufDatumTextField = new TextField(onlinequelle.getAufrufdatum());
        TextField urlTextField = new TextField(onlinequelle.getUrl());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, aufrufDatumLabel, aufrufDatumTextField, 3, 0);
        addContentTOGridPane(rowConstraint, urlLabel, urlTextField, 4, 0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    private void adjustDialogForAnderes(RowConstraints rowConstraint) {
        Anderes anderes = (Anderes)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(230);
        this.okButton.setLayoutY(195);
        this.cancelButton.setLayoutY(195);

        //Create all needed labels for Buch
        Label herausgeberLabel = new Label("Herausgeber");
        Label auflageLabel = new Label("Auflage");
        Label ausgabeLabel = new Label("Ausgabe");

        //create all needed textfields for Buch
        TextField herausgeberTextField = new TextField(anderes.getHerausgeber());
        TextField auflageTextField = new TextField(anderes.getAuflage());
        TextField ausgabeTextField = new TextField(anderes.getAusgabe());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, herausgeberTextField, 3,0);
        addContentTOGridPane(rowConstraint, auflageLabel, auflageTextField, 4,0);
        addContentTOGridPane(rowConstraint, ausgabeLabel, ausgabeTextField, 5,0);
    }

    /**
     * Adjusts and adds components for the editDialog
     * @param rowConstraint rowContraint for the Dialog
     */
    public void adjustDialogForWissenschaftlicheArbeit(RowConstraints rowConstraint) {
        WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit)this.quelle;

        //adjust anchorPane and Buttons for Dialog
        this.anchorPane.setPrefHeight(200);
        this.okButton.setLayoutY(165);
        this.cancelButton.setLayoutY(165);

        //Create all needed labels for Onlinequelle
        Label herausgeberLabel = new Label("Herausgeber");
        Label einrichtungsLabel = new Label("Einrichtung");

        //Create all  needed textfields for Onlinequelle
        TextField herausgeberTextLabel = new TextField(wissenschaftlicheArbeit.getHerausgeber());
        TextField einrichtungsTextLabel = new TextField(wissenschaftlicheArbeit.getEinrichtung());

        //Add the content to the Gridpane
        addContentTOGridPane(rowConstraint, herausgeberLabel, herausgeberTextLabel, 3, 0);
        addContentTOGridPane(rowConstraint, einrichtungsLabel, einrichtungsTextLabel, 4, 0);
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
