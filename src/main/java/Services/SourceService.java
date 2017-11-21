package Services;

import dao.SourceDatabaseImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.interfaces.SourceInterface;
import utilities.SourceUtillities;
import viewmodels.interfaces.SourceViewInterface;
import java.util.List;

/**
 * Created by Cedric on 21.11.2017.
 * Service for the UI
 */
public abstract class SourceService {

    public static ObservableList<SourceViewInterface> getSourcesFromDatabase() {
        List<SourceInterface> sourceList = SourceDatabaseImpl.getAllSourcesFromDatabase();
        ObservableList<SourceViewInterface> sourceViewList = FXCollections.observableArrayList();
        for (SourceInterface source : sourceList) {
            SourceViewInterface sourceView = SourceUtillities.convertSourceToSourceView(source);
            sourceViewList.add(sourceView);
        }
        return sourceViewList;
    }

    public static void deleteSource(SourceViewInterface sourceView) {
        SourceInterface source = SourceUtillities.convertSourceViewToSource(sourceView);
        SourceDatabaseImpl.deleteSource(source);
    }
}
