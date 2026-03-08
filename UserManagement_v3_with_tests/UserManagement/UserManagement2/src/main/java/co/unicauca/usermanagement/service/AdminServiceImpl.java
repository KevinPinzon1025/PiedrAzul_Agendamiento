//revisar el constructor de admin, esta viene siendo una clase legada

package co.unicauca.usermanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.usermanagement.Administrator;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;

public class AdminServiceImpl implements IAdminService{
    private final IUserRepository repository;

    public AdminServiceImpl(IUserRepository repository) {
        this.repository = repository;
    }

   /* public boolean register(String login, String fullName, String role, boolean active, String passwordPlain) {
        
    }*/

    @Override
    public User login(String login, String password) {
        User user = repository.findByLogin(login);
        if (user == null) return null;
        if (!user.isActive()) return null;

        boolean ok = PasswordHasher.verify(password, user.getPasswordSalt(), user.getPasswordHash());
        return ok ? user : null;
    }

    @Override
    public boolean configDates() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean configTimeWin(long timeWin, Professional professional) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean scheduleAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean register(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) return false;
        if (user.getFirstName() == null || user.getFirstName().isBlank()) return false;
        //if (role == null || role.isBlank()) return false;

        if (!PasswordPolicy.isValid(user.getPasswordHash())) return false; //aqui el passwordhash aun esta en texto plano

        if (repository.findByLogin(user.getLogin()) != null) return false;

        String salt = PasswordHasher.newSaltBase64();
        String hash = PasswordHasher.hashBase64(user.getPasswordHash().toCharArray(), salt);

        User admin = new Administrator(user.getIdUser(), user.getLogin(), hash, salt, user.isActive(),user.getFirstName(), user.getFirstLastName());
        return repository.save(admin);
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

