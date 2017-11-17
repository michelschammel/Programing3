package utilities;

import dao.ConnectionKlasse;
import enums.SourceStandardAttributes;
import models.interfaces.ObjectTemplateInterface;
import models.interfaces.QuoteInterface;
import models.interfaces.SourceInterface;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Cedric on 14.11.2017.
 * creates all needed strings to add/update/delete a source
 */
public abstract class DatabaseStringCreator {
    private static final String SOURCE_INTERFACE = "SourceInterface";
    private static final String INSERT_BASE_SOURCE = "INSERT INTO Source(title, year, author) VALUES ('%s', '%s', '%s')";
    private static final String GET_SOURCE_ID = "SELECT seq FROM sqlite_sequence WHERE name = 'Source'";
    private static final String UPDATE_BASE_SOURCE = "UPDATE Source SET author = '%s', year = '%s', title = '%s' WHERE id = %d";

    public static void insertOrUpdateSource(SourceInterface source){
        //if the source
        Class sourceClass = source.getClass();
        boolean isSourceInterfaceExtension = true;
        for (Class sourceInterfaceClass : sourceClass.getInterfaces()) {
            if (sourceInterfaceClass.getSimpleName().equals(SOURCE_INTERFACE)) {
                isSourceInterfaceExtension = false;
            }
        }
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
            if (isSourceInterfaceExtension) {
                statement.execute(updateSourceExtraAttributes(source, updateSource));
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String updateSourceExtraAttributes(SourceInterface source, boolean updateSource) throws SQLException{
        List<ObjectTemplateInterface> template = SourceReflection.getTemplate(source);
        StringBuilder sourceCommand = new StringBuilder();

        if (template != null) {
            //remove all standard attributes from the template, because we already inserted/updated them
            for (SourceStandardAttributes attribute : SourceStandardAttributes.values()) {
                template.removeIf(templateRow -> templateRow.getAttributeName().equals(attribute.name()));
            }

            try {

                if (!updateSource) {
                    sourceCommand.append("INSERT INTO ").append(source.getClass().getSimpleName()).append("(id,");
                    StringBuilder sourceValues = new StringBuilder(")VALUES(").append(source.getId()).append(',');
                    for (ObjectTemplateInterface templateRow : template) {
                        sourceCommand.append(templateRow.getAttributeName()).append(',');
                        sourceValues.append("'").append(templateRow.getAttributeValue()).append("',");
                    }
                    sourceValues.deleteCharAt(sourceValues.lastIndexOf(",")).append(')');
                    sourceCommand.deleteCharAt(sourceCommand.lastIndexOf(","));
                    sourceCommand.append(sourceValues);
                } else {
                    sourceCommand.append("UPDATE").append(source.getClass().getSimpleName()).append(" SET ");
                    for (ObjectTemplateInterface templateRow : template) {
                        sourceCommand.append(templateRow.getAttributeName()).append("='").append(templateRow.getAttributeValue()).append("',");
                    }
                    sourceCommand.deleteCharAt(sourceCommand.lastIndexOf(","));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sourceCommand.toString();
    }

    private static void updateQuoteAndTags(SourceInterface source) {
        for (QuoteInterface quote : source.getQuoteList()) {

        }
    }

    private static Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();
        return con.getConnection();
    }
}
