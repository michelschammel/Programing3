package quellen.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionKlasse {
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
     * Methode um eine Verbindung aufzubauen
     *
     * @return Connection
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
