
package co.unicauca.usermanagement;

/**
 *
 * @author Sam
 */
public class Scheduler extends User {

    public Scheduler() {
    }

    public Scheduler(double idUser, String login, String passwordHash, String passwordSalt, boolean active, String firstName, String firstLastName) {
        super(idUser, login, passwordHash, passwordSalt, active, firstName, firstLastName);
    }
    
}
