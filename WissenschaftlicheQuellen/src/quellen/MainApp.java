package quellen;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import quellen.model.Datenbank;
import quellen.model.Quelle;
import quellen.model.Zitat;
import quellen.view.QuellenOverviewController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Zitate.
     */
    private ObservableList<Quelle> quellenData= FXCollections.observableArrayList();

    /**
     * Constructor
     * @throws SQLException 
     */
    public MainApp() throws SQLException {
    	String sql = "SELECT * FROM Quellen";
    	ResultSet rs = null;
			rs = Datenbank.getInstance().queryWithReturn(sql);
    	try {
			while(rs.next()) {
				// Add some sample data
				Quelle testQuelle = new Quelle(rs.getString("autor"),rs.getString("titel"),rs.getString("jahr"));
				testQuelle.addZitat(new Zitat("This is a test."));
				quellenData.add(testQuelle);
			}
		} catch (SQLException e) {
		}    
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Wissenschaftliche Quellen");

        initRootLayout();

        showQuellenOverview();
    }
    
    
    

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the quellen overview inside the root layout.
     */
    public void showQuellenOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/quellenOverview.fxml"));
            AnchorPane quellenOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(quellenOverview);

            // Give the controller access to the main app.
            QuellenOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Quelle> getQuellenList() {
        
        return this.quellenData;
    }
}