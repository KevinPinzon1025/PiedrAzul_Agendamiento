package co.unicauca.usermanagement.acces;

import co.unicauca.appointmentmanagement.Appointment;

public class AppointmentRepositorySQL implements IAppointmentRepository {

    private String conn;

    @Override
    public boolean saveAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Appointment findById(double id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}