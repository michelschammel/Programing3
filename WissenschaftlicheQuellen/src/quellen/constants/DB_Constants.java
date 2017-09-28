package quellen.constants;

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
    public static final String S_GET_QUELLEN =  "select *\n" +
            "from Quellen \n" +
            "WHERE quellenID IN (select quelle.quellenId\n" +
            "from (select Quellen.quellenId \n" +
            "from Quellen Natural join Artikel) as artikel, \n" +
            "(select Quellen.quellenId \n" +
            "from Quellen Natural join Onlinequellen) as onlinequellen,\n" +
            "(select Quellen.quellenId  \n" +
            "from Quellen Natural join Anderes) as anderes,\n" +
            "(select Quellen.quellenId \n" +
            "from Quellen Natural join Bücher) as buch,\n" +
            "(select Quellen.quellenId\n" +
            "from Quellen Natural join WissenschaftlicheArbeiten) as wissenschaftlicheArbeiten,\n" +
            "(select Quellen.quellenId \n" +
            "from Quellen) as quelle\n" +
            "where quelle.quellenId NOT IN (artikel.quellenId, onlinequellen.quellenId, anderes.quellenId, buch.quellenId, wissenschaftlicheArbeiten.quellenId))";

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

    //Delete Quelle
    public static final String PS_DELETE_QUELLE = "DELETE FROM Quellen WHERE quellenId = ?";
    public static final String PS_DELETE_ANDERES = "DELETE FROM Anderes WHERE quellenId = ?";
    public static final String PS_DELETE_ARTIKEL = "DELETE FROM Artikel WHERE quellenId = ?";
    public static final String PS_DELETE_BUCH = "DELETE FROM Bücher WHERE quellenId = ?";
    public static final String PS_DELETE_ONLINEQUELLE = "DELETE FROM Onlinequellen WHERE quellenId = ?";
    public static final String PS_DELETE_WARBEIT = "DELETE FROM WissenschaftlicheArbeiten WHERE quellenId = ?";
}
