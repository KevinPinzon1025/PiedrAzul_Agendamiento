package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;

public class AppointmentDirector {
    private AppointmentBuilder appointmentBuilder;

    public void setAppointmentBuilder(AppointmentBuilder aB){
        appointmentBuilder = aB;
    }

    public Appointment getAppointment(){
        return appointmentBuilder.getAppointment();
    }

    public void buildManualAppointment(){
        appointmentBuilder.createAppointment();
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }

    public void buildSelfServiceAppointment(){
        appointmentBuilder.createAppointment();
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }

    public void buildRescheduleAppointment(Appointment prevAppointment){
        appointmentBuilder.setAppointment(prevAppointment);
        appointmentBuilder.buildSchedulerData();
        appointmentBuilder.buildPatientData();
        appointmentBuilder.buildProfessionalData();
        appointmentBuilder.buildSchedulingDate();
        appointmentBuilder.buildAppointmentDate();
        appointmentBuilder.buildObservationData();
    }
}
