package quellen.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Datenbank {
	
	private static Datenbank instance;
	private static Connection c;
	
	private Datenbank() throws SQLException {
		invokeConnection();
	}
	
	/**
	 * 
	 * @return A Database object with a connection to the database.
	 * @throws SQLException 
	 */
	public static Datenbank getInstance() throws SQLException {
		
		if(instance == null) {
			Datenbank.instance = new Datenbank();
		}
		return Datenbank.instance;
		
	}
	
	/**
	 * Creates a new connection to the database if no connection already exists.
	 * @throws SQLException 
	 */
	private static void invokeConnection() throws SQLException {
		 if(c != null) {
			 return;
		 }
		 String path = "jdbc:sqlite:C:/Users/Tristan/Documents/Programing3/WissenschaftlicheQuellen/src/quellen/model/datenbank.sqlite";
	     c = DriverManager.getConnection(path);
	}
	
	public void queryWithoutReturn(String sql) throws SQLException {
		Statement stmt  = c.createStatement();
        stmt.executeQuery(sql);
	}
	
	public ResultSet queryWithReturn(String sql) throws SQLException {
		Statement stmt  = c.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
        return rs;
	}
}
