package main_app;

import controller.RootLayoutController;
import factories.SourceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Quote;
import models.interfaces.*;
import utilities.SourceConverter;
import viewmodels.interfaces.*;

import static main_app.constants.MainAppConstants.*;


/**
 * @author Björn Schmidt
 * @author Michel Schammel
 * @author Roman Berezin
 * @author Cedric Schreiner
 */
public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WISSENSCHAFTLICHE_QUELLEN);

        testall();
        initRootLayout();
    }

    private void testall() {
        Quote quote = new Quote(1, "source text", 1);
        Quote quote1 = new Quote(2, "source1 text", 1);
        Quote quote2 = new Quote(3, "source2 text", 1);
        Quote quote3 = new Quote(4, "source3 text", 1);
        TagInterface tag = new models.Tag("tag", 1);
        TagInterface tag1 = new models.Tag("tag2", 2);
        quote.addTag(tag);
        quote.addTag(tag1);

        SourceInterface source = SourceFactory.produceSource(SourceFactory.STANDARD);
        source.setTitle("source title");
        source.setAuthor("source author");
        source.setId(1);
        source.setYear("source year");
        source.addQuote(quote);
        source.addQuote(quote1);
        source.addQuote(quote2);
        source.addQuote(quote3);
        SourceViewInterface sourceView = SourceConverter.convertSourceToSourceView(source);
        System.out.println(sourceView);
        SourceInterface sourceConvert = SourceConverter.convertSourceViewToSource(sourceView);
        System.out.println(sourceConvert);

        ArticleInterface article = (ArticleInterface) SourceFactory.produceSource(SourceFactory.ARTICLE);
        article.setEdition("article edition");
        article.setMagazine("article magazine");
        article.setId(2);
        article.setTitle("article title");
        article.setAuthor("article author");
        article.setYear("article year");
        ArticleViewInterface articleView = (ArticleViewInterface) SourceConverter.convertSourceToSourceView(article);
        System.out.println(articleView);

        BookInterface book = (BookInterface) SourceFactory.produceSource(SourceFactory.BOOK);
        book.setEdition("book edition");
        book.setIsbn("book ISBN");
        book.setMonth("book month");
        book.setPublisher("book publisher");
        book.setId(3);
        book.setTitle("book title");
        book.setAuthor("book author");
        book.setYear("book year");
        BookViewInterface bookView = (BookViewInterface) SourceConverter.convertSourceToSourceView(book);
        System.out.println(bookView);

        OnlineSourceInterface onlineSource = (OnlineSourceInterface) SourceFactory.produceSource(SourceFactory.ONLINE_SOURCE);
        onlineSource.setPollingDate("online polling date");
        onlineSource.setUrl("oneline url");
        onlineSource.setAuthor("online author");
        onlineSource.setId(4);
        onlineSource.setTitle("onlone title");
        onlineSource.setYear("online year");
        OnlinesourceViewInterface onlinesourceView = (OnlinesourceViewInterface) SourceConverter.convertSourceToSourceView(onlineSource);
        System.out.println(onlinesourceView);

        OthersInterface others = (OthersInterface) SourceFactory.produceSource(SourceFactory.OTHER);
        others.setEdition("other edition");
        others.setPublisher("other publisher");
        others.setAuthor("other author");
        others.setId(5);
        others.setTitle("other title");
        others.setYear("other year");
        OthersViewInterface othersView = (OthersViewInterface) SourceConverter.convertSourceToSourceView(others);
        System.out.println(othersView);

        ScientificWorkInterface scientificWork = (ScientificWorkInterface) SourceFactory.produceSource(SourceFactory.SCIENTIFIC_WORK);
        scientificWork.setInstitution("work institution");
        scientificWork.setPublisher("work publisher");
        scientificWork.setAuthor("work author");
        scientificWork.setId(6);
        scientificWork.setTitle("work title");
        scientificWork.setYear("work year");
        ScientificWorkViewInterface scientificWorkView = (ScientificWorkViewInterface) SourceConverter.convertSourceToSourceView(scientificWork);
        System.out.println(scientificWorkView);
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(ROOT_LAYOUT_FXML));
            BorderPane rootLayout = loader.load();
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setStage(primaryStage);
            rootLayoutController.showSourceOverview();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
