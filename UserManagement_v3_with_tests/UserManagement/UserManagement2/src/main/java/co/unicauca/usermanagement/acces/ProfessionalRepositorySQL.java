
package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.User;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Sam
 */
public class ProfessionalRepositorySQL implements IUserRepository {

    private Connection conn;
    
    public ProfessionalRepositorySQL() {
        connect();
        createTable();
    }
    
    private void connect(){
        
    }
    
    private void createTable(){
        
    }
    @Override
    public boolean save(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User findByLogin(String login) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<User> list() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
