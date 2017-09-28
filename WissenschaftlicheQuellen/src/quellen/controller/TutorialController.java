package quellen.controller;

import javafx.fxml.FXML;
import javafx.scene.image.*;

/**
 *
 * @Author Björn Schmidt
 */
public class TutorialController {

    @FXML
    private ImageView myImage;

    private int nextClicks = 1;
    private boolean buttonTest = true;
    private static final int TUT_01 = 1;
    private static final int TUT_02 = 2;
    private static final int TUT_03 = 3;
    private static final int TUT_04 = 4;
    private static final int TUT_05 = 5;
    private static final int TUT_06 = 6;
    private static final int TUT_07 = 7;
    private static final int TUT_08 = 8;
    private static final int TUT_09 = 9;


    private static final int TUT_ANZAHL_PLUS_EINS = 10;




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
            case TUT_04:
                image = new Image(getClass().getResource("../resources/tut04.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_05:
                image = new Image(getClass().getResource("../resources/tut05.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_06:
                image = new Image(getClass().getResource("../resources/tut06.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_07:
                image = new Image(getClass().getResource("../resources/tut07.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
            case TUT_08:
                image = new Image(getClass().getResource("../resources/tut08.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
 /*           case TUT_09:
                image = new Image(getClass().getResource("../resources/tut09.png").toExternalForm());
                myImage.setImage(image);
                this.nextClicks++;
                break;
   */         default:
                this.buttonTest = false;
                image = new Image(getClass().getResource("../resources/tut.png").toExternalForm());
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
            case TUT_04:
                image = new Image(getClass().getResource("../resources/tut04.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_05:
                image = new Image(getClass().getResource("../resources/tut05.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_06:
                image = new Image(getClass().getResource("../resources/tut06.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_07:
                image = new Image(getClass().getResource("../resources/tut07.png").toExternalForm());
                myImage.setImage(image);
                break;
            case TUT_08:
                image = new Image(getClass().getResource("../resources/tut08.png").toExternalForm());
                myImage.setImage(image);
                break;
   /*         case TUT_09:
                image = new Image(getClass().getResource("../resources/tut09.png").toExternalForm());
                myImage.setImage(image);
                break;
     */       default:
                image = new Image(getClass().getResource("../resources/tut.png").toExternalForm());
                myImage.setImage(image);
        }
    }
}


