package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;

public class AppointmentDirector {
    private AppointmentBuilder appointmentBuilder;

    public void setAppointmentBuilder(AppointmentBuilder aB) {
        if (aB == null) {
            throw new IllegalArgumentException("El builder no puede ser nulo");
        }
        appointmentBuilder = aB;
    }

    public Appointment getAppointment() {
        if (appointmentBuilder == null) {
            throw new IllegalStateException("No se ha configurado un AppointmentBuilder");
        }
        return appointmentBuilder.getAppointment();
    }

    public void buildManualAppointment() {
        if (appointmentBuilder == null) {
            throw new IllegalStateException("No se ha configurado un AppointmentBuilder");
        }

        appointmentBuilder.createAppointment();
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }

    public void buildSelfServiceAppointment() {
        if (appointmentBuilder == null) {
            throw new IllegalStateException("No se ha configurado un AppointmentBuilder");
        }

        appointmentBuilder.createAppointment();
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }

    public void buildRescheduleAppointment(Appointment prevAppointment) {
        if (appointmentBuilder == null) {
            throw new IllegalStateException("No se ha configurado un AppointmentBuilder");
        }
        if (prevAppointment == null) {
            throw new IllegalArgumentException("La cita previa no puede ser nula");
        }

        appointmentBuilder.setAppointment(prevAppointment);
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }
}