package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;

abstract class AppointmentBuilder {
    protected Appointment appointment;

    public Appointment getAppointment(){return appointment;}

    public void createAppointment(){ appointment = new Appointment();;}

    //este no esta en el codigo de ejemplo, pero es para el reagendamiento, para no instanciar una cita desde cero porque tecnicamente la cita ya exisitia, solo se le va a cambiar la fecha
    public void setAppointment(Appointment appointment) {this.appointment = appointment;}

    public abstract void buildPatientData();

    public abstract void buildProfessionalData();

    public abstract void buildSchedulerData();

    public abstract void buildSchedulingDate();

    public abstract void buildAppointmentDate();

    public abstract  void buildObservationData();
}
