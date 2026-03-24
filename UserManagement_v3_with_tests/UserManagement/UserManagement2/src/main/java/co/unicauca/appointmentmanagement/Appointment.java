package co.unicauca.appointmentmanagement;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import java.time.LocalDateTime;

/**
 *
 * @author Sam, Kevin
 */
public class Appointment {
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmenDate;
    private String observation;
    private Scheduler scheduler;
    private Patient patient;
    private Professional professional;

    public Appointment() {
    }

    public Appointment(LocalDateTime schedulingDate, LocalDateTime appointmenDate, String observation,
        Scheduler scheduler, Patient patient, Professional professional) {
        this.schedulingDate = schedulingDate;
        this.appointmenDate = appointmenDate;
        this.observation = observation;
        this.scheduler = scheduler;
        this.patient = patient;
        this.professional = professional;
    }

    public LocalDateTime getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(LocalDateTime schedulingDate) {
        this.schedulingDate = schedulingDate;
    }

    public LocalDateTime getAppointmenDate() {
        return appointmenDate;
    }

    public void setAppointmenDate(LocalDateTime appointmenDate) {
        this.appointmenDate = appointmenDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "schedulingDate=" + schedulingDate +
                ", appointmenDate=" + appointmenDate +
                ", observation='" + observation + '\'' +
                ", scheduler=" + scheduler +
                ", patient=" + patient +
                ", professional=" + professional +
                '}';
    }
}