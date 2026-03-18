package co.unicauca.appointmentmanagement.service;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import java.time.LocalDateTime;

public class SelfServiceAppointmentBuilder extends AppointmentBuilder {

    private final Patient patient;
    private final Professional professional;
    private final LocalDateTime schedulingDate;
    private final LocalDateTime appointmentDate;
    private final String observation;

    public SelfServiceAppointmentBuilder(Patient patient, Professional professional,
            LocalDateTime schedulingDate, LocalDateTime appointmentDate,
            String observation) {
        this.patient = patient;
        this.professional = professional;
        this.schedulingDate = schedulingDate;
        this.appointmentDate = appointmentDate;
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
        appointment.setScheduler(null);
    }

    @Override
    public void buildSchedulingDate() {
        if (schedulingDate == null) {
            throw new IllegalArgumentException("La fecha de agendamiento no puede ser nula");
        }
        appointment.setSchedulingDate(schedulingDate);
    }

    @Override
    public void buildAppointmentDate() {
        if (appointmentDate == null) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser nula");
        }
        if (schedulingDate != null && appointmentDate.isBefore(schedulingDate)) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser anterior a la fecha de agendamiento");
        }
        appointment.setAppointmenDate(appointmentDate);
    }

    @Override
    public void buildObservationData() {
        appointment.setObservation(observation == null ? "" : observation);
    }
}