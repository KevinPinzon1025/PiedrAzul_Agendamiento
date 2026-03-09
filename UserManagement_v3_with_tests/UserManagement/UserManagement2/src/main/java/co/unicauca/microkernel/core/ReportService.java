
package co.unicauca.microkernel.core;


import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;



/**
 *
 * @author Sam
 */
public class ReportService {
    public void generarReporte(Report report) throws Exception{
        
        ReportPluginManager manager = ReportPluginManager.getInstance();
        IReportPlugin plugin = manager.getReportPlugin(report.getFormat());

        if (plugin == null) {
            throw new Exception("No hay un plugin disponible para el país indicado: " + report.getFormat());
        }
        
        plugin.generateReport(report.getAppointments(), report.getFileName());
        
    }
    
}
