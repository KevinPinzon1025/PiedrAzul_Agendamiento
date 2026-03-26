
package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.User;
import java.util.List;

/**
 *
 * @author Sam
 */
public interface IPatientService extends IUserService {
    
    List<User> getAllPatients();

    default void addPatientChangeListener(IPatientChangeListener listener) {
        // default no-op (permite no romper implementaciones existentes)
    }

    default void removePatientChangeListener(IPatientChangeListener listener) {
        // default no-op (permite no romper implementaciones existentes)
    }
}
