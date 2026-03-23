package co.unicauca.usermanagement.acces;

import co.unicauca.appointmentmanagement.Appointment;

public interface IAppointmentRepository {
    public boolean saveAppointment(Appointment newAppointment);
    public Appointment findById(double id);
    public boolean updateAppointment(Appointment newAppointment);
}