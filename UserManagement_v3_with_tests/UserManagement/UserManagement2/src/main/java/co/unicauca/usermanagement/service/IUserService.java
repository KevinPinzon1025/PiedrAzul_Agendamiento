
package co.unicauca.usermanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.usermanagement.User;

/**
 *
 * @author Sam
 */
public interface IUserService {
    public boolean scheduleAppointment(Appointment newAppointment);
    public boolean register(User user);
    public User login(String login, String password);
    public boolean update(User newUser);
    public boolean delete(double idUser);
}
