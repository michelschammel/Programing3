package quellen.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import quellen.model.Quelle;

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
