package quellen.view;
import java.sql.ResultSet;

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

	private void initialize() {

	}

	@FXML
	private void setStats1() {
		int[] pieZahlen = new int[9];
		try {
			for (int i = 0; i < DatabaseTypes.length; i++) {
				ResultSet result = Datenbank.queryWithReturn("select count (*) from " + DatabaseTypes[i]);
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

}
