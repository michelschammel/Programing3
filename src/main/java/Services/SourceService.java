package Services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.interfaces.SourceInterface;
import utilities.DatabaseStringCreator;
import utilities.SourceConverter;
import viewmodels.interfaces.SourceViewInterface;
import java.util.List;

/**
 * Created by Cedric on 21.11.2017.
 * Service for the UI
 */
public abstract class SourceService {

    public static ObservableList<SourceViewInterface> getSourcesFromDatabase() {
        List<SourceInterface> sourceList = DatabaseStringCreator.getAllSourcesFromDatabase();
        ObservableList<SourceViewInterface> sourceViewList = FXCollections.observableArrayList();
        for (SourceInterface source : sourceList) {
            SourceViewInterface sourceView = SourceConverter.convertSourceToSourceView(source);
            sourceViewList.add(sourceView);
        }
        return sourceViewList;
    }

    public static void deleteSource(SourceViewInterface sourceView) {
        SourceInterface source = SourceConverter.convertSourceViewToSource(sourceView);
        DatabaseStringCreator.deleteSource(source);
    }
}
