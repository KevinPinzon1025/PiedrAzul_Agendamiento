
package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.Professional;

/**
 *
 * @author Sam
 */
public interface IAdminService extends IUserService {
    public boolean configDates();
    public boolean configTimeWin(long timeWin, Professional professional);
}
