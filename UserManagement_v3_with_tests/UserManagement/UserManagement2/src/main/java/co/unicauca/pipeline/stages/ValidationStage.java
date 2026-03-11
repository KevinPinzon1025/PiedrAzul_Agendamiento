
package co.unicauca.pipeline.stages;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import co.unicauca.pipeline.interfaz.PipelineStage;
import java.util.List;

/**
 *
 * @author Sam
 */
public class ValidationStage implements PipelineStage {

    @Override
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input) {
       List<ApointmentDTO> processedDTO = appointmentsDTO;
       if (appointments == null || appointments.isEmpty()) {
            throw new IllegalArgumentException("La lista de citas está vacía");
        }
       return processedDTO;
    }
    
}
