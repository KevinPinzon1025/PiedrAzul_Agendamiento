
package co.unicauca.usermanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

/**
 *
 * @author Sam
 */
public class PatientServiceImpl implements IPatientService {

    private final IUserRepository repository;

    // Observadores del modelo (servicio) para mantener desacopladas la vista y la lógica.
    private final List<IPatientChangeListener> patientChangeListeners = new CopyOnWriteArrayList<>();
    
    //constructor
    public PatientServiceImpl(IUserRepository repository) {
        this.repository = repository;
    }
    
    //metodos de la interfaz
    @Override
    public boolean scheduleAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean register(User newUser) {
        boolean saved = repository.save(newUser);
        if (saved) {
            notifyPatientsChanged();
        }
        return saved;
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

    @Override
    public List<User> getAllPatients() {
        return repository.list();
    }

    @Override
    public void addPatientChangeListener(IPatientChangeListener listener) {
        if (listener == null) return;
        patientChangeListeners.add(listener);
    }

    @Override
    public void removePatientChangeListener(IPatientChangeListener listener) {
        if (listener == null) return;
        patientChangeListeners.remove(listener);
    }

    private void notifyPatientsChanged() {
        for (IPatientChangeListener listener : patientChangeListeners) {
            try {
                listener.onPatientsChanged();
            } catch (Exception ignored) {
                // Evita que un observador defectuoso rompa el flujo del modelo.
            }
        }
    }
    
}
