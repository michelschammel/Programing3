package quellen.controller;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import quellen.model.Datenbank;

import static quellen.constants.Controller_Constants.*;
import static quellen.constants.DB_Constants.*;


/**
 * Klasse zur Darstellung der Kuchendiagramme.
 * @author Roman Berezin
 */
public class PieChartController {
	String[] DatabaseTypes = {"Anderes", "Artikel", "Bücher", "Onlinequellen", "Quellen", "Tags", "TagsZitate",
			"WissenschaftlicheArbeiten", "Zitate"};

	@FXML
	ObservableList<PieChart.Data> pieChartData;
	@FXML
	PieChart pieChart;
	Datenbank db;


	private void initialize() {

	}

	@FXML
	private void setStats1() throws SQLException {
		//initialize array and database connection
		db = Datenbank.getInstance();
		int[] pieZahlen = new int[9];

		//query database
		try {
			for (int i = 0; i < DatabaseTypes.length; i++) {
				ResultSet result = db.queryWithReturn(PIECHART_STAT_1 + DatabaseTypes[i]);
				pieZahlen[i] = result.getInt(1);
				result.close();
			}

			//write data in piechart
			pieChartData = FXCollections.observableArrayList(
					   new PieChart.Data(SC_ANDERES, pieZahlen[0]),
					   new PieChart.Data(SC_ARTIKEL, pieZahlen[1]),
					   new PieChart.Data(SC_BUECHER, pieZahlen[2]),
					   new PieChart.Data(SC_OQUELLEN, pieZahlen[3]),
					   new PieChart.Data(SC_QUELLEN, pieZahlen[4]),
					   new PieChart.Data(SC_TAGS, pieZahlen[5]),
					   new PieChart.Data(SC_TAGSZITATE, pieZahlen[6]),
					   new PieChart.Data(SC_WARBEITEN, pieZahlen[7]),
					   new PieChart.Data(SC_ZITATE, pieZahlen[8]));
			pieChart.setData(pieChartData);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//not used currently
	@FXML
	private void setStats2() {
		pieChartData = FXCollections.observableArrayList(
				   new PieChart.Data("Iphone 5S", 13),
				   new PieChart.Data("Samsung Grand", 25),
				   new PieChart.Data("MOTO G", 10),
				   new PieChart.Data("Nokia Lumia", 22));

		pieChart.setData(pieChartData);

	}


	@FXML
	private void setStats3() throws SQLException {

		//initialize connection to database
		db = Datenbank.getInstance();
		ResultSet result = db.queryWithReturn(PIECHART_STAT_2);
		//result.next();
		int anzahlAutoren = result.getInt(1);
		result.close();
		ResultSet result2 = db.queryWithReturn(PIECHART_STAT_3);
		result2.next();
		int[] anzahlWerke = new int[anzahlAutoren];
		String[] autoren = new String[anzahlAutoren];

		//query data
		for (int i = 0; i < anzahlAutoren; i++) {
			String autor = result2.getString(1);
			ResultSet tempResult = Datenbank.queryWithReturn(PIECHART_STAT_4 + autor + "\"");
			tempResult.next();
			autoren[i] = autor;
			anzahlWerke[i] = tempResult.getInt(1);
			tempResult.next();
			result2.next();
			tempResult.close();
		}
		result2.close();

		//write data to piechart
		pieChartData = FXCollections.observableArrayList();
		for (int j = 0; j < anzahlAutoren; j++) {
			pieChartData.add(new PieChart.Data(autoren[j], anzahlWerke[j]));
		}
		pieChart.setData(pieChartData);
	}

	@FXML
	private void setStats4() throws SQLException{
	    //initialize connection to database
	    db = Datenbank.getInstance();
	    ResultSet result = db.queryWithReturn(PIECHART_STAT_5);
	    int[] werkeInZeiten = {0,0,0,0,0};

	    //query data
	    while (result.next()) {
	        String tempObject = result.getString(1);
	        try {
                int date = Integer.parseInt(tempObject);
                if (date < 1900) {
                    werkeInZeiten[0]++;
                } else if (date >= 1900 && date < 2000) {
                    werkeInZeiten[1]++;
                } else if (date >= 2000 && date < 2100) {
                    werkeInZeiten[2]++;
                } else if (date >= 2100) {
                    werkeInZeiten[3]++;
                }
            }catch (Exception e) {
                werkeInZeiten[4]++;
            }

        }
        result.close();

	    //write data to piechart
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("vor 1900", werkeInZeiten[0]),
                new PieChart.Data("20. Jh", werkeInZeiten[1]),
                new PieChart.Data("21. Jh", werkeInZeiten[2]),
                new PieChart.Data("aus der Zukunft", werkeInZeiten[3]),
                new PieChart.Data("nicht bekannt", werkeInZeiten[4])
        );
        pieChart.setData(pieChartData);


    }


}
