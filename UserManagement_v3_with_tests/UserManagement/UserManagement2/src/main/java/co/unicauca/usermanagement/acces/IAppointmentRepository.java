package co.unicauca.usermanagement.acces;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.time.LocalDate;
import java.util.List;

public interface IAppointmentRepository {
    boolean saveAppointment(Appointment newAppointment);
    Appointment findById(double id);
    boolean updateAppointment(Appointment newAppointment);

    void initializeDatabase();
    List<AppointmentEntity> getAll();
    List<AppointmentEntity> findByProfessionalAndDate(String professional, LocalDate date);
    List<String> getAllProfessionals();
}