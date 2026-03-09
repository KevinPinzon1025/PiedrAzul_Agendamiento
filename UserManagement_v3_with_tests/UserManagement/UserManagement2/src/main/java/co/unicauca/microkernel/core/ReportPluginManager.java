
package co.unicauca.microkernel.core;

import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public class ReportPluginManager {
         //atributos
    private static final String FILE_NAME = "plugin.properties";
    private static ReportPluginManager instance;
 
    private final Properties pluginProperties;

    //constructor
    private ReportPluginManager() {
        pluginProperties = new Properties();
    }

    //metodos
    public static ReportPluginManager getInstance() {
        return instance;
    }

    public static void init(String basePath) throws Exception {

        instance = new ReportPluginManager();
        instance.loadProperties(basePath);
        if (instance.pluginProperties.isEmpty()) {
            throw new Exception("Could not initialize plugins");
        }

    }

    public IReportPlugin getReportPlugin(String formatCode) {

        //Verificar si existe una entrada en el archivo para el formato indicado.
        String propertyName = "report." + formatCode.toLowerCase();
        if (!pluginProperties.containsKey(propertyName)) {
            return null;
        }

        IReportPlugin plugin = null;
        //Obtener el nombre de la clase del plugin.
        String pluginClassName = pluginProperties.getProperty(propertyName);

        try {

            //Obtener una referencia al tipo de la clase del plugin.
            Class<?> pluginClass = Class.forName(pluginClassName);
            if (pluginClass != null) {

                //Crear un nuevo objeto del plugin.
                Object pluginObject = pluginClass.getDeclaredConstructor().newInstance();

                if (pluginObject instanceof IReportPlugin) {
                    plugin = (IReportPlugin) pluginObject;
                }
            }

        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            Logger.getLogger("ReportPluginManager").log(Level.SEVERE, "Error al ejecutar la aplicación", ex);
        }

        return plugin;

    }

    private void loadProperties(String basePath) {

        String filePath = basePath + FILE_NAME;
        try (FileInputStream stream = new FileInputStream(filePath)) {

            pluginProperties.load(stream);

        } catch (IOException ex) {
            Logger.getLogger("ReportPluginManager").log(Level.SEVERE, "Error al ejecutar la aplicación", ex);
        }

    }
}



