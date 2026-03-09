
package co.unicauca.microkernel.piedaazul.common.entity;

import java.time.LocalDateTime;

/**
 *
 * @author Sam
 */
public class AppointmentEntity {
    private LocalDateTime schedulingDate;
    private LocalDateTime appointmenDate;
    private String observation;
    private String scheduler;
    private String patient;
    private String professional;
    
    //constructor sin parametros

    public AppointmentEntity() {
    }
    
    //constructor con parametros

    public AppointmentEntity(LocalDateTime schedulingDate, LocalDateTime appointmenDate, String observation, String scheduler, String patient, String professional) {
        this.schedulingDate = schedulingDate;
        this.appointmenDate = appointmenDate;
        this.observation = observation;
        this.scheduler = scheduler;
        this.patient = patient;
        this.professional = professional;
    }
    
    //getters & setters

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

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }
    
}
