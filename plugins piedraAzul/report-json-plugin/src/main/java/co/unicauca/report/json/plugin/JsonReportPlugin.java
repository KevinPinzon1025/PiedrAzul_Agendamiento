
package co.unicauca.report.json.plugin;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
/**
 *
 * @author Sam
 */
public class JsonReportPlugin implements IReportPlugin {

    @Override //un cambio en lo que dice la guia para que se genere un archivo directamente
    public void generateReport(List<AppointmentEntity> data, String fileName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Crea el archivo JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), data);
            System.out.println("Archivo JSON creado: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
