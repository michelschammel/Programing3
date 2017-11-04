package quellen.Interfaces;

import javafx.collections.ObservableList;
import quellen.model.Quelle;

import java.util.List;

public interface SourceDatabaseInterface {

    ObservableList<Quelle> getQuellenFromDataBase();

    void updateQuery(Quelle source);

    int insertNewQuelle(Quelle quelle);

    void deleteQuelle(Quelle quelle);

    int getNumberOfSources(String type);

    List<String> getAuthors();

    int getNumberOfSourcesFromAuthor(String author);

    List<Integer> getSourceRealeaseDates();
}
