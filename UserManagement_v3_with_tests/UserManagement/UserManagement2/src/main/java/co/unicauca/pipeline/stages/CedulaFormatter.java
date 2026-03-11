
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
public class CedulaFormatter implements PipelineStage {

    @Override
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input) {
        List<ApointmentDTO> processedDTO = appointmentsDTO;
        for (int i=0;i<appointments.size();i++) {
            String cedula = String.valueOf(appointments.get(i).getCedPatient());
            if (cedula != null) {
                String formattedCedula = cedula.replaceAll("(\\d{1})(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3.$4");
                processedDTO.get(i).setCedPatient(formattedCedula); 
            }
        }
        return appointmentsDTO;
    }
    
}
