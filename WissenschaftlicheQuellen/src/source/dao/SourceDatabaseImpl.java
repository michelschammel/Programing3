package source.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import source.Interfaces.SourceDatabaseInterface;
import source.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static source.constants.SourceDatabaseImplConstants.*;
import static source.constants.DB_Constants.*;

/**
 * @author Cedric Schreiner
 */
public class SourceDatabaseImpl implements SourceDatabaseInterface {
    /**
     * get a connection to the DB
     * @return connection
     */
    private Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();

        return con.getConnection();
    }

    /**
     * get All Quellen from the DB
     * @return all source as ObservableList
     */
    public ObservableList<Quelle> getQuellenFromDataBase() {
        ResultSet rsQuellen;

        try (Connection connection = this.getConnection()){
            //Create a new Observablelist
            ObservableList<Quelle> quelleList = FXCollections.observableArrayList();

            //Step 1: Get all different types of source from the DB
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

            //get the leftover quelle
            rsQuellen = statement.executeQuery(S_GET_QUELLEN);
            Quelle LeftOverquelle;
            while (rsQuellen.next()) {
                LeftOverquelle = new Quelle(rsQuellen.getInt("quellenId"),
                        rsQuellen.getString("titel"),
                        rsQuellen.getString("autor"),
                        rsQuellen.getString("jahr"));
                quelleList.add(LeftOverquelle);
            }

            //Step 2: Get all Zitate of all source
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
                final ResultSet rsTags;
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
            return null;
        }
    }

    /**
     * gets called to update a quelle in the DB
     * this method checks what instance of quelle it is and updates it.
     * it even checks what Zitate/Tags are still in the DB but are not anymore in quelle and deletes them.
     * @param quelle quelle to update
     */
    public void updateQuery(Quelle quelle) {
        try (Connection connection = this.getConnection()) {
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
                    Anderes anderes = (Anderes) quelle;

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
                    Artikel artikel = (Artikel) quelle;

                    //fill statement with values
                    pStatementArtikel.setString(1, artikel.getAusgabe());
                    pStatementArtikel.setString(2, artikel.getMagazin());
                    pStatementArtikel.setInt(3, artikel.getId());
                    pStatementArtikel.executeUpdate();

                } else if (quelle instanceof Buch) {
                    //Preparestatemt for the update
                    PreparedStatement pStatementBuch = connection.prepareStatement(PS_UPDATE_BUCH);

                    //Cast quelle into buch
                    Buch buch = (Buch) quelle;

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
                    Onlinequelle onlinequelle = (Onlinequelle) quelle;

                    //fill statement with values
                    pStatementOnlineQuelle.setString(1, onlinequelle.getUrl());
                    pStatementOnlineQuelle.setString(2, onlinequelle.getAufrufdatum());
                    pStatementOnlineQuelle.setInt(3, onlinequelle.getId());
                    pStatementOnlineQuelle.executeUpdate();

                } else if (quelle instanceof WissenschaftlicheArbeit) {
                    //Preparestatemt for the update
                    PreparedStatement pStatementWissenschaftlicheArbeit = connection.prepareStatement(PS_UPDATE_WISSENSCHAFTLICHEARBEIT);

                    //Cast quelle into wissenschaftlicheArbeit
                    WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit) quelle;

                    //fill statement with values
                    pStatementWissenschaftlicheArbeit.setString(1, wissenschaftlicheArbeit.getEinrichtung());
                    pStatementWissenschaftlicheArbeit.setString(2, wissenschaftlicheArbeit.getHerausgeber());
                    pStatementWissenschaftlicheArbeit.setInt(3, wissenschaftlicheArbeit.getId());
                    pStatementWissenschaftlicheArbeit.executeUpdate();
                }

                //We just updated the Quelle without zitate/tags
                //Update zitate
                quelle.getZitatList().forEach(zitat -> {
                    ResultSet rsZitatId;
                    try {
                        if (zitat.getZitatId() != 0) {
                            //Zitat exists in the DB
                            PreparedStatement preparedStatementA = connection.prepareStatement(PS_UPDATE_ZITAT);
                            preparedStatementA.setString(1, zitat.getText());
                            preparedStatementA.setInt(2, zitat.getZitatId());
                            preparedStatementA.executeUpdate();
                        } else {
                            //Insert Zitat into DB
                            PreparedStatement preparedStatemenB = connection.prepareStatement(PS_INSERT_ZITAT);
                            preparedStatemenB.setString(1, zitat.getText());
                            preparedStatemenB.setInt(2, zitat.getQuellenId());
                            preparedStatemenB.execute();
                            //Get ZitatId
                            Statement statement = connection.createStatement();
                            rsZitatId = statement.executeQuery(PS_GET_LAST_INSERTED_ZITAT_ID);
                            rsZitatId.next();
                            zitat.setZitatId(rsZitatId.getInt("seq"));

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                //Update all Tags
                quelle.getZitatList().forEach(zitat -> zitat.getTagList().forEach(tag -> {

                    ResultSet rsTagId;
                    try {
                        if (tag.getTagId() != 0) {
                            //Tag exists in the DB
                            System.out.println("Tag vorhanden und wird geupdated");
                            PreparedStatement preparedStatement = connection.prepareStatement(PS_UPDATE_TAG);
                            preparedStatement.setString(1, tag.getText());
                            preparedStatement.setInt(2, tag.getTagId());
                            preparedStatement.executeUpdate();
                        } else {
                            //Insert Tag into DB
                            System.out.println("Tag wird neu eingefügt");
                            PreparedStatement preparedStatement = connection.prepareStatement(PS_INSERT_TAG);
                            preparedStatement.setString(1, tag.getText());
                            preparedStatement.execute();

                            //Get TagId
                            Statement statement = connection.createStatement();
                            rsTagId = statement.executeQuery(PS_GET_LAST_INSERTED_TAG_ID);
                            //Has only 1 value
                            rsTagId.next();

                            PreparedStatement preparedStatementTag = connection.prepareStatement(PS_INSERT_TAG_ZITAT_CONNECTION);
                            preparedStatementTag.setInt(1, zitat.getZitatId());
                            preparedStatementTag.setInt(2, rsTagId.getInt("seq"));
                            preparedStatementTag.execute();
                            tag.setTagId(rsTagId.getInt("seq"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));

                //Delete all tags in the db that were removed from a zitat
                quelle.getZitatList().forEach(zitat -> {
                    try {
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        ResultSet rsTagIds;
                        //Get all tagIds that are used in this zitat
                        PreparedStatement preparedStatementGetTagIds = connection.prepareStatement(PS_GET_TAG_ID_OF_ZITAT);
                        preparedStatementGetTagIds.setInt(1, zitat.getZitatId());
                        rsTagIds = preparedStatementGetTagIds.executeQuery();
                        while (rsTagIds.next()) {
                            arrayList.add(rsTagIds.getInt("tagId"));
                        }
                        //check if they are still in use
                        zitat.getTagList().forEach(tag -> {
                            for (int i = 0; i < arrayList.size(); i++) {
                                if (arrayList.get(i) == tag.getTagId()) {
                                    //Tag is still used
                                    arrayList.remove(i);
                                }
                            }
                        });
                        //Arraylist contains all  tag ids that are not used anymore
                        //Delete tag and connection to zitat from DB
                        arrayList.forEach(tagId -> {
                            try {
                                PreparedStatement preparedStatementDeleteTagZitatConnection = connection.prepareStatement(PS_DELETE_TAG_ZITAT_CONNECTION_WITH_TAG_ID);
                                preparedStatementDeleteTagZitatConnection.setInt(1, tagId);
                                preparedStatementDeleteTagZitatConnection.execute();
                                PreparedStatement preparedStatementDeleteTag = connection.prepareStatement(PS_DELETE_TAG);
                                preparedStatementDeleteTag.setInt(1, tagId);
                                preparedStatementDeleteTag.execute();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });


                //Delete zitate that were removed from the quelle
                //Get all zitate
                ResultSet rsZitate;
                PreparedStatement preparedStatementGetZitate = connection.prepareStatement(PS_GET_ZITATTE);
                preparedStatementGetZitate.setInt(1, quelle.getId());
                rsZitate = preparedStatementGetZitate.executeQuery();
                ArrayList<Integer> arrayList = new ArrayList<>();
                while (rsZitate.next()) {
                    arrayList.add(rsZitate.getInt("zitatId"));
                }

                quelle.getZitatList().forEach(zitat -> {
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i) == zitat.getZitatId()) {
                            //Zitat is still available in quelle
                            arrayList.remove(i);
                        }
                    }
                });

                //arrayList contains all zitatIds that we need to delete
                arrayList.forEach(zitatId -> {
                    ResultSet rsTagIdsLambda;
                    try {
                        //First delete zitat
                        PreparedStatement preparedStatementDeleteZitat = connection.prepareStatement(PS_DELETE_ZITAT);
                        preparedStatementDeleteZitat.setInt(1, zitatId);
                        preparedStatementDeleteZitat.execute();

                        //get all tagIds that we need to delete
                        PreparedStatement preparedStatementGetTagId = connection.prepareStatement(PS_GET_TAG_ID_CONNECTION);
                        preparedStatementGetTagId.setInt(1, zitatId);
                        rsTagIdsLambda = preparedStatementGetTagId.executeQuery();
                        while (rsTagIdsLambda.next()) {
                            PreparedStatement preparedStatementDeleteTags = connection.prepareStatement(PS_DELETE_TAG);
                            preparedStatementDeleteTags.setInt(1, rsTagIdsLambda.getInt("tagId"));
                            preparedStatementDeleteTags.execute();
                        }

                        //Delete connection between tag and zitat
                        PreparedStatement preparedStatementDeleteConnection = connection.prepareStatement(PS_DELETE_TAG_ZITAT_CONNECTION_WITH_ZITAT_ID);
                        preparedStatementDeleteConnection.setInt(1, zitatId);
                        preparedStatementDeleteConnection.execute();
                    } catch (SQLException error) {
                        error.printStackTrace();
                    }
                });

                //Commit both statements
                connection.commit();
                //turn autocommit on
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a new Quelle
     * @param quelle
     * @return
     */
    public int insertNewQuelle(Quelle quelle) {
        try (Connection connection = this.getConnection()){
            connection.setAutoCommit(false);
            ResultSet rsQuellenID;
            //Insert the standard attributes of Quelle (Author, Titel, Year)
            PreparedStatement preparedStatementInsertQuelle = connection.prepareStatement(PS_INSERT_QUELLE);
            preparedStatementInsertQuelle.setString(1, quelle.getAutor());
            preparedStatementInsertQuelle.setString(2, quelle.getTitel());
            preparedStatementInsertQuelle.setString(3, quelle.getJahr());
            preparedStatementInsertQuelle.execute();
            Statement statementGetQuellenId = connection.createStatement();
            rsQuellenID = statementGetQuellenId.executeQuery(PS_GET_LAST_INSERTED_QUELLEN_ID);
            rsQuellenID.next();
            quelle.setId(rsQuellenID.getInt("seq"));

            //Check what instance quelle is an insert the leftover attributes
            if (quelle instanceof Anderes) {
                PreparedStatement preparedStatementInsertAnderes = connection.prepareStatement(PS_INSERT_ANDERES);
                Anderes anderes = (Anderes)quelle;
                preparedStatementInsertAnderes.setInt(1, anderes.getId());
                preparedStatementInsertAnderes.setString(2, anderes.getAuflage());
                preparedStatementInsertAnderes.setString(3, anderes.getHerausgeber());
                preparedStatementInsertAnderes.setString(4, anderes.getAusgabe());
                preparedStatementInsertAnderes.execute();

            } else if (quelle instanceof  Artikel) {
                PreparedStatement preparedStatementInsertArtikel = connection.prepareStatement(PS_INSERT_ARTIKEL);
                Artikel artikel = (Artikel)quelle;
                preparedStatementInsertArtikel.setInt(1, artikel.getId());
                preparedStatementInsertArtikel.setString(2, artikel.getAusgabe());
                preparedStatementInsertArtikel.setString(3, artikel.getMagazin());
                preparedStatementInsertArtikel.execute();

            } else if (quelle instanceof Buch) {
                PreparedStatement preparedStatementInsertBuch = connection.prepareStatement(PS_INSERT_BUCH);
                Buch buch = (Buch)quelle;
                preparedStatementInsertBuch.setInt(1, buch.getId());
                preparedStatementInsertBuch.setString(2, buch.getIsbn());
                preparedStatementInsertBuch.setString(3, buch.getHerausgeber());
                preparedStatementInsertBuch.setString(4, buch.getAuflage());
                preparedStatementInsertBuch.setString(5, buch.getMonat());
                preparedStatementInsertBuch.execute();

            } else if (quelle instanceof  Onlinequelle) {
                PreparedStatement preparedStatementInsertOnlinequelle = connection.prepareStatement(PS_INSERT_ONLINEQUELLE);
                Onlinequelle onlinequelle = (Onlinequelle)quelle;
                preparedStatementInsertOnlinequelle.setInt(1, onlinequelle.getId());
                preparedStatementInsertOnlinequelle.setString(2, onlinequelle.getUrl());
                preparedStatementInsertOnlinequelle.setString(3, onlinequelle.getAufrufdatum());
                preparedStatementInsertOnlinequelle.execute();

            } else if (quelle instanceof WissenschaftlicheArbeit) {
                PreparedStatement preparedStatementInsertWArbeit = connection.prepareStatement(PS_INSERT_WISSENSCHAFTLICHE_ARBEIT);
                WissenschaftlicheArbeit wissenschaftlicheArbeit = (WissenschaftlicheArbeit)quelle;
                preparedStatementInsertWArbeit.setInt(1, wissenschaftlicheArbeit.getId());
                preparedStatementInsertWArbeit.setString(2, wissenschaftlicheArbeit.getEinrichtung());
                preparedStatementInsertWArbeit.setString(3, wissenschaftlicheArbeit.getHerausgeber());
                preparedStatementInsertWArbeit.execute();
            }
            connection.commit();
            connection.setAutoCommit(true);

            quelle.getZitatList().forEach( zitat -> {
                zitat.setQuellenId(quelle.getId());
            });
            //Update function inserts all zitate and tags

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Just send the quelle to the update function
        //this Method then inserts Zitate and Tags
        this.updateQuery(quelle);
        return quelle.getId();
    }

    /**
     * Delete quelle from DB
     * @param quelle quelle to delete
     */
    public void deleteQuelle(Quelle quelle) {
        try (Connection connection = this.getConnection()) {
            //delete zitatlist from quelle;
            quelle.getZitatList().clear();
            this.updateQuery(quelle);

            //Delete quelle without zitat
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementDeleteQuelle = connection.prepareStatement(PS_DELETE_QUELLE);
            preparedStatementDeleteQuelle.setInt(1, quelle.getId());
            preparedStatementDeleteQuelle.execute();

            if (quelle instanceof Anderes) {
                PreparedStatement preparedStatementDeleteAnderes = connection.prepareStatement(PS_DELETE_ANDERES);
                preparedStatementDeleteAnderes.setInt(1, quelle.getId());
                preparedStatementDeleteAnderes.execute();

            } else if (quelle instanceof Artikel) {
                PreparedStatement preparedStatementDeleteArtikel = connection.prepareStatement(PS_DELETE_ARTIKEL);
                preparedStatementDeleteArtikel.setInt(1, quelle.getId());
                preparedStatementDeleteArtikel.execute();

            } else if (quelle instanceof Buch) {
                PreparedStatement preparedStatementDeleteBuch = connection.prepareStatement(PS_DELETE_BUCH);
                preparedStatementDeleteBuch.setInt(1, quelle.getId());
                preparedStatementDeleteBuch.execute();

            } else if (quelle instanceof Onlinequelle) {
                PreparedStatement preparedStatementDeleteOnlineQuelle = connection.prepareStatement(PS_DELETE_ONLINEQUELLE);
                preparedStatementDeleteOnlineQuelle.setInt(1, quelle.getId());
                preparedStatementDeleteOnlineQuelle.execute();

            } else if (quelle instanceof WissenschaftlicheArbeit) {
                PreparedStatement preparedStatementWissenschaftlichArbeit = connection.prepareStatement(PS_DELETE_WARBEIT);
                preparedStatementWissenschaftlichArbeit.setInt(1, quelle.getId());
                preparedStatementWissenschaftlichArbeit.execute();

            }
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfSources(String type) {
        int typeCount = 0;
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()){
            ResultSet rs;
            String selectStatement = PIECHART_STAT_1.replace("?", type);
            rs = statement.executeQuery(selectStatement);

            rs.next();
            typeCount =  rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeCount;
    }

    public List<String> getAuthors() {
        List<String> authorList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet rs;
            rs = statement.executeQuery(PIECHART_STAT_3);

            while(rs.next()) {
                authorList.add(rs.getString(INDEX_1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorList;
    }

    public int getNumberOfSourcesFromAuthor(String author) {
        int numberOfSources = 0;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(PIECHART_STAT_4)) {
            ResultSet rs;
            statement.setString(INDEX_1, author);
            rs = statement.executeQuery();

            rs.next();
            numberOfSources = rs.getInt(INDEX_1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfSources;
    }

    public List<Integer> getSourceRealeaseDates() {
        List<Integer> releaseDateList = new ArrayList<>();

        try(Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet rs;
            rs = statement.executeQuery(PIECHART_STAT_5);

            while(rs.next()) {
                releaseDateList.add(rs.getInt(INDEX_1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return releaseDateList;
    }
}
