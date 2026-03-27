package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import java.time.LocalDate;
import java.util.List;

public interface IAppointmentService {
    List<Appointment> getAll();
    List<Appointment> findByProfessionalAndDate(String professional, LocalDate date);
    List<String> getAllProfessionals();
    
    boolean isDateAvailable(LocalDate date);

    boolean scheduleAppointment(Appointment appointment);

    default void addAppointmentChangeListener(IAppointmentChangeListener listener) {
        // default no-op (permite no romper implementaciones existentes)
    }

    default void removeAppointmentChangeListener(IAppointmentChangeListener listener) {
        // default no-op (permite no romper implementaciones existentes)
    }
}