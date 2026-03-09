
package co.unicauca.microkernel.piedraazul.common.interfaz;

import java.util.List;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;

 /*
 * @author Sam
 */
public interface IReportPlugin {
    void generateReport(List<AppointmentEntity> data, String fileName);
    
}
