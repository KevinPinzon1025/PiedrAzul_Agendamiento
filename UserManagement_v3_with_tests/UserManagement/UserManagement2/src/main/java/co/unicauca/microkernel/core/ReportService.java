
package co.unicauca.microkernel.core;


import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedraazul.common.interfaz.IReportPlugin;
import co.unicauca.pipeline.stages.CedulaFormatter;
import co.unicauca.pipeline.stages.PluginStage;
import co.unicauca.pipeline.stages.ReportPipeline;
import co.unicauca.pipeline.stages.TransformCamelCaseDoctorStage;
import co.unicauca.pipeline.stages.TransformCamelCaseNameStage;
import co.unicauca.pipeline.stages.TransformDateStage;
import co.unicauca.pipeline.stages.ValidationStage;



/**
 *
 * @author Sam
 */
public class ReportService {
    public void generarReporte(Report report) throws Exception{
        //instanciamos un reportPipeLine
        ReportPipeline pipeline = new ReportPipeline();
        pipeline.addStage(new ValidationStage());
        pipeline.addStage(new CedulaFormatter());
        pipeline.addStage(new TransformCamelCaseNameStage());
        pipeline.addStage(new TransformCamelCaseDoctorStage());
        pipeline.addStage(new TransformDateStage());
        pipeline.addStage(new PluginStage());
        
        //de aqui en adelante iria al stage
        pipeline.execute(report);
        
    }
    
}
