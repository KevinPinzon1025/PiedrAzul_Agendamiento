package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.Administrator;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    static class InMemoryUserRepository implements IUserRepository {
        private final Map<String, User> data = new HashMap<>();

        @Override
        public User findByLogin(String login) {
            if (login == null) return null;
            return data.get(login.trim());
}

        @Override
        public boolean save(User user) {
            data.put(user.getLogin().trim(), user);
            return true;
}

        @Override
        public List<User> list() {
            return new java.util.ArrayList<>(data.values());
        }

    }

    @Test
    void shouldNotAllowDuplicateLogin() {
        IUserRepository repo = new InMemoryUserRepository();
        AdminServiceImpl service = new AdminServiceImpl(repo);

        double idAdmin = 1001;
        String login = "kevin";
        String firstName = "Kevin";
        String firstLastName = "Santiago";
        boolean active = true;
        String password = "Abc123!";
        
        Administrator admin1 = new Administrator(idAdmin, login, password, password, active, firstName, firstLastName);
        Administrator admin2 = new Administrator(1002, login, password, password, active, firstName, firstLastName);
        
        boolean first = service.register(admin1);
        assertTrue(first);

        boolean second = service.register(admin2);
        assertFalse(second);
    }
    
    @Test
    void shouldTreatTrimmedLoginAsSameUser() {
        IUserRepository repo = new InMemoryUserRepository();
        AdminServiceImpl service = new AdminServiceImpl(repo);
        
        double idAdmin = 1001;
        String login = "kevin";
        String firstName = "Kevin";
        String firstLastName = "Santiago";
        boolean active = true;
        String password = "Abc123!";
        
        Administrator admin1 = new Administrator(idAdmin, login, password, password, active, firstName, firstLastName);
        Administrator admin2 = admin1;
        admin2.setLogin("   kevin   ");
        boolean first = service.register(admin1);
        assertTrue(first);

        // mismo login pero con espacios    
        boolean second = service.register(admin2);
        assertFalse(second);
    }
}
