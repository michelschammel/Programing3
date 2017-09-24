package quellen.constants;

public class DB_Constants {
    //Prepared Statements to update a quelle
    public static final String PS_UPDATA_QUELLE = "UPDATE Quellen SET autor = ?, titel = ?, jahr = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ANDERES = "UPDATE Anderes SET auflage = ?, herausgeber = ?, ausgabe = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ARTIKEL = "UPDATE Artikel SET ausgabe = ?, magazin = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_BUCH = "UPDATE Bücher SET ISBN = ?, Verlag = ?, Auflage = ?, Monat = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ONLINEQUELLE = "UPDATE Onlinequellen SET url = ?, abrufdatum = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_WISSENSCHAFTLICHEARBEIT = "UPDATE WissenschaftlicheArbeiten SET hochschule = ?, herausgeber = ? WHERE quellenId = ?";

    //Statements to get all quelle
    public static final String S_GET_ANDERES = "SELECT * FROM Quellen NATURAL JOIN Anderes";
    public static final String S_GET_ARTIKEl = "SELECT * FROM Quellen NATURAL JOIN Artikel";
    public static final String S_GET_BUECHER = "SELECT * FROM Quellen NATURAL JOIN Bücher";
    public static final String S_GET_ONLINEQUELLEN = "SELECT * FROM Quellen NATURAL JOIN Onlinequellen";
    public static final String S_GET_WISSENSCHAFTLICHE_ARBEITEN = "SELECT * FROM Quellen NATURAL JOIN WissenschaftlicheArbeiten";

    //Prepared Statements to get all zitate of a quelle
    public static final String PS_GET_ZITATTE = "SELECT * FROM Zitate WHERE quellenId = ?";

    //Prepared Statements to get all tags of a zitat
    public static final String PS_GET_TAGS = "SELECT * FROM Zitate NATURAL JOIN TagsZitate NATURAL JOIN Tags WHERE zitatId = ?";
}
