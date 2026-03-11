
package co.unicauca.microkernel.piedaazul.common.entity;

import java.time.LocalDateTime;

/**
 *
 * @author Sam
 * @brief Esta clase se usa para transferir los datos a traves de los pipelines
 *        no se usa para logica de negocio, solo para facilitar la generacion de 
 *        los reportes
 */
public class ApointmentDTO {
    private String schedulingDate;
    private String appointmenDate;
    private String observation;
    private String scheduler;
    private String patient;
    private String professional;
    private String cedPatient;

   
    
    //constructor sin parametros

    public ApointmentDTO() {
    }

    public String getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(String schedulingDate) {
        this.schedulingDate = schedulingDate;
    }

    public String getAppointmenDate() {
        return appointmenDate;
    }

    public void setAppointmenDate(String appointmenDate) {
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
    
    public String getCedPatient() {
        return cedPatient;
    }

    public void setCedPatient(String cedPatient) {
        this.cedPatient = cedPatient;
    }

    
}
