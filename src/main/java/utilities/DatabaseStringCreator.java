package utilities;

import models.interfaces.ObjectTemplateInterface;
import models.interfaces.SourceInterface;

import java.sql.Statement;
import java.util.List;

/**
 * Created by Cedric on 14.11.2017.
 * creates all needed strings to add/update/delete a sourde
 */
public abstract class DatabaseStringCreator {
    private static final String SOURCE_INTERFACE = "SourceInterface";

    public static Statement insertOrUpdateSource(SourceInterface source){

        List<ObjectTemplateInterface> sourceTemplates = SourceReflection.getTemplate(source);

        //if the source
        Class sourceClass = source.getClass();
        boolean isSourceInterfaceExtension = true;
        for (Class sourceInterfaceClass : sourceClass.getInterfaces()) {
            if (sourceInterfaceClass.getSimpleName().equals(SOURCE_INTERFACE)) {
                isSourceInterfaceExtension = false;
            }
        }
        try {
            //If the id of the source is 0 it has to be inserted, otherwise just update the source
            if (source.getId() != 0) {
                String insertSource = String.format("INSERT INTO Source(title, year, author) VALUES (%s, %s, %s)", source.getTitle(), source.getYear(), source.getAuthor());

            }


            if (isSourceInterfaceExtension) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
