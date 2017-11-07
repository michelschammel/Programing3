package quellen.controller;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import quellen.Interfaces.SourceDatabaseInterface;
import quellen.dao.SourceDatabaseImpl;
import quellen.enums.DatabaseTables;

import static quellen.constants.Controller_Constants.*;
import static quellen.constants.DB_Constants.*;


/**
 * Klasse zur Darstellung der Kuchendiagramme.
 * @author Roman Berezin
 */
public class PieChartController {

	@FXML
	private ObservableList<PieChart.Data> pieChartData;
	@FXML
	private PieChart pieChart;
    private SourceDatabaseInterface database = new SourceDatabaseImpl();

	@FXML
	private void setNumberOfDifferentSources() {
		List<Integer> pieZahlen = new ArrayList<>();

		//query database
		for (DatabaseTables type : DatabaseTables.values()) {
			pieZahlen.add(database.getNumberOfSources(type.name()));
		}

		pieChartData = FXCollections.observableArrayList(
				new PieChart.Data(SC_ANDERES,    pieZahlen.get(INDEX_0)),
				new PieChart.Data(SC_ARTIKEL,    pieZahlen.get(INDEX_1)),
				new PieChart.Data(SC_BUECHER,    pieZahlen.get(INDEX_2)),
				new PieChart.Data(SC_OQUELLEN,   pieZahlen.get(INDEX_3)),
				new PieChart.Data(SC_QUELLEN,    pieZahlen.get(INDEX_4)),
				new PieChart.Data(SC_TAGS,       pieZahlen.get(INDEX_5)),
				new PieChart.Data(SC_TAGSZITATE, pieZahlen.get(INDEX_6)),
				new PieChart.Data(SC_WARBEITEN,  pieZahlen.get(INDEX_7)),
				new PieChart.Data(SC_ZITATE,     pieZahlen.get(INDEX_8)));
		pieChart.setData(pieChartData);
	}

	@FXML
	private void setStats3() {
		//query data
		List<String> authorList = database.getAuthors();
		List<Integer> numberOfSources = new ArrayList<>();
		for (String author : authorList) {
			numberOfSources.add(database.getNumberOfSourcesFromAuthor(author));
		}

		//write data to piechart
		pieChartData = FXCollections.observableArrayList();
		for (int j = 0; j < authorList.size(); j++) {
			pieChartData.add(new PieChart.Data(authorList.get(j), numberOfSources.get(j)));
		}

		pieChart.setData(pieChartData);
	}

	@FXML
	private void setStats4() {
	    SourceTime sourceTime = new SourceTime();

		List<Integer> releaseDateList = database.getSourceRealeaseDates();
		for (int realeaseDate : releaseDateList) {
			if (realeaseDate < 1900) {
				sourceTime.beforeTwentiethCentury++;
			} else if (realeaseDate < 2000) {
				sourceTime.twentiethCentury++;
			} else if (realeaseDate < 2100) {
				sourceTime.twentyFirstCentury++;
			} else {
				sourceTime.future++;
			}
		}

	    //write data to piechart
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(BEFORE_TWENTIETH_CENTURY, sourceTime.beforeTwentiethCentury),
                new PieChart.Data(TWENTIETH_CENTURY, sourceTime.twentiethCentury),
                new PieChart.Data(TWENTY_FIRST_CENTURY, sourceTime.twentyFirstCentury),
                new PieChart.Data(FUTURE, sourceTime.future)
        );
        pieChart.setData(pieChartData);


    }

    private class SourceTime {
		int beforeTwentiethCentury = 0;
		int twentiethCentury = 0;
		int twentyFirstCentury = 0;
		int future = 0;
	}
}
