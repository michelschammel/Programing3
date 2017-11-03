package quellen.service;

import javafx.collections.ObservableList;
import quellen.dao.SchnittstelleQuelle;
import quellen.model.Quelle;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Cedric Schreiner
 */
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

    public int insertNewQuelle(Quelle quelle) {
        return schnittstelleQuelle.insertNewQuelle(quelle);
    }

    public void deleteQuelle(Quelle quelle) {
        schnittstelleQuelle.deleteQuelle(quelle);
    }

    public ResultSet queryWithReturn(String sql) throws SQLException{
        System.out.println("Geht noch 1");
        return schnittstelleQuelle.queryWithReturn(sql);
    }
}
