package quellen.controller;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import quellen.model.*;

public class AddTagsController {
    @FXML
    private TableView<Tag> tagTable;
    @FXML
    private TableColumn<Tag, String> tagColumn;

    private ObservableList<Tag> tagList;
    private Zitat zitat;

    @FXML
    private void initialize() {
        // Initialize the zitat table with the two columns.
        this.tagColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        this.showTagDeails(null);

        // Listen for selection changes and show the quelle details when changed.
        this.tagTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTagDeails(newValue));

        this.tagTable.setRowFactory(tv -> {
            TableRow<Tag> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (!row.isEmpty())) {
                        ObservableList<Tag> tagList = zitat.getTagList();
                        boolean addTag = true;
                        for (int i = 0; i < tagList.size(); i++) {
                            if (tagList.get(i).getText().equals(tagTable.getSelectionModel().getSelectedItem().getText())) {
                                addTag = false;
                            }
                        }
                        if (addTag) {
                            zitat.addTag(new Tag(tagTable.getSelectionModel().getSelectedItem()));
                        }
                    }
                }
            });
            return row;
        });
    }

    public void setTagList(ObservableList<Tag> tagList) {
        this.tagList = tagList;
        this.tagTable.setItems(this.tagList);
    }

    private void showTagDeails(Tag tag) {
        if (tag != null) {
            tagTable.setItems(tagList);
        }
    }

    public void addTag() {
        System.out.println("Add");
    }

    public void setZitat(Zitat zitat) {
        this.zitat = zitat;
    }
}
