package quellen.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;

import static quellen.constants.DB_Constants.*;

public class Datenbank {
	
	private static Datenbank instance;
	private static Connection connection;
	
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
		 if(connection != null) {
			 return;
		 }
		 String path = "jdbc:sqlite:datenbank.sqlite";
		connection = DriverManager.getConnection(path);
	}
	
	public static void queryWithoutReturn(String sql) throws SQLException {
		Statement stmt  = connection.createStatement();
        stmt.executeQuery(sql);
	}
	
	public static ResultSet queryWithReturn(String sql) throws SQLException {
		//needed type but not working with sqlite
		//Statement stmt = c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Statement stmt  = connection.createStatement();
		//Return the Resultset
        return stmt.executeQuery(sql);
	}

	public static void updateDatabase(String sql) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
	}
}