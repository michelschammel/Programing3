package dao;

import factories.QuoteFactory;
import factories.SourceFactory;
import models.interfaces.ObjectTemplateInterface;
import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;
import models.interfaces.TagInterface;
import utilities.SourceUtillities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static dao.constants.SourceDatabaseImplConstants.*;
import static dao.constants.DB_Constants.*;

/**
 * @author Cedric Schreiner
 */
public abstract class SourceDatabaseImpl  {

    public static int getNumberOfSources(String type) {
        int typeCount = 0;
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()){
            ResultSet rs;
            String selectStatement = PIECHART_STAT_1.replace("?", type);
            rs = statement.executeQuery(selectStatement);

            rs.next();
            typeCount =  rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeCount;
    }

    public static List<String> getAuthors() {
        List<String> authorList = new ArrayList<>();
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet rs;
            rs = statement.executeQuery(PIECHART_STAT_3);

            while(rs.next()) {
                authorList.add(rs.getString(INDEX_1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorList;
    }

    public static int getNumberOfSourcesFromAuthor(String author) {
        int numberOfSources = 0;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(PIECHART_STAT_4)) {
            ResultSet rs;
            statement.setString(INDEX_1, author);
            rs = statement.executeQuery();

            rs.next();
            numberOfSources = rs.getInt(INDEX_1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfSources;
    }

    public static List<Integer> getSourceRealeaseDates() {
        List<Integer> releaseDateList = new ArrayList<>();

        try(Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet rs;
            rs = statement.executeQuery(PIECHART_STAT_5);

            while(rs.next()) {
                releaseDateList.add(rs.getInt(INDEX_1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return releaseDateList;
    }

    private static final String INSERT_BASE_SOURCE = "INSERT INTO Source(title, year, author) VALUES ('%s', '%s', '%s')";
    private static final String GET_SOURCE_ID = "SELECT seq FROM sqlite_sequence WHERE name = 'Source'";
    private static final String UPDATE_BASE_SOURCE = "UPDATE Source SET author = '%s', year = '%s', title = '%s' WHERE id = %d";

    private static final String[] databaseTables = {"Source", "Article", "Book", "Onlinesource", "Others", "ScientificWork"};

    public static void deleteSource(SourceInterface source) {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            for (QuoteInterface quote : source.getQuoteList()) {
                deleteQuote(quote, connection);
            }

            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM Source WHERE id = " + source.getId());

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<SourceInterface> getAllSourcesFromDatabase() {
        List<SourceInterface> sourceList = new ArrayList<>();
        try (Connection connection = getConnection()){
            connection.setAutoCommit(false);
            List<ObjectTemplateInterface> template;
            SourceInterface source;
            SourceInterface tmp;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Source");
            while(resultSet.next()) {
                tmp = SourceFactory.produceSource(SourceFactory.SOURCE);
                if (tmp != null) {
                    tmp.setId(resultSet.getInt("id"));
                    tmp.setAuthor(resultSet.getString("author"));
                    tmp.setTitle(resultSet.getString("title"));
                    tmp.setYear(resultSet.getString("year"));
                    tmp.setQuoteList(getSourceQuotes(tmp, connection));
                    sourceList.add(tmp);
                }
            }

            for (String tableName : databaseTables) {
                if (!tableName.equals("Source")) {
                    source = SourceFactory.produceSource(tableName);
                    if (source!= null) {
                        resultSet = statement.executeQuery("SELECT * FROM Source NATURAL JOIN " + tableName);
                        template = SourceUtillities.getTemplate(source);
                        SourceUtillities.removeBasicAttributes(template);

                        while (resultSet.next()) {
                            for (int i = 0; i < sourceList.size(); i++) {
                                SourceInterface listSource = sourceList.get(i);
                                if (listSource.getId() == resultSet.getInt("id")) {
                                    source.setId(listSource.getId());
                                    source.setAuthor(listSource.getAuthor());
                                    source.setTitle(listSource.getTitle());
                                    source.setYear(listSource.getYear());
                                    source.setQuoteList(listSource.getQuoteList());
                                    for (ObjectTemplateInterface templateRow : template) {
                                        SourceUtillities.setObjectValue(templateRow.getAttributeName(), source, resultSet.getObject(templateRow.getAttributeName()));
                                    }
                                    sourceList.set(i, source);
                                }
                            }
                        }
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sourceList;
    }

    private static List<QuoteInterface> getSourceQuotes(SourceInterface source,Connection connection) throws SQLException {
        List<QuoteInterface> quoteList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Quote WHERE sourceId = " + source.getId());
        QuoteInterface quote;

        while(resultSet.next()) {
            quote = QuoteFactory.createQuote();
            quote.setText(resultSet.getString("text"));
            quote.setSourceId(source.getId());
            quote.setId(resultSet.getInt("id"));
            quote.setTagList(getQuoteTags(quote, connection));
            quoteList.add(quote);
        }

        return quoteList;
    }

    private static List<TagInterface> getQuoteTags(QuoteInterface quote, Connection connection) throws SQLException{
        List<TagInterface> tagList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Tags.tagId, Tags.name FROM Tags NATURAL JOIN QuoteTags WHERE QuoteTags.quoteId = " + quote.getId());
        TagInterface tag;

        while(resultSet.next()) {
            tag = new models.Tag();
            tag.setText(resultSet.getString("name"));
            tag.setId(resultSet.getInt("tagId"));
            tagList.add(tag);
        }

        return tagList;
    }

    private static boolean isSourceExtension (SourceInterface source) {
        Class sourceClass = source.getClass();
        return !(sourceClass.getSimpleName().equals("Source"));
    }

    public static void insertOrUpdateSource(SourceInterface source){
        boolean isSourceExtension = isSourceExtension(source);

        try (Connection connection = getConnection()){
            //If the id of the source is 0 it has to be inserted, otherwise just update the source
            ResultSet resultSet;
            Statement statement = connection.createStatement();
            boolean updateSource;
            connection.setAutoCommit(false);
            if (source.getId() == 0) {
                String insertSource = String.format(INSERT_BASE_SOURCE, source.getTitle(), source.getYear(), source.getAuthor());
                statement.execute(insertSource);
                Statement getSourceId = connection.createStatement();
                resultSet = getSourceId.executeQuery(GET_SOURCE_ID);
                resultSet.next();
                source.setId(resultSet.getInt(1));
                updateSource = false;
            } else {
                String updateSourceCommand = String.format(UPDATE_BASE_SOURCE, source.getAuthor(), source.getYear(), source.getTitle(), source.getId());
                statement.executeUpdate(updateSourceCommand);
                updateSource = true;
            }

            //if the source has some extra attributes they have to be inserted/updated too
            if (isSourceExtension) {
                statement.execute(updateSourceExtraAttributes(source, updateSource));
            }
            updateQuotes(source, connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String updateSourceExtraAttributes(SourceInterface source, boolean updateSource) throws SQLException{
        List<ObjectTemplateInterface> template = SourceUtillities.getTemplate(source);
        StringBuilder sourceCommand = new StringBuilder();

        if (template != null) {
            //remove all standard attributes from the template, because we already inserted/updated them
            SourceUtillities.removeBasicAttributes(template);

            if (updateSource) {
                sourceCommand.append("UPDATE ").append(source.getClass().getSimpleName()).append(" SET ");
                for (ObjectTemplateInterface templateRow : template) {
                    sourceCommand.append(templateRow.getAttributeName()).append("='").append(templateRow.getAttributeValue()).append("',");
                }
                sourceCommand.deleteCharAt(sourceCommand.lastIndexOf(","));
            } else {
                sourceCommand.append("INSERT INTO ").append(source.getClass().getSimpleName()).append("(id,");
                StringBuilder sourceValues = new StringBuilder(")VALUES(").append(source.getId()).append(',');
                for (ObjectTemplateInterface templateRow : template) {
                    sourceCommand.append(templateRow.getAttributeName()).append(',');
                    sourceValues.append("'").append(templateRow.getAttributeValue()).append("',");
                }
                sourceValues.deleteCharAt(sourceValues.lastIndexOf(",")).append(')');
                sourceCommand.deleteCharAt(sourceCommand.lastIndexOf(","));
                sourceCommand.append(sourceValues);
            }
        }
        return sourceCommand.toString();
    }

    private static void updateQuotes(SourceInterface source, Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        List<QuoteInterface> databaseQuotes = new ArrayList<>();
        ResultSet resultSet;
        QuoteInterface quote;

        resultSet = statement.executeQuery("SELECT * FROM Quote WHERE sourceId = " + source.getId());
        while (resultSet.next()) {
            quote = QuoteFactory.createQuote();
            quote.setId(resultSet.getInt("id"));
            quote.setSourceId(resultSet.getInt("sourceId"));
            quote.setText(resultSet.getString("text"));
            databaseQuotes.add(quote);
        }
        boolean insertQuote;
        for (QuoteInterface sourceQuote : source.getQuoteList()) {
            insertQuote = true;
            for (ListIterator<QuoteInterface> iterator = databaseQuotes.listIterator(); iterator.hasNext();) {
                QuoteInterface databaseQuote = iterator.next();
                if (databaseQuote.getId() == sourceQuote.getId()) {
                    updateQuote(sourceQuote, connection);
                    iterator.remove();
                    insertQuote = false;
                }
            }
            if (insertQuote) {
                sourceQuote.setSourceId(source.getId());
                insertQuote(sourceQuote, connection);
            }
        }

        for (QuoteInterface databaseQuote : databaseQuotes) {
            deleteQuote(databaseQuote, connection);
        }
        //remove all quotes that are left in the databaseQuotes
    }

    private static void deleteQuote(QuoteInterface quote, Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM Quote WHERE id = " + quote.getId());
        updateTags(quote, connection);
    }

    private static void insertQuote(QuoteInterface quote, Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO Quote (sourceId, text) VALUES (" + quote.getSourceId() + ",'" + quote.getText()+ "')");
        ResultSet resultSet = statement.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Tags'");
        resultSet.next();
        quote.setId(resultSet.getInt("seq"));
        updateTags(quote, connection);
    }

    private static void updateQuote(QuoteInterface quote, Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE Quote SET text = '" + quote.getText() + "' WHERE id = " + quote.getId());
        updateTags(quote, connection);
    }

    private static void updateTags(QuoteInterface quote, Connection connection) throws SQLException{
        List<TagInterface> databaseTags = new ArrayList<>();
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        TagInterface tag;
        resultSet  = statement.executeQuery("SELECT Tags.tagId, Tags.name FROM Tags NATURAL JOIN QuoteTags WHERE QuoteTags.quoteId = " + quote.getId());
        while(resultSet.next()) {
            tag = new models.Tag();
            tag.setId(resultSet.getInt("tagId"));
            tag.setText(resultSet.getString("name"));
            databaseTags.add(tag);
        }

        boolean insertTag;
        for (TagInterface quoteTag : quote.getTagList()) {
            insertTag = true;
            for (ListIterator<TagInterface> iterator = databaseTags.listIterator(); iterator.hasNext();) {
                TagInterface databaseTag = iterator.next();
                if (databaseTag.getId() == quoteTag.getId()) {
                    updateTag(quoteTag, connection);
                    iterator.remove();
                    insertTag = false;
                }
            }
            if (insertTag) {
                insertTag(quoteTag, connection, quote);
            }
        }

        for (TagInterface databaseTag : databaseTags) {
            deleteTag(databaseTag, connection);
        }
    }

    private static void deleteTag(TagInterface tag, Connection connection) throws  SQLException{
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM QuoteTags WHERE tagId = " + tag.getId());
        statement.execute("DELETE FROM Tags WHERE tagId = " + tag.getId());
    }

    private static void insertTag(TagInterface tag, Connection connection, QuoteInterface quote) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet;
        //insert the new tag
        statement.execute("INSERT INTO Tags (name) VALUES ('" + tag.getText() + "')");
        //get the generated id from the database
        resultSet = statement.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Tags'");
        resultSet.next();
        //set the id
        tag.setId(resultSet.getInt("seq"));
        //insert the connection between quote and tag
        statement.execute("INSERT INTO QuoteTags VALUES (" + quote.getId() + "," + tag.getId() + ")");
    }

    private static void updateTag(TagInterface tag, Connection connection) throws SQLException{
        Statement statement = connection.createStatement();
        statement.executeUpdate("UPDATE Tags SET name = '" + tag.getText() + "' WHERE tagId = " + tag.getId());
    }

    private static Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();
        return con.getConnection();
    }
}