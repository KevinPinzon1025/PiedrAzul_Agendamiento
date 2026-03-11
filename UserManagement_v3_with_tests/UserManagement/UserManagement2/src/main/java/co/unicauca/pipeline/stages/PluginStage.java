
package co.unicauca.pipeline.stages;

import co.unicauca.microkernel.core.ReportPluginManager;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import co.unicauca.pipeline.interfaz.PipelineStage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 */
public class PluginStage implements PipelineStage {

    @Override
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input) {
        List<ApointmentDTO> processedDTO = appointmentsDTO;
        ReportPluginManager manager = ReportPluginManager.getInstance();
        IReportPlugin plugin = manager.getReportPlugin(input.getFormat());

        if (plugin == null) {
            try {
                throw new Exception("No hay un plugin disponible para el formato indicado: " + input.getFormat());
            } catch (Exception ex) {
                Logger.getLogger(PluginStage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        plugin.generateReport(processedDTO, input.getFileName());
        return appointmentsDTO;
    }
    
}
