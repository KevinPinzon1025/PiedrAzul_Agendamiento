
package co.unicauca.usermanagement;

/**
 *
 * @author Sam
 */
public class Administrator extends User {

     //constructor sin parametros
    public Administrator() {
    }
    
    //constructor con parametros

    public Administrator(double idUser, String login, String passwordHash, String passwordSalt, boolean active, String firstName, String firstLastName) {
        super(idUser, login, passwordHash, passwordSalt, active, firstName, firstLastName);
    }
    
   
    
}
