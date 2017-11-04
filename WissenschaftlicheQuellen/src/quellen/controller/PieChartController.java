package quellen.controller;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import quellen.Interfaces.SourceDatabaseInterface;
import quellen.dao.SourceDatabaseControl;
import quellen.enums.DatabaseTables;
import quellen.model.Datenbank;

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
    private Datenbank db;
    private SourceDatabaseInterface database = new SourceDatabaseControl();

	@FXML
	private void setNumberOfDifferentSources() throws SQLException {
		//initialize array and database connection
		db = Datenbank.getInstance();
		List<Integer> pieZahlen = new ArrayList<>();

		//query database
		try {

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


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void setStats3() throws SQLException {
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
                } else if (date < 2000) {
                    werkeInZeiten[1]++;
                } else if ( date < 2100) {
                    werkeInZeiten[2]++;
                } else {
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
