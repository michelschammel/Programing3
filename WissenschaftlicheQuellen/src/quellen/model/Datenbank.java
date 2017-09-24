package quellen.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
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

	public static void updateDatabase(String sql) throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
	}

	public ObservableList<Quelle> getQuellenFromDataBase() {
		ResultSet rsQuellen;
		try {
			//Create a new Observablelist
			ObservableList<Quelle> quelleList = FXCollections.observableArrayList();
			ObservableList<Zitat> zitatList = FXCollections.observableArrayList();

			//Step 1: Get all different types of quellen from the DB
			//First get all "anderes"
			Statement statement = connection.createStatement();
            rsQuellen = statement.executeQuery(S_GET_ANDERES);
			Anderes anderes;
			while (rsQuellen.next()) {
				anderes = new Anderes(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"),
                        rsQuellen.getString("herausgeber"),
                        rsQuellen.getString("auflage"),
                        rsQuellen.getString("ausgabe"));
				quelleList.add(anderes);
			}

			//get all "artikel"
			rsQuellen = statement.executeQuery(S_GET_ARTIKEl);
			Artikel artikel;
			while (rsQuellen.next()) {
			    artikel = new Artikel(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"),
                        rsQuellen.getString("ausgabe"),
                        rsQuellen.getString("magazin"));
			    quelleList.add(artikel);
            }

            //get all "buecher"
            rsQuellen = statement.executeQuery(S_GET_BUECHER);
			Buch buch;
			while (rsQuellen.next()) {
			    buch = new Buch(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"),
                        rsQuellen.getString("Verlag"),
                        rsQuellen.getString("Auflage"),
                        rsQuellen.getString("Monat"),
                        rsQuellen.getString("ISBN"));
			    quelleList.add(buch);
            }

            //get all "onlinequellen"
            rsQuellen = statement.executeQuery(S_GET_ONLINEQUELLEN);
			Onlinequelle onlinequelle;
            while (rsQuellen.next()) {
                onlinequelle = new Onlinequelle(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"),
                        rsQuellen.getString("abrufdatum"),
                        rsQuellen.getString("url"));
                quelleList.add(onlinequelle);
            }

            //get all "wissenschaftliche arbeiten"
            rsQuellen = statement.executeQuery(S_GET_WISSENSCHAFTLICHE_ARBEITEN);
            WissenschaftlicheArbeit wissenschaftlicheArbeit;
            while (rsQuellen.next()) {
                wissenschaftlicheArbeit = new WissenschaftlicheArbeit(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"),
                        rsQuellen.getString("herausgeber"),
                        rsQuellen.getString("hochschule"));
                quelleList.add(wissenschaftlicheArbeit);
            }

            //Step 2: Get all Zitate of all quellen
            quelleList.forEach( quelle -> {
                final ResultSet rsZitate;
                Zitat zitat;
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(PS_GET_ZITATTE);
                    preparedStatement.setInt(1, quelle.getId());
                    rsZitate = preparedStatement.executeQuery();
                    while (rsZitate.next()) {
                        zitat = new Zitat(rsZitate.getString("text"),
                                rsZitate.getInt("quellenId"),
                                rsZitate.getInt("zitatId"));
                        quelle.addZitat(zitat);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });

            //Step 3: Get all Tags for a Zitat
            quelleList.forEach( quelle -> quelle.getZitatList().forEach( zitat -> {
                final  ResultSet rsTags;
                Tag tag;
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(PS_GET_TAGS);
                    preparedStatement.setInt(1, zitat.getZitatId());
                    rsTags = preparedStatement.executeQuery();
                    while (rsTags.next()) {
                        tag = new Tag(rsTags.getString("name"),
                                rsTags.getInt("tagId"));
                        zitat.addTag(tag);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }));

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
	 * @return boolean (worked/failed)
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
			//We just updated the Quelle without zitate/tags
            //Update zitate
            quelle.getZitatList().forEach( zitat -> {
                try {
                    if (zitat.getZitatId() != 0) {
                        //Zitat exists in the DB
                        PreparedStatement preparedStatement = connection.prepareStatement(PS_UPDATE_ZITAT);
                        preparedStatement.setString(1, zitat.getText());
                        preparedStatement.setInt(2, zitat.getZitatId());
                        preparedStatement.executeUpdate();
                    } else {
                        //Insert Zitat into DB
                        PreparedStatement preparedStatemen = connection.prepareStatement(PS_INSERT_ZITAT);
                        preparedStatemen.setString(1, zitat.getText());
                        preparedStatemen.setInt(2, zitat.getQuellenId());
                        preparedStatemen.execute();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            });


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