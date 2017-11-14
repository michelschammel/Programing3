package utilities;

import models.interfaces.SourceInterface;
import models.interfaces.SourceTemplateInterface;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Cedric on 14.11.2017.
 * creates all needed strings to add/update/delete a sourde
 */
public abstract class DatabaseStringCreator {


    public static Statement addSource(SourceInterface source, Statement statement) throws SQLException{
        String addFundamentalSourceAttributes = String.format("INSERT INTO Source (title, author, year) VALUES('%s''%s''%s');", source.getTitle(), source.getAuthor(), source.getYear());
        statement.addBatch(addFundamentalSourceAttributes);

        List<SourceTemplateInterface> sourceTemplates = SourceReflection.getTemplate(source);
        return null;
    }
}
