package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import viewmodels.QuoteView;
import viewmodels.interfaces.QuoteViewInterface;
import viewmodels.interfaces.SourceViewInterface;
import viewmodels.interfaces.TagViewInterface;

import java.awt.geom.Point2D;


/**
 * Controller for AddZitate
 * @author Cedric Schreiner
 */
public class AddQuoteController {
    @FXML
    private TableView<QuoteViewInterface> zitatTable;
    @FXML
    private TableColumn<QuoteViewInterface, String> zitatColumn;
    @FXML
    private TextField searchField;

    private ObservableList<QuoteViewInterface> zitatList;
    private ObservableList<QuoteViewInterface> searchZitatList;
    private ObservableList<QuoteViewInterface> tmpList;
    private SourceViewInterface quelle;
    private Stage addZitatStage;
    private ContextMenu tagContextMenu;
    private double yMouseCoordinate;
    private boolean goUp;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the zitat table with the two columns.
        this.zitatColumn.setCellValueFactory(cellData -> cellData.getValue().getTextProperty());
        this.showZitatDeails(null);

        //Create a new Contextmenu for zitattags
        tagContextMenu = new ContextMenu();

        // Listen for selection changes and show the quelle details when changed.
        this.zitatTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showZitatDeails(newValue));

        this.zitatTable.setOnMouseMoved((MouseEvent event) -> {
            goUp = (event.getY() < yMouseCoordinate);
            yMouseCoordinate = event.getY();
        });

        this.zitatTable.setRowFactory(tv -> {
            TableRow<QuoteViewInterface> row = new TableRow<>();

            row.hoverProperty().addListener((observable) ->
                rowHover(row)
            );

            row.setOnMouseClicked((MouseEvent event) ->
                handleRowDoubleClick(event, row)
            );
            return row;
        });
    }

    /**
     * Show all zitate
     * @param quote selected zitat
     */
    private void showZitatDeails(QuoteViewInterface quote) {
        if (quote != null) {
            zitatTable.setItems(zitatList);
        }
    }

    private void handleRowDoubleClick(MouseEvent event, TableRow<QuoteViewInterface> row) {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
            boolean isNewZitat = true;
            boolean isNewTag;
            //text of the selected zitat
            String zitatText = zitatTable.getSelectionModel().getSelectedItem().getText();

            //check if a zitat like this already exists in quelle
            for (QuoteViewInterface quote: quelle.getQuoteList()) {
                if (quote.getText().toLowerCase().equals(zitatText.toLowerCase())) {
                    isNewZitat = false;
                    //the zitat exists already in quelle
                    //check if the zitat has new tags that the zitat of quelle doesnt have
                    for(TagViewInterface newQuoteTag: zitatTable.getSelectionModel().getSelectedItem().getTagList()) {
                        isNewTag = true;
                        for (TagViewInterface sourceTag: quote.getTagList()) {
                            if (newQuoteTag.getText().toLowerCase().equals(sourceTag.getText().toLowerCase())) {
                                isNewTag = false;
                            }
                        }
                        if (isNewTag) {
                            quote.addTag(new viewmodels.TagView(newQuoteTag.getText()));
                        }
                    }
                }
            }

            if (isNewZitat) {
                //create new zitat
                QuoteViewInterface quote = new QuoteView(zitatTable.getSelectionModel().getSelectedItem().getText(), quelle.getId());
                //set every tagId to 0
                zitatTable.getSelectionModel().getSelectedItem().getTagList().forEach(tag ->
                        quote.getTagList().add(new viewmodels.TagView(tag.getText()))
                );
                quelle.getQuoteList().add(quote);
            }
        }
    }

    private void rowHover(TableRow<QuoteViewInterface> row) {
        final QuoteViewInterface quote = row.getItem();
        tagContextMenu.getItems().clear();

        if (row.isHover() && quote != null) {
            Tooltip zitatToolTip = new Tooltip(quote.getText());
            zitatToolTip.setWrapText(true);
            zitatToolTip.setMaxWidth(160);
            row.setTooltip(zitatToolTip);
            quote.getTagList().forEach( tag -> {
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
    void setStage(Stage stage) {
        this.addZitatStage = stage;
    }

    /**
     * set the quoteList that is later displayed
     * @param quoteList contains all quotes with tags
     */
    void setZitatList(ObservableList<QuoteViewInterface> quoteList) {
        this.zitatList = quoteList;
        this.tmpList = quoteList;
        this.zitatTable.setItems(this.zitatList);
    }

    /**
     * @param source add quote to this source
     */
    public void setQuelle(SourceViewInterface source) {
        this.quelle = source;
    }
}
