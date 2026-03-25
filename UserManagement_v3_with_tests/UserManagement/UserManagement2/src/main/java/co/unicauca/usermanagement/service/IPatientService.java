
package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.User;
import java.util.List;

/**
 *
 * @author Sam
 */
public interface IPatientService extends IUserService {
    
    List<User> getAllPatients();
}
