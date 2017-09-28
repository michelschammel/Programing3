package quellen.controller;

import javafx.fxml.FXML;
import javafx.scene.image.*;

import static quellen.constants.Controller_Constants.*;

/**
 *
 * @Author Björn Schmidt
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

        if (buttonTest == false && nextClicks < TUT_ANZAHL_PLUS_EINS) {
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
 /*           case TUT_09:
                image = new Image(getClass().getResource(PIC_TUT_09).toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
   */         default:
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

        if (nextClicks > 1 && buttonTest == true) {
            this.nextClicks--;
            this.buttonTest = false;
        } else if (nextClicks == TUT_ANZAHL_PLUS_EINS && buttonTest == false){
            this.nextClicks--;
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
   /*         case TUT_09:
                image = new Image(getClass().getResource(PIC_TUT_09).toExternalForm());
                myImage.setImage(image);
                break;
     */       default:
                image = new Image(getClass().getResource(PIC_TUT).toExternalForm());
                myImage.setImage(image);
        }
    }
}


