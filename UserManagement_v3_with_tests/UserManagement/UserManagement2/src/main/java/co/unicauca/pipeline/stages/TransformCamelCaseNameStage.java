
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
public class TransformCamelCaseNameStage implements PipelineStage {

    @Override
    public List<ApointmentDTO> process(List<ApointmentDTO> appointmentsDTO, List<AppointmentEntity> appointments, Report input) {
        List<ApointmentDTO> processedDTO = appointmentsDTO;
        for (int i=0;i<appointments.size();i++) {
            String patientName = appointments.get(i).getPatient();
            if (patientName != null) {
                String[] words = patientName.split(" ");
                StringBuilder camelCaseName = new StringBuilder();
                for (String word : words) {
                    camelCaseName.append(word.substring(0, 1).toUpperCase())
                                 .append(word.substring(1).toLowerCase());
                }
                processedDTO.get(i).setPatient(camelCaseName.toString()); 
            }
        }
        return processedDTO;
    }
    
}
