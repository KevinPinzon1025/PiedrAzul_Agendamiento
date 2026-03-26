
package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.User;
import java.util.List;

/**
 *
 * @author Sam
 */
public interface IProfessionalService extends IUserService {
    List<User> getAllProfessionals();
}
