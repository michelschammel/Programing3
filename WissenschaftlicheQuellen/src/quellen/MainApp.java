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
import quellen.model.Quelle;
import quellen.model.Zitat;
import quellen.model.Datenbank;
import quellen.view.QuellenOverviewController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * The data as an observable list of Quelle.
     */
    private ObservableList<Quelle> quellenData= FXCollections.observableArrayList();
    
    /**
     * The Constructor.
     * @throws SQL Exception
     */
    public MainApp() throws SQLException {
        /*String sql = "SELECT * FROM Quellen";
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
        }*/    
        // Add some sample data
        Quelle testQuelle = new Quelle("Test", "Goethe", "1888");
        Quelle testQuelle2 = new Quelle("Test2", "Schiller", "1919");
        Quelle testQuelle3 = new Quelle("Test3", "Marx", "1901");
        Quelle testQuelle4 = new Quelle("Test4", "Müller", "2005s");
        testQuelle.addZitat(new Zitat("This is a test."));
        testQuelle.addZitat(new Zitat("What happens when you have 2 zitate"));
        testQuelle2.addZitat(new Zitat("This is also a test."));
        testQuelle3.addZitat(new Zitat("This is a third test."));
        testQuelle4.addZitat(new Zitat("This is a fourth test."));
        
        quellenData.add(testQuelle);
        quellenData.add(testQuelle2);
        quellenData.add(testQuelle3);
        quellenData.add(testQuelle4);
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
            // Load quellen overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/quellenOverview.fxml"));
            AnchorPane quellenOverview = (AnchorPane) loader.load();

            // Set quellen overview into the center of root layout.
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
