
package co.unicauca.pipeline.interfaz;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import java.util.List;

/**
 *
 * @author Sam
 */
public interface PipelineStage {
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input);
}
