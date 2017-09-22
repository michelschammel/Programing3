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
import javafx.stage.Modality;
import javafx.stage.Stage;
import quellen.model.Quelle;
import quellen.model.Zitat;
import quellen.model.Datenbank;
import quellen.view.QuellenEditDialogController;
import quellen.view.QuellenOverviewController;
import quellen.view.RootLayoutController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Quelle.
     */
    private ObservableList<Quelle> quellenData= FXCollections.observableArrayList();

    /**
     * The Constructor.
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
        // Add some sample data
        Quelle testQuelle = new Quelle("Test", "Goethe", "1888");
        Quelle testQuelle2 = new Quelle("Test2", "Schiller", "1919");
        Quelle testQuelle3 = new Quelle("Test3", "Marx", "1901");
        Quelle testQuelle4 = new Quelle("Test4", "Müller", "2005s");
        Zitat zitat1 = new Zitat("This is a test.");
        Zitat zitat2 = new Zitat("What happens when you have 2 zitate");
        Zitat zitat3 = new Zitat("This is also a test.");
        Zitat zitat4 = new Zitat("This is a third test.");
        Zitat zitat5 = new Zitat("This is a fourth test.");
        zitat1.addTag("first Tag");
        zitat1.addTag("second Tag");
        zitat1.addTag("third Tag");
        zitat2.addTag("first tag of second zitat");
        zitat2.addTag("second tag of sedond zitat");
        testQuelle.addZitat(zitat1);
        testQuelle.addZitat(zitat2);
        testQuelle2.addZitat(zitat3);
        testQuelle3.addZitat(zitat4);
        testQuelle4.addZitat(zitat5);

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
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

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
     * Opens a dialog to edit details for the specified quelle. If the user
     * clicks OK, the changes are saved into the provided quellen object and true
     * is returned.
     *
     * @param quelle the quelle object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showQuellenEditDialog(Quelle quelle) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/QuellenEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the quelle into the controller.
            QuellenEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setQuelle(quelle);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
    /**
     * Search for Tag/Author/Source/Quote
     *
     */
    public void search(String searchText, boolean searchAuthor, boolean searchTag, boolean searchSource, boolean searchQuote) {

    }

    public void showBarChart() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/BarChart.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("BarChart");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            //BarChartController controller = loader.getController();

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPieChart() {
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PieChart.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("PieChart");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the persons into the controller.
            //BarChartController controller = loader.getController();

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
