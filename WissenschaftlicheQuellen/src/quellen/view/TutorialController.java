package quellen.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;

public class TutorialController {

    @FXML
    private Button myNextButton;

    @FXML
    private ImageView myImage;

    private int nextClicks = 0;

    @FXML
    private void handleButtonAction (ActionEvent event) {
        // Handle Button event.
        Image image;
        if (nextClicks == 0) {
            image = new Image(getClass().getResource("./pic/tut01.png").toExternalForm());
            myImage.setImage(image);
            this.nextClicks++;
        } //else if (nextClicks == 1) {
            //image = new Image(getClass().getResource("./pic/.png").toExternalForm());
            //myImage.setImage(image);
            //this.nextClicks++;
       // }



    }





}
