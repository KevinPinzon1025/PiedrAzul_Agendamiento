
package co.unicauca.usermanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;

/**
 *
 * @author Sam
 */
public class ProfessionalServiceImpl implements IProfessionalService {

    private final IUserRepository repository;
    
    public ProfessionalServiceImpl (IUserRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean scheduleAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean register(User newUser) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User login(String login, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(User newUser) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(double idUser) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
