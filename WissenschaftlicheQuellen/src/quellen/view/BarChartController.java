package quellen.view;


import java.sql.ResultSet;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import quellen.model.*;
import java.util.*;


public class BarChartController {
		String[] DatabaseTypes = {"Anderes", "Artikel", "Bücher", "Onlinequellen", "Quellen", "Tags", "TagsZitate",
				"WissenschaftlicheArbeiten", "Zitate"};


	    @FXML
	    private BarChart<String, Integer> barChart;

	    @FXML
	    private CategoryAxis xAxis;

	    private ObservableList<String> quellen = FXCollections.observableArrayList();
	    private ObservableList<String> authors = FXCollections.observableArrayList();

	    /**
	     * Initializes the controller class. This method is automatically called
	     * after the fxml file has been loaded.
	     */
	    @FXML
	    private void initialize() {

	    }



	    //eintraege pro quelle
	    @FXML
	    private void setStats1() {
	    	try {
	    		//set xAxis categories
	    		String[] types = {"Anderes", "Artikel", "Bücher", "Onlinequellen", "Quellen", "Tags", "TagsZitate",
	    				"W. Arbeiten", "Zitate"};
	    		quellen.addAll(Arrays.asList(types));
	    		xAxis.setCategories(quellen);


	    		//query database and set yAxis values
	    		XYChart.Series<String, Integer> series = new XYChart.Series<>();
	    		for (int i = 0; i < types.length; i++) {
	    			ResultSet result = Datenbank.queryWithReturn("select count (*) from " + DatabaseTypes[i]);
	    			int quellen = result.getInt(1);
	    			series.getData().add(new XYChart.Data<>(types[i], quellen));
	    			result.close();
	    		}

	    		barChart.getData().add(series);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }

	   //quellen pro autor
	   @FXML
	   private void setStats2() {
		   try {
			   //set xAxis categories
	    		ResultSet result = Datenbank.queryWithReturn("select autor from quellen");
			    List<String> list = new LinkedList<String>();
			    while (result.next()) {
			    	list.add(result.getString(1));
			    } //count the authors
			    Object[] temp = list.toArray();
			    String[] autoren = new String[temp.length];
			    for (int i = 0; i < temp.length; i++) {
			    	autoren[i] = temp[i].toString();
			    }
	    		authors.addAll(Arrays.asList(autoren));
	    		xAxis.setCategories(authors);


	    		//query database and set yAxis values
	    		XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
	    		ResultSet result2 = Datenbank.queryWithReturn("select count (*) from Quellen group by autor");
	    		int counter = 0;
	    		while (result2.next()) {
	    			series2.getData().add(new XYChart.Data<>(autoren[counter], result2.getInt(1)));
	    			counter++;
	    		}
	    		result2.close();
	    		barChart.getData().add(series2);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}


	   }

	   //clear the chart
	   @FXML
	   private void clearChart() {
		   barChart.getData().clear();
		   //TODO clear xAxis aswell
	   }



}




