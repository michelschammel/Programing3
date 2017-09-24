package quellen.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

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

	public ObservableList<Quelle> getQuellenFromDataBase() {
		ResultSet rsAnders;
		ResultSet rsZitate;
		ResultSet rsTags;
		try {
			//Create a new Observablelist
			ObservableList<Quelle> quelleList = FXCollections.observableArrayList();
			ObservableList<Zitat> zitatList = FXCollections.observableArrayList();

			//Step 1: Get all different types of quellen from the DB
			//First get all Anderes
			Statement statement = connection.createStatement();
			rsAnders = statement.executeQuery(S_GET_ANDERES);
			Anderes anderes;
			Zitat zitat;
			Tag tag;
			while (rsAnders.next()) {
				anderes = new Anderes(rsAnders.getInt("quellenId"),
						rsAnders.getString("titel"),
						rsAnders.getString("autor"),
						rsAnders.getString("jahr"),
						rsAnders.getString("herausgeber"),
						rsAnders.getString("auflage"),
						rsAnders.getString("ausgabe"));

				//Now we need to get all Zitate from the quelle
				PreparedStatement preparedStatementZitat = connection.prepareStatement(PS_GET_ZITATTE);
				preparedStatementZitat.setInt(1, anderes.getId());
				rsZitate = preparedStatementZitat.executeQuery();
				while(rsZitate.next()) {
					zitat = new Zitat(rsZitate.getString("text"),
							rsZitate.getInt("quellenId"),
							rsZitate.getInt("zitatId"));

					//Now we need to get for the Zitat all Tags
					PreparedStatement preparedStatementTags = connection.prepareStatement(PS_GET_TAGS);
					preparedStatementTags.setInt(1, zitat.getZitatId());
					rsTags = preparedStatementTags.executeQuery();
					while(rsTags.next()) {
						tag = new Tag(rsTags.getString("name"),
								rsTags.getInt("tagId"));
						zitat.addTag(tag);
					}
					anderes.addZitat(zitat);
				}
				quelleList.add(anderes);
			}
			return quelleList;

		} catch (SQLException e) {
			try {
				connection.rollback();
				return null;
			} catch (SQLException error) {
				System.out.println();
				return null;
			}
		}
	}

	/**
	 * gets called to update a quelle in the DB
	 * @param quelle quelle to update
	 * @return boolean (worken/failed)
	 */
	public boolean updateQuery(Quelle quelle) {
		try {
			//Preparestatemt for the update
			PreparedStatement pStatementQuelle = connection.prepareStatement(PS_UPDATA_QUELLE);
			//Deactivate autoCommit, without this we can commit both statements at the same time and if a error occurs
			//we can just do a rollback
			connection.setAutoCommit(false);
			//Every quelle is a instance of quelle so we can update it without problem
			pStatementQuelle.setString(1, quelle.getAutor());
			pStatementQuelle.setString(2, quelle.getTitel());
			pStatementQuelle.setString(3, quelle.getJahr());
			pStatementQuelle.setInt(4, quelle.getId());
			pStatementQuelle.executeUpdate();

			//Now we check what instance it is
			//For every instance we need a different update statement
			if (quelle instanceof Anderes) {
				//Preparestatemt for the update
				PreparedStatement pStatementAnderes = connection.prepareStatement(PS_UPDATE_ANDERES);

				//Cast quelle into anderes
				Anderes anderes = (Anderes)quelle;

				//fill statement with values
				pStatementAnderes.setString(1, anderes.getAuflage());
				pStatementAnderes.setString(2, anderes.getHerausgeber());
				pStatementAnderes.setString(3, anderes.getAusgabe());
				pStatementAnderes.setInt(4, anderes.getId());
				pStatementAnderes.executeUpdate();

			} else if (quelle instanceof Artikel) {
				//Preparestatemt for the update
				PreparedStatement pStatementArtikel = connection.prepareStatement(PS_UPDATE_ARTIKEL);

				//Cast quelle into artikel
				Artikel artikel = (Artikel)quelle;

				//fill statement with values
				pStatementArtikel.setString(1, artikel.getAusgabe());
				pStatementArtikel.setString(2, artikel.getMagazin());
				pStatementArtikel.setInt(3, artikel.getId());
				pStatementArtikel.executeUpdate();

			} else if (quelle instanceof Buch) {
				//Preparestatemt for the update
				PreparedStatement pStatementBuch = connection.prepareStatement(PS_UPDATE_BUCH);

				//Cast quelle into buch
				Buch buch = (Buch)quelle;

				//fill statement with values
				pStatementBuch.setString(1, buch.getIsbn());
				pStatementBuch.setString(2, buch.getHerausgeber());
				pStatementBuch.setString(3, buch.getAuflage());
				pStatementBuch.setString(4, buch.getMonat());
				pStatementBuch.setInt(5, buch.getId());
				pStatementBuch.executeUpdate();

			} else if (quelle instanceof Onlinequelle) {
				//Preparestatemt for the update
				PreparedStatement pStatementOnlineQuelle = connection.prepareStatement(PS_UPDATE_ONLINEQUELLE);

				//Cast quelle into onlinequelle
				Onlinequelle onlinequelle = (Onlinequelle)quelle;

				//fill statement with values
				pStatementOnlineQuelle.setString(1, onlinequelle.getUrl());
				pStatementOnlineQuelle.setString(2, onlinequelle.getAufrufdatum());
				pStatementOnlineQuelle.setInt(3, onlinequelle.getId());
				pStatementOnlineQuelle.executeUpdate();

			} else if (quelle instanceof  WissenschaftlicheArbeit) {
				//Preparestatemt for the update
				PreparedStatement pStatementWissenschaftlicheArbeit = connection.prepareStatement(PS_UPDATE_WISSENSCHAFTLICHEARBEIT);

				//Cast quelle into wissenschaftlicheArbeit
				WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit)quelle;

				//fill statement with values
				pStatementWissenschaftlicheArbeit.setString(1, wissenschaftlicheArbeit.getEinrichtung());
				pStatementWissenschaftlicheArbeit.setString(2, wissenschaftlicheArbeit.getHerausgeber());
				pStatementWissenschaftlicheArbeit.setInt(3, wissenschaftlicheArbeit.getId());
				pStatementWissenschaftlicheArbeit.executeUpdate();
			}
			//Commit both statements
			connection.commit();
			//turn autocommit on
			connection.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			try {
				connection.rollback();
				return false;
			} catch (SQLException error) {
				System.out.println();
				return false;
			}
		}
	}
}