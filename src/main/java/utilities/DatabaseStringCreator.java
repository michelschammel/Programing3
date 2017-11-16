package utilities;

import dao.ConnectionKlasse;
import enums.SourceStandardAttributes;
import models.interfaces.ObjectTemplateInterface;
import models.interfaces.SourceInterface;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Cedric on 14.11.2017.
 * creates all needed strings to add/update/delete a sourde
 */
public abstract class DatabaseStringCreator {
    private static final String SOURCE_INTERFACE = "SourceInterface";

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
            if (source.getId() == 0) {
                String insertSource = String.format("INSERT INTO Source(title, year, author) VALUES ('%s', '%s', '%s')", source.getTitle(), source.getYear(), source.getAuthor());
                statement.execute(insertSource);
                Statement getSourceId = connection.createStatement();
                resultSet = getSourceId.executeQuery("SELECT seq FROM sqlite_sequence WHERE name = 'Source'");
                resultSet.next();
                source.setId(resultSet.getInt(1));
            } else {
                String updateSource = String.format("UPDATE Source SET author = '%s', year = '%s', title = '%s' WHERE id = %d", source.getAuthor(), source.getYear(), source.getTitle(), source.getId());
                statement.executeUpdate(updateSource);
            }

            //if the source has some extra attributes they have to be inserted/updated too
            if (isSourceInterfaceExtension) {
                updateSourceExtraAttributes(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateSourceExtraAttributes(SourceInterface source) throws SQLException{
        List<ObjectTemplateInterface> template = SourceReflection.getTemplate(source);
        boolean insertSource = (source.getId() == 0);

        if (template != null) {
            //remove all standard attributes from the template, because we already inserted/updated them
            for (SourceStandardAttributes attribute : SourceStandardAttributes.values()) {
                template.removeIf(templateRow -> templateRow.getAttributeName().equals(attribute.name()));
            }

            try {

                if (!insertSource) {
                    StringBuilder insertSourceCommand = new StringBuilder("INSERT INTO" + source.getClass().getSimpleName() + "(");
                    for (ObjectTemplateInterface templateRow : template) {
                        insertSourceCommand.append(templateRow.getAttributeName()).append(',');
                    }

                    insertSourceCommand.deleteCharAt(insertSourceCommand.lastIndexOf(","));
                    insertSourceCommand.append(")VALUES(");
                } else {
                    StringBuilder updateSourceCommand = new StringBuilder("UPDATE" + source.getClass().getSimpleName() + "SET");
                }

                for (ObjectTemplateInterface templateRow : template) {
                    String getterMethodName = "get" + Character.toUpperCase(templateRow.getAttributeName().charAt(0)) + templateRow.getAttributeName().substring(1);

                    Method method = source.getClass().getDeclaredMethod(getterMethodName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection() {
        ConnectionKlasse con = new ConnectionKlasse();
        return con.getConnection();
    }
}
