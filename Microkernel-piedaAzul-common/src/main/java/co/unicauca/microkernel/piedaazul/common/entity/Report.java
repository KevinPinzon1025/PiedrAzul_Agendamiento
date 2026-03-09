
package co.unicauca.microkernel.piedaazul.common.entity;

import java.util.List;

/**
 *
 * @author Sam
 */
public class Report {
    private List<AppointmentEntity> appointments;
    private String format;
    String fileName;

    public Report(List<AppointmentEntity> appointments, String format, String fileName) {
        this.appointments = appointments;
        this.format = format;
        this.fileName = fileName;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    
}
