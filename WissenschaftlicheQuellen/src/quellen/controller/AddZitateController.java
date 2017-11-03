package quellen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import quellen.model.*;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * Created by Cedric on 26.09.2017.
 * Controller for AddZitate
 * @author Cedric Schreiner
 */
public class AddZitateController {
    @FXML
    private TableView<Zitat> zitatTable;
    @FXML
    private TableColumn<Zitat, String> zitatColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Zitat> zitatList;
    private ObservableList<Zitat> searchZitatList;
    private ObservableList<Zitat> tmpList;
    private Quelle quelle;
    private Stage addZitatStage;
    private ContextMenu tagContextMenu;
    private double yMouseCoordinate;
    private boolean goUp;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the zitat table with the two columns.
        this.zitatColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        this.showZitatDeails(null);

        //Create a new Contextmenu for zitattags
        tagContextMenu = new ContextMenu();

        // Listen for selection changes and show the quelle details when changed.
        this.zitatTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZitatDeails(newValue));

        this.zitatTable.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                goUp = (event.getY() < yMouseCoordinate);
                yMouseCoordinate = event.getY();
            }
        });

        this.zitatTable.setRowFactory(tv -> {
            TableRow<Zitat> row = new TableRow<>();

            row.hoverProperty().addListener((observable) -> {
                rowHover(row);
            });

            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    handleRowDoubleClick(event, row);
                }
            });
            return row;
        });
    }

    /**
     * Show all zitate
     * @param zitat selected zitat
     */
    private void showZitatDeails(Zitat zitat) {
        if (zitat != null) {
            zitatTable.setItems(zitatList);
        }
    }

    private void handleRowDoubleClick(MouseEvent event, TableRow<Zitat> row) {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
            boolean isNewZitat = true;
            boolean isNewTag;
            //text of the selected zitat
            String zitatText = zitatTable.getSelectionModel().getSelectedItem().getText();

            //check if a zitat like this already exists in quelle
            for (Zitat zitat: quelle.getZitatList()) {
                if (zitat.getText().toLowerCase().equals(zitatText.toLowerCase())) {
                    isNewZitat = false;
                    //the zitat exists already in quelle
                    //check if the zitat has new tags that the zitat of quelle doesnt have
                    for(Tag newZitatTag: zitatTable.getSelectionModel().getSelectedItem().getTagList()) {
                        isNewTag = true;
                        for (Tag quelleTag: zitat.getTagList()) {
                            if (newZitatTag.getText().toLowerCase().equals(quelleTag.getText().toLowerCase())) {
                                isNewTag = false;
                            }
                        }
                        if (isNewTag) {
                            zitat.addTag(new Tag(newZitatTag.getText()));
                        }
                    }
                }
            }

            if (isNewZitat) {
                //create new zitat
                Zitat zitat = new Zitat(zitatTable.getSelectionModel().getSelectedItem().getText(), quelle.getId());
                //set every tagId to 0
                zitatTable.getSelectionModel().getSelectedItem().getTagList().forEach(tag ->
                        zitat.getTagList().add(new Tag(tag.getText()))
                );
                quelle.getZitatList().add(zitat);
            }
        }
    }

    private void rowHover(TableRow<Zitat> row) {
        final Zitat zitat = row.getItem();
        tagContextMenu.getItems().clear();

        if (row.isHover() && zitat != null) {
            Tooltip zitatToolTip = new Tooltip(zitat.getText());
            zitatToolTip.setWrapText(true);
            zitatToolTip.setMaxWidth(160);
            row.setTooltip(zitatToolTip);
            zitat.getTagList().forEach( tag -> {
                MenuItem tagMenu = new MenuItem(tag.getText());
                tagContextMenu.getItems().add(tagMenu);
            });

            Point2D contextPosition = calculateContextMenuPosition();
            tagContextMenu.hide();
            tagContextMenu.show(addZitatStage, contextPosition.getX(), contextPosition.getY());
        }
    }

    private Point2D calculateContextMenuPosition() {
        Point2D contextPosition = new Point2D.Double();
        double x;
        double y;

        Window window = zitatTable.getScene().getWindow();
        x = window.getX() + window.getWidth() - 3;
        y = window.getY() + 95;

        if (goUp) {
            y += 24 * (Math.round((this.yMouseCoordinate - 69) / 24) + 1);
        } else {
            y += 24 * (Math.round((this.yMouseCoordinate - 69) / 24) + 2);
        }

        contextPosition.setLocation(x, y);
        return contextPosition;
    }

    /**
     * search for a specific zitat
     */
    public void search() {
        if (tmpList != null) {
            zitatList = tmpList;
        }
        this.searchZitatList = FXCollections.observableArrayList();
        String searchText = this.searchField.getText().toLowerCase();
        this.zitatList.forEach(zitat -> {
            if (zitat.getText().toLowerCase().contains(searchText)) {
                this.searchZitatList.add(zitat);
            }
        });
        tmpList = zitatList;
        zitatList = searchZitatList;
        this.zitatTable.setItems(this.zitatList);
    }

    /**
     * reset the search and show all tags
     */
    public void reset() {
        this.zitatList = tmpList;
        this.zitatTable.setItems(this.zitatList);
    }

    /**
     * Set the Stage
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.addZitatStage = stage;
    }

    /**
     * set the zitatlist that is later displayed
     * @param zitatList contains all zitate with tags
     */
    public void setZitatList(ObservableList<Zitat> zitatList) {
        this.zitatList = zitatList;
        this.tmpList = zitatList;
        this.zitatTable.setItems(this.zitatList);
    }

    /**
     * @param quelle add zitate to this quelle
     */
    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;
    }
}
