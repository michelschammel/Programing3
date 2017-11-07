package source.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Cedric Schreienr
 */
public class ConnectionKlasse{
    /**
     * Creates a connection to the DB
     */
    private Properties properties;

    public ConnectionKlasse() {
        properties = new Properties();
        InputStream is;

        try {
            is = ConnectionKlasse.class.getClassLoader().getResourceAsStream("DBconfig.properties");
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to create a connection
     * @return Connection to the DB
     */
    public Connection getConnection() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(properties.getProperty("class-name"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
}
