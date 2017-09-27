package quellen.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quellen.model.*;

import java.sql.*;
import java.util.ArrayList;

import static quellen.constants.DB_Constants.*;

public class SchnittstelleQuelle {

    private Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();

        return con.getConnection();
    }

    public ObservableList<Quelle> getQuellenFromDataBase() {
        ResultSet rsQuellen;

        try (Connection connection = this.getConnection()){
            //Create a new Observablelist
            ObservableList<Quelle> quelleList = FXCollections.observableArrayList();

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
                    System.out.println("rollback");
                    connection.rollback();
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
