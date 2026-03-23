package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.time.LocalDate;
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
    
    @Override
    public List<AppointmentEntity> findByProfessionalAndDate(String professional, LocalDate date) {

        List<AppointmentEntity> result = new ArrayList<>();

        for (AppointmentEntity ap : getAll()) {

            boolean sameProfessional = ap.getProfessional().equalsIgnoreCase(professional);

            boolean sameDate = ap.getAppointmenDate().toLocalDate().equals(date);

            if (sameProfessional && sameDate) {
                result.add(ap);
            }
        }

        return result;
    }
    
    @Override
    public List<String> getAllProfessionals() {

        List<String> professionals = new ArrayList<>();

        for (AppointmentEntity ap : getAll()) {
            if (!professionals.contains(ap.getProfessional())) {
                professionals.add(ap.getProfessional());
            }
        }

        return professionals;
    }
}