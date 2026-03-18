package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceImpl implements IAppointmentService {

    @Override
    public List<AppointmentEntity> getAll() {

        List<AppointmentEntity> appointments = new ArrayList<>();

        // Datos de prueba (mock)
        AppointmentEntity appointmentOne = new AppointmentEntity(
                LocalDateTime.of(2026, 2, 14, 8, 30),
                LocalDateTime.of(2026, 4, 1, 10, 45),
                "Dolor de cabeza",
                "Sch Juan Perez",
                "pat. Alan Brito",
                "Dr. Jose Ignacio",
                1059237786
        );

        AppointmentEntity appointmentTwo = new AppointmentEntity(
                LocalDateTime.of(2026, 1, 1, 9, 0),
                LocalDateTime.of(2026, 4, 2, 11, 35),
                "Dolor de estómago",
                "Sch Juan Perez",
                "pat. Pedro Medina",
                "Dr. Ibis Gonzales",
                105896324
        );

        AppointmentEntity appointmentThree = new AppointmentEntity(
                LocalDateTime.of(2026, 2, 23, 10, 20),
                LocalDateTime.of(2026, 4, 3, 9, 15),
                "Fractura de brazo",
                "Sch Juan Perez",
                "pat. Eduardo Santos",
                "Dr. Clara Ines",
                1059237269
        );

        appointments.add(appointmentOne);
        appointments.add(appointmentTwo);
        appointments.add(appointmentThree);

        return appointments;
    }
}