package quellen.constants;

public class DB_Constants {
    //Prepared Statements to update a quelle
    public static final String PS_UPDATA_QUELLE = "UPDATE Quellen SET autor = ?, titel = ?, jahr = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ANDERES = "UPDATE Anderes SET auflage = ?, herausgeber = ?, ausgabe = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ARTIKEL = "UPDATE Artikel SET ausgabe = ?, magazin = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_BUCH = "UPDATE Bücher SET ISBN = ?, Verlag = ?, Auflage = ?, Monat = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_ONLINEQUELLE = "UPDATE Onlinequellen SET url = ?, abrufdatum = ? WHERE quellenId = ?";
    public static final String PS_UPDATE_WISSENSCHAFTLICHEARBEIT = "UPDATE WissenschaftlicheArbeiten SET hochschule = ?, herausgeber = ? WHERE quellenId = ?";
}
