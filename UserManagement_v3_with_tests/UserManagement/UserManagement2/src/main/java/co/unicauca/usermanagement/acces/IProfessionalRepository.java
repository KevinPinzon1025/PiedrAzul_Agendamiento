
package co.unicauca.usermanagement.acces;

import java.util.List;

/**
 *
 * @author Sam
 */
public interface IProfessionalRepository extends IUserRepository {
    public List<String> getActiveProfessionalNames();
    
}
