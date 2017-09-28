package quellen.controller;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import quellen.model.Datenbank;

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
		db = Datenbank.getInstance();
		int[] pieZahlen = new int[9];
		try {
			for (int i = 0; i < DatabaseTypes.length; i++) {
				ResultSet result = db.queryWithReturn("select count (*) from " + DatabaseTypes[i]);
				pieZahlen[i] = result.getInt(1);
				result.close();
			}
			pieChartData = FXCollections.observableArrayList(
					   new PieChart.Data("Anderes", pieZahlen[0]),
					   new PieChart.Data("Artikel", pieZahlen[1]),
					   new PieChart.Data("Bücher", pieZahlen[2]),
					   new PieChart.Data("Onlinequellen", pieZahlen[3]),
					   new PieChart.Data("Quellen", pieZahlen[4]),
					   new PieChart.Data("Tags", pieZahlen[5]),
					   new PieChart.Data("TagsZitate", pieZahlen[6]),
					   new PieChart.Data("W. Arbeiten", pieZahlen[7]),
					   new PieChart.Data("Zitate", pieZahlen[8]));
			pieChart.setData(pieChartData);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}



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
		db = Datenbank.getInstance();
		ResultSet result = db.queryWithReturn("select count(distinct autor) from Quellen");
		//result.next();
		int anzahlAutoren = result.getInt(1);
		result.close();
		ResultSet result2 = db.queryWithReturn("select distinct autor from Quellen");
		result2.next();
		int[] anzahlWerke = new int[anzahlAutoren];
		String[] autoren = new String[anzahlAutoren];

		for (int i = 0; i < anzahlAutoren; i++) {
			String autor = result2.getString(1);
			ResultSet tempResult = Datenbank.queryWithReturn("select count(*) from Quellen where autor = \"" + autor + "\"");
			tempResult.next();
			autoren[i] = autor;
			anzahlWerke[i] = tempResult.getInt(1);
			tempResult.next();
			result2.next();
			tempResult.close();
		}
		result2.close();
		pieChartData = FXCollections.observableArrayList();
		for (int j = 0; j < anzahlAutoren; j++) {
			pieChartData.add(new PieChart.Data(autoren[j], anzahlWerke[j]));
		}
		pieChart.setData(pieChartData);
	}


}
