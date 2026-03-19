package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;

public abstract class AppointmentBuilder {
    protected Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void createAppointment() {
        appointment = new Appointment();
    }

    public void setAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        this.appointment = appointment;
    }

    public abstract void buildPatientData();

    public abstract void buildProfessionalData();

    public abstract void buildSchedulerData();

    public abstract void buildSchedulingDate();

    public abstract void buildAppointmentDate();

    public abstract void buildObservationData();
}