package controller;

import javafx.fxml.FXML;
import javafx.scene.image.*;

import static controller.constants.TutorialControllerConstants.*;

/**
 *
 * @author Björn Schmidt
 */
public class TutorialController {

    @FXML
    private ImageView myImage;

    private int nextClicks = 1;
    private boolean buttonTest = true;




    /**
     * Verwaltet die Tutorialbilder, damit sie beim "weiter" klicken in der richtigen Reihenfolge auftauchen.
     */
    @FXML
    private void handleButtonNextAction () {
        // Handle Button event.
        Image image;

        if (!buttonTest && nextClicks < TUT_ANZAHL_PLUS_EINS) {
            this.nextClicks++;
        }
        this.buttonTest = true;

        switch (nextClicks) {
            case TUT_01:
                image = new Image(getClass().getResource(PIC_TUT_01).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
            case TUT_02:
                image = new Image(getClass().getResource(PIC_TUT_02).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_03:
                image = new Image(getClass().getResource(PIC_TUT_03).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_04:
                image = new Image(getClass().getResource(PIC_TUT_04).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_05:
                image = new Image(getClass().getResource(PIC_TUT_05).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_06:
                image = new Image(getClass().getResource(PIC_TUT_06).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_07:
                image = new Image(getClass().getResource(PIC_TUT_07).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_08:
                image = new Image(getClass().getResource(PIC_TUT_08).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_09:
                image = new Image(getClass().getResource(PIC_TUT_09).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_10:
                image = new Image(getClass().getResource(PIC_TUT_10).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_11:
                image = new Image(getClass().getResource(PIC_TUT_11).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_12:
                image = new Image(getClass().getResource(PIC_TUT_12).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_13:
                image = new Image(getClass().getResource(PIC_TUT_13).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_14:
                image = new Image(getClass().getResource(PIC_TUT_14).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_15:
                image = new Image(getClass().getResource(PIC_TUT_15).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            default:
                this.buttonTest = false;
                image = new Image(getClass().getResource(PIC_TUT).toExternalForm());
                myImage.setImage(image);
        }
    }

    /**
     * Verwaltet die Tutorialbilder, damit sie beim "zurueck" klicken in der richtigen Reihenfolge auftauchen.
     */
    @FXML
    private void handleButtonBackAction () {
        Image image;

        if (nextClicks > 1 && buttonTest) {
            this.nextClicks--;
            this.buttonTest = false;
        }

        if (nextClicks > 1) {
            this.nextClicks--;
        }


        switch (nextClicks) {
            case TUT_01:
                image = new Image(getClass().getResource(PIC_TUT_01).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_02:
                image = new Image(getClass().getResource(PIC_TUT_02).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_03:
                image = new Image(getClass().getResource(PIC_TUT_03).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_04:
                image = new Image(getClass().getResource(PIC_TUT_04).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_05:
                image = new Image(getClass().getResource(PIC_TUT_05).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_06:
                image = new Image(getClass().getResource(PIC_TUT_06).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_07:
                image = new Image(getClass().getResource(PIC_TUT_07).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_08:
                image = new Image(getClass().getResource(PIC_TUT_08).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_09:
                image = new Image(getClass().getResource(PIC_TUT_09).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_10:
                image = new Image(getClass().getResource(PIC_TUT_10).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_11:
                image = new Image(getClass().getResource(PIC_TUT_11).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_12:
                image = new Image(getClass().getResource(PIC_TUT_12).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_13:
                image = new Image(getClass().getResource(PIC_TUT_13).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_14:
                image = new Image(getClass().getResource(PIC_TUT_14).toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_15:
                image = new Image(getClass().getResource(PIC_TUT_15).toExternalForm());
                myImage.setImage(image);
                break;
            default:
                image = new Image(getClass().getResource(PIC_TUT).toExternalForm());
                myImage.setImage(image);
        }
    }
}


