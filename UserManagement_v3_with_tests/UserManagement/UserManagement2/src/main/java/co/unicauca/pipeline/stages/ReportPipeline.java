
package co.unicauca.pipeline.stages;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import co.unicauca.pipeline.interfaz.PipelineStage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class ReportPipeline {
    private List<PipelineStage> stages;

    public ReportPipeline() {
        stages = new ArrayList<>();
    }

    // Añadir una etapa al pipeline
    public void addStage(PipelineStage stage) {
        stages.add(stage);
    }

    // Ejecutar el pipeline completo
    public void execute(Report input) {
        List<ApointmentDTO> result = transformDTO(input.getAppointments());
        for (PipelineStage stage : stages) {
            result = stage.process(result, input.getAppointments(), input);
        }
    }
    
    //preprocesamiento
    public static List<ApointmentDTO> transformDTO(List<AppointmentEntity> appointments){
        List<ApointmentDTO> dtos = new ArrayList<>();
        for(int i=0;i<appointments.size();i++){
            ApointmentDTO dto = new ApointmentDTO();
            dtos.add(dto);
        }
        return dtos;
    }
}
