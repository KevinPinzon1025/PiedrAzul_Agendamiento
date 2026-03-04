
package co.unicauca.usermanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import java.util.LinkedList;

/**
 *
 * @author Sam
 */
public interface ISchedulerService extends IUserService {
    public LinkedList<Appointment> checkAppointment();
}
