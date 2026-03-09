
package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class AppointmentServiceImpl implements IAppointmentService {

    @Override //PARA EL MICROKERNEL usar el AppointmentEntity que es diferentente a Appointment
    public List<AppointmentEntity> getAll() {
        List<AppointmentEntity> appointments = new ArrayList<>();

        /*
         * Para este ejemplo, se crearán objetos de prueba directamente aquí.
         * */
        AppointmentEntity appointmentOne = new AppointmentEntity(LocalDateTime.of(2026,02,14,8,30), LocalDateTime.of(2026,04,01,10,45), "Dolor de cabeza", "Sch Juan Perez", "pat. Alan Brito", "Dr. Jose Ignacio");
        AppointmentEntity appointmentTwo = new AppointmentEntity(LocalDateTime.of(2026,01,1,9,00), LocalDateTime.of(2026,04,02,11,35), "Dolor de estomago", "Sch Juan Perez", "pat. Pedro Medina", "Dr. Ibis Gonzales");
        AppointmentEntity appointmentThree = new AppointmentEntity(LocalDateTime.of(2026,02,23,10,20), LocalDateTime.of(2026,04,03,9,15), "Fractura de brazo", "Sch Juan Perez", "pat. Eduardo Santos", "Dr. Clara Ines");


        appointments.add(appointmentOne);
        appointments.add(appointmentTwo);
        appointments.add(appointmentThree);

        return appointments;
    }
    
}
