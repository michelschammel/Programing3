package quellen.view;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import quellen.model.*;

/**
 * Created by Cedric on 26.09.2017.
 * Controller for AddZitate
 */
public class AddZitateController {
    @FXML
    private TableView<Zitat> zitatTable;
    @FXML
    private TableColumn<Zitat, String> zitatColumn;

    private ObservableList<Zitat> zitatList;
    private Quelle quelle;

    @FXML
    private void initialize() {
        // Initialize the zitat table with the two columns.
        this.zitatColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        this.showZitatDeails(null);

        // Listen for selection changes and show the quelle details when changed.
        this.zitatTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZitatDeails(newValue));

        this.zitatTable.setRowFactory(tv -> {
            TableRow<Zitat> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        Zitat zitat = zitatTable.getSelectionModel().getSelectedItem();
                        System.out.println(zitatTable.getSelectionModel().getSelectedItem().getText() + ": " + zitatTable.getSelectionModel().getSelectedItem());
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

    public void setZitatList(ObservableList<Zitat> zitatList) {
        this.zitatList = zitatList;
        this.zitatTable.setItems(this.zitatList);
    }

    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;
    }
}
