package quellen.service;

import javafx.collections.ObservableList;
import quellen.dao.SchnittstelleQuelle;
import quellen.model.Quelle;

public class QuellenService {
    private SchnittstelleQuelle schnittstelleQuelle;

    public QuellenService () {
        this.schnittstelleQuelle = new SchnittstelleQuelle();
    }

    public ObservableList<Quelle> getQuellenDataFromDB() {
        return schnittstelleQuelle.getQuellenFromDataBase();
    }

    public void updateQuelle(Quelle quelle) {
        schnittstelleQuelle.updateQuery(quelle);
    }

    public void insertNewQuelle(Quelle quelle) {
        schnittstelleQuelle.insertNewQuelle(quelle);
    }
}
