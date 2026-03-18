package co.unicauca.appointmentmanagement.service;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import java.time.LocalDateTime;

public class RescheduleAppointmentBuilder extends AppointmentBuilder {

    private final Patient patient;
    private final Professional professional;
    private final Scheduler scheduler;
    private final LocalDateTime schedulingDate;
    private final LocalDateTime newAppointmentDate;
    private final String observation;

    public RescheduleAppointmentBuilder(Patient patient, Professional professional,
            Scheduler scheduler, LocalDateTime schedulingDate,
            LocalDateTime newAppointmentDate, String observation) {
        this.patient = patient;
        this.professional = professional;
        this.scheduler = scheduler;
        this.schedulingDate = schedulingDate;
        this.newAppointmentDate = newAppointmentDate;
        this.observation = observation;
    }

    @Override
    public void buildPatientData() {
        if (patient == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        appointment.setPatient(patient);
    }

    @Override
    public void buildProfessionalData() {
        if (professional == null) {
            throw new IllegalArgumentException("El profesional no puede ser nulo");
        }
        appointment.setProfessional(professional);
    }

    @Override
    public void buildSchedulerData() {
        if (scheduler == null) {
            throw new IllegalArgumentException("El reagendamiento requiere un scheduler");
        }
        appointment.setScheduler(scheduler);
    }

    @Override
    public void buildSchedulingDate() {
        if (schedulingDate == null) {
            throw new IllegalArgumentException("La fecha de reagendamiento no puede ser nula");
        }
        appointment.setSchedulingDate(schedulingDate);
    }

    @Override
    public void buildAppointmentDate() {
        if (newAppointmentDate == null) {
            throw new IllegalArgumentException("La nueva fecha de la cita no puede ser nula");
        }
        if (schedulingDate != null && newAppointmentDate.isBefore(schedulingDate)) {
            throw new IllegalArgumentException("La nueva fecha de la cita no puede ser anterior a la fecha de reagendamiento");
        }
        appointment.setAppointmenDate(newAppointmentDate);
    }

    @Override
    public void buildObservationData() {
        appointment.setObservation(observation == null ? "Cita reagendada" : observation);
    }
}