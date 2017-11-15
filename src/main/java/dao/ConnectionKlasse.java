package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Cedric Schreienr
 */
public class ConnectionKlasse{
    /**
     * Method to create a connection
     * @return Connection to the DB
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:datenbank.sqlite");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
}
