
package co.unicauca.microkernel.piedraazul.common.interfaz;

import co.unicauca.microkernel.piedaazul.common.entity.ApointmentDTO;
import java.util.List;


 /*
 * @author Sam
 */
public interface IReportPlugin {
    void generateReport(List<ApointmentDTO> data, String fileName);
    
}
