package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main_app.MainApp;

import java.io.IOException;

public abstract class ViewUtillities {

    public static void showScreen(String resource, String titel, Stage stage) {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(resource));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(titel);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
