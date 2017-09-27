package quellen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.*;

public class TutorialController {

    @FXML
    private ImageView myImage;

    private int nextClicks = 1;
    private static final int TUT_01 = 1;
    private static final int TUT_02 = 2;
    private static final int TUT_03 = 3;
    private boolean buttonTest = true;



    @FXML
    private void handleButtonNextAction () {
        // Handle Button event.
        Image image;
        this.buttonTest = true;

        switch (nextClicks) {
            case TUT_01:
                image = new Image(getClass().getResource("../resources/tut01.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
            case TUT_02:
                image = new Image(getClass().getResource("../resources/tut02.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_03:
                image = new Image(getClass().getResource("../resources/tut03.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            default:
                this.buttonTest = false;
                image = new Image(getClass().getResource("../resources/ende.png").toExternalForm());
                myImage.setImage(image);
        }
    }


    @FXML
    private void handleButtonBackAction () {
        Image image;

        if (nextClicks > 1 && buttonTest == true) {
            this.nextClicks--;
            this.buttonTest = false;
        }
        if (nextClicks > 1) {
            this.nextClicks--;
        }

        switch (nextClicks) {
            case TUT_01:
                image = new Image(getClass().getResource("../resources/tut01.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_02:
                image = new Image(getClass().getResource("../resources/tut02.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_03:
                image = new Image(getClass().getResource("../resources/tut03.png").toExternalForm());
                myImage.setImage(image);
                break;
            default:
                image = new Image(getClass().getResource("../resources/ende.png").toExternalForm());
                myImage.setImage(image);
        }
    }
}


