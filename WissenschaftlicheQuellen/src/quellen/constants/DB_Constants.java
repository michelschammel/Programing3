package quellen.constants;

/**
 * Constants Class for all Selections/Updates/Deletes on the DB
 * @author Cedric Schreiner, Roman Berezin
 */
public class DB_Constants {
    //Prepared Statements to update a quelle
    public static final String PS_UPDATA_QUELLE = "UPDATE Quellen SET autor = ?, titel = ?, jahr = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ANDERES = "UPDATE Anderes SET auflage = ?, herausgeber = ?, ausgabe = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ARTIKEL = "UPDATE Artikel SET ausgabe = ?, magazin = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_BUCH = "UPDATE Bücher SET ISBN = ?, Verlag = ?, Auflage = ?, Monat = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ONLINEQUELLE = "UPDATE Onlinequellen SET url = ?, abrufdatum = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_WISSENSCHAFTLICHEARBEIT = "UPDATE WissenschaftlicheArbeiten SET hochschule = ?, herausgeber = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ZITAT = "UPDATE Zitate SET text = ? WHERE zitatId = ?";
    public static final String PS_UPDATE_TAG = "UPDATE Tags SET name = ? WHERE tagId = ?";

    //Statements to get all quelle
    public static final String S_GET_ANDERES = "SELECT * FROM Quellen NATURAL JOIN Anderes";
    public static final String S_GET_ARTIKEl = "SELECT * FROM Quellen NATURAL JOIN Artikel";
    public static final String S_GET_BUECHER = "SELECT * FROM Quellen NATURAL JOIN Bücher";
    public static final String S_GET_ONLINEQUELLEN = "SELECT * FROM Quellen NATURAL JOIN Onlinequellen";
    public static final String S_GET_WISSENSCHAFTLICHE_ARBEITEN = "SELECT * FROM Quellen NATURAL JOIN WissenschaftlicheArbeiten";
    public static final String S_GET_QUELLEN =  "select * from Quellen WHERE quellenId IN(\n" +
            "select id\n" +
            "from (\t\n" +
            "\tselect Quellen.quellenId as id, Anderes.quellenId as anderes, Artikel.quellenId as artikel, Bücher.quellenId as buch, Onlinequellen.quellenId as oquelle, WissenschaftlicheArbeiten.quellenId as wartikel\n" +
            "\tfrom Quellen \n" +
            "\tleft outer join Anderes ON Quellen.quellenId = Anderes.quellenId\n" +
            "\tleft outer join Artikel ON Quellen.quellenId = Artikel.quellenId \n" +
            "\tleft outer join Bücher ON Quellen.quellenId = Bücher.quellenId \n" +
            "\tleft outer join Onlinequellen ON Quellen.quellenId = Onlinequellen.quellenId \n" +
            "\tleft outer join WissenschaftlicheArbeiten ON Quellen.quellenId = WissenschaftlicheArbeiten.quellenId \n" +
            "\tWHERE anderes IS NULL \n" +
            "\tAND artikel IS NULL \n" +
            "AND buch IS NULL\n" +
            "AND oquelle IS NULL\n" +
            "AND wartikel IS NULL))";

    //Prepared Statements to get all zitate of a quelle
    public static final String PS_GET_ZITATTE = "SELECT * FROM Zitate WHERE quellenId = ?";

    //Prepared Statements to get all tags of a zitat
    public static final String PS_GET_TAGS = "SELECT * FROM Zitate NATURAL JOIN TagsZitate NATURAL JOIN Tags WHERE zitatId = ?";

    //Prepared Statement to insert
    public static final String PS_INSERT_ZITAT = "INSERT INTO Zitate (text, quellenId) VALUES (?, ?)";
    public static final String PS_INSERT_TAG = "INSERT INTO Tags (name) VALUES (?)";
    public static final String PS_INSERT_TAG_ZITAT_CONNECTION = "INSERT INTO TagsZitate VALUES (?, ?)";
    public static final String PS_INSERT_QUELLE = "INSERT INTO Quellen(autor, titel, jahr) VALUES (?, ?, ?)";
    public static final String PS_INSERT_ANDERES = "INSERT INTO Anderes VALUES (?, ?, ?, ?)";
    public static final String PS_INSERT_ARTIKEL = "INSERT INTO Artikel VALUES (?, ?, ?)";
    public static final String PS_INSERT_BUCH = "INSERT INTO Bücher VALUES (?, ?, ?, ?, ?)";
    public static final String PS_INSERT_ONLINEQUELLE = "INSERT INTO Onlinequellen VALUES (?, ?, ?)";
    public static final String PS_INSERT_WISSENSCHAFTLICHE_ARBEIT = "INSERT INTO WissenschaftlicheArbeiten VALUES (?, ?, ?)";

    //Statement to get the tagId
    public static final String PS_GET_LAST_INSERTED_TAG_ID = "SELECT seq FROM sqlite_sequence WHERE name = 'Tags'";

    //Statement to get the zitatId
    public static final String PS_GET_LAST_INSERTED_ZITAT_ID = "SELECT seq FROM sqlite_sequence WHERE name = 'Zitate'";

    //Statement to get the quellenId
    public static final String PS_GET_LAST_INSERTED_QUELLEN_ID = "SELECT seq FROM sqlite_sequence WHERE name = 'Quellen'";

    //Delete Zitate
    public static final String PS_DELETE_ZITAT = "DELETE FROM Zitate WHERE zitatId = ?";
    public static final String PS_DELETE_TAG_ZITAT_CONNECTION_WITH_ZITAT_ID = "DELETE FROM TagsZitate WHERE zitatId = ?";
    public static final String PS_DELETE_TAG_ZITAT_CONNECTION_WITH_TAG_ID = "DELETE FROM TagsZitate WHERE tagId = ?";
    public static final String PS_GET_TAG_ID_CONNECTION = "SELECT tagId FROM TagsZitate WHERE zitatId = ?";
    public static final String PS_DELETE_TAG = "DELETE FROM Tags WHERE tagId = ?";
    public static final String PS_GET_TAG_ID_OF_ZITAT = "SELECT tagId FROM TagsZitate NATURAL JOIN Tags WHERE zitatId = ?";

    //subcategories
    public static final String SC_ARTIKEL = "Artikel";
    public static final String SC_BUECHER = "Buch";
    public static final String SC_WARBEITEN = "W. Arbeit";
    public static final String SC_OQUELLEN = "Onlinequelle";
    public static final String SC_ANDERES = "Andere";
    public static final String SC_NONE = "<keine>";
    public static final String SC_TAGS = "Tags";
    public static final String SC_TAGSZITATE = "TagsZitate";
    public static final String SC_ZITATE = "Zitate";
    public static final String SC_QUELLEN = "Quellen";

    //Delete Quelle
    public static final String PS_DELETE_QUELLE = "DELETE FROM Quellen WHERE quellenId = ?";
    public static final String PS_DELETE_ANDERES = "DELETE FROM Anderes WHERE quellenId = ?";
    public static final String PS_DELETE_ARTIKEL = "DELETE FROM Artikel WHERE quellenId = ?";
    public static final String PS_DELETE_BUCH = "DELETE FROM Bücher WHERE quellenId = ?";
    public static final String PS_DELETE_ONLINEQUELLE = "DELETE FROM Onlinequellen WHERE quellenId = ?";
    public static final String PS_DELETE_WARBEIT = "DELETE FROM WissenschaftlicheArbeiten WHERE quellenId = ?";

    //Konstanten für die Statistiken
    public static final String PIECHART_STAT_1 = "select count (*) from ?";
    public static final String PIECHART_STAT_2 = "select count(distinct autor) from Quellen";
    public static final String PIECHART_STAT_3 = "select distinct autor from Quellen";
    public static final String PIECHART_STAT_4 = "select count(*) from Quellen where autor = ?";
    public static final String PIECHART_STAT_5 = "select jahr from Quellen";
}
