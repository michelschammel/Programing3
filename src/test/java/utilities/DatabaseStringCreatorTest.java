package utilities;

import dao.ConnectionKlasse;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class DatabaseStringCreatorTest {

    private static Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();
        return con.getConnection();
    }

    @Test
    public void testSourceInsert() {

    }
}