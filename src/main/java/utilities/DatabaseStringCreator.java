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
    private static final String PARENT_INTERFACE = "SourceInterface";

    public static Statement insertOrUpdateSource(SourceInterface source){

        List<ObjectTemplateInterface> sourceTemplates = SourceReflection.getTemplate(source);

        //if the source
        Class c = source.getClass();
        boolean isNoSourceInterfaceExtension = true;
        for (Class cl : c.getInterfaces()) {
            if (cl.getSimpleName().equals(PARENT_INTERFACE)) {
                isNoSourceInterfaceExtension = false;
            }
        }

        if (isNoSourceInterfaceExtension) {

        }
        return null;
    }
}
