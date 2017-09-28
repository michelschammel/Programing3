package quellen.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import quellen.model.*;

import javax.tools.Tool;

/**
 * Created by Cedric on 26.09.2017.
 * Controller for AddZitate
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
                final Zitat zitat = row.getItem();
                tagContextMenu.getItems().clear();
                double x;
                double y;

                if (row.isHover() && zitat != null) {
                    Tooltip zitatToolTip = new Tooltip(zitat.getText());
                    zitatToolTip.setWrapText(true);
                    zitatToolTip.setMaxWidth(160);
                    row.setTooltip(zitatToolTip);
                    zitat.getTagList().forEach( tag -> {
                        MenuItem tagMenu = new MenuItem(tag.getText());
                        tagContextMenu.getItems().add(tagMenu);
                    });
                    Window window = zitatTable.getScene().getWindow();
                    x = window.getX() + window.getWidth() - 3;
                    y = window.getY() + 95;
                    if (goUp) {
                        y += 24 * (Math.round((this.yMouseCoordinate - 69) / 24) + 1);
                    } else {
                        y += 24 * (Math.round((this.yMouseCoordinate - 69) / 24) + 2);
                    }
                    tagContextMenu.hide();
                    tagContextMenu.show(addZitatStage, x, y);
                }
            });

            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        //create new zitat
                        Zitat zitat = new Zitat(zitatTable.getSelectionModel().getSelectedItem().getText(), quelle.getId());
                        //set every tagId to 0
                        zitatTable.getSelectionModel().getSelectedItem().getTagList().forEach( tag -> {
                            zitat.getTagList().add(new Tag(tag.getText()));
                        });
                        quelle.getZitatList().add(zitat);
                    }
                }
            });
            return row;
        });
    }

    private void showZitatDeails(Zitat zitat) {
        if (zitat != null) {
            zitatTable.setItems(zitatList);
        }
    }

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

    public void reset() {
        this.zitatList = tmpList;
        this.zitatTable.setItems(this.zitatList);
    }

    public void setStage(Stage stage) {
        this.addZitatStage = stage;
    }

    public void setZitatList(ObservableList<Zitat> zitatList) {
        this.zitatList = zitatList;
        this.zitatTable.setItems(this.zitatList);
    }

    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;
    }
}
