
package co.unicauca.pipeline.stages;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.microkernel.piedaazul.common.entity.Report;
import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import co.unicauca.pipeline.interfaz.PipelineStage;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Sam
 */
public class TransformDateStage implements PipelineStage {

    @Override
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input) {
        List<ApointmentDTO> processedDTO = appointmentsDTO;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        for (int i=0;i<appointments.size();i++) {
            if (appointments.get(i).getSchedulingDate() != null) {
                String formattedSchedulingDate = formatter.format(appointments.get(i).getSchedulingDate());
                processedDTO.get(i).setSchedulingDate(formattedSchedulingDate); 
            }
            if (appointments.get(i).getAppointmenDate() != null) {
                String formattedAppointmentDate = formatter.format(appointments.get(i).getAppointmenDate());
                // Convertimos LocalDateTime a String formateada
                processedDTO.get(i).setAppointmenDate(formattedAppointmentDate); 
            }
        
        }
        return processedDTO;
    }
}
