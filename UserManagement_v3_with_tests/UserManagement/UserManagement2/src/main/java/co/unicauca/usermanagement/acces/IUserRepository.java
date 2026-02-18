package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.User;
import java.util.List;


public interface IUserRepository {

    boolean save(User user);

    User findByLogin(String login);

    List<User> list();
}
