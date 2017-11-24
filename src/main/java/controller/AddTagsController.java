package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.TagViewInterface;

/**
 * Controller for AddTags.xml
 * @author Cedric Schreiner
 */
public class AddTagsController {
    @FXML
    private TableView<TagViewInterface> tagTable;
    @FXML
    private TableColumn<TagViewInterface, String> tagColumn;
    @FXML
    private TextField searchField;

    private ObservableList<TagViewInterface> tagList;
    private QuoteViewInterface quote;
    private ObservableList<TagViewInterface> searchTagList;
    private ObservableList<TagViewInterface> tmpList;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the quote table with the two columns.
        this.tagColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        this.showTagDeails(null);

        // Listen for selection changes and show the quelle details when changed.
        this.tagTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTagDeails(newValue));

        this.tagTable.setRowFactory(tv -> {
            TableRow<TagViewInterface> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) ->
                    handleRowMouseClick(event, row)
            );
            return row;
        });
    }

    /**
     *
     */
    private void handleRowMouseClick(MouseEvent event, TableRow<TagViewInterface> row) {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
            ObservableList<TagViewInterface> tagList = quote.getTagList();
            boolean addTag = true;

            for (TagViewInterface tag : tagList) {
                if (tag.getText().equals(tagTable.getSelectionModel().getSelectedItem().getText())) {
                    addTag = false;
                }
            }
            if (addTag) {
                quote.addTag(new viewmodels.TagView(tagTable.getSelectionModel().getSelectedItem().getText()));
            }
        }
    }

    /**
     * set the tagList that is later used for AddTags
     * @param tagList tagList used for AddTag
     */
    void setTagList(ObservableList<TagViewInterface> tagList) {
        this.tagList = tagList;
        this.tmpList = tagList;
        this.tagTable.setItems(this.tagList);
    }

    /**
     * Show All Tags
     * @param tag selected tag
     */
    private void showTagDeails(TagViewInterface tag) {
        if (tag != null) {
            tagTable.setItems(tagList);
        }
    }

    /**
     * Search for a specific tag
     */
    public void search() {
        if (tmpList != null) {
            tagList = tmpList;
        }
        this.searchTagList = FXCollections.observableArrayList();
        String searchText = this.searchField.getText().toLowerCase();
        this.tagList.forEach(tag -> {
            if (tag.getText().toLowerCase().contains(searchText)) {
                this.searchTagList.add(tag);
            }
        });
        tmpList = tagList;
        tagList = searchTagList;
        this.tagTable.setItems(this.tagList);
    }

    /**
     * reset the search and show all tags
     */
    public void reset() {
        this.tagList = tmpList;
        this.tagTable.setItems(this.tagList);
    }

    /**
     * Quote where the selected tags are added
     * @param quote add the tags to this quote
     */
    public void setQuote(QuoteViewInterface quote) {
        this.quote = quote;
    }
}
