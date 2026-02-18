package co.unicauca.usermanagement.service;

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
        UserService service = new UserService(repo);

        String login = "kevin";
        String fullName = "Kevin Santiago";
        String role = "ADMIN";
        boolean active = true;
        String password = "Abc123!";

        boolean first = service.register(login, fullName, role, active, password);
        assertTrue(first);

        boolean second = service.register(login, "Otro Nombre", "USER", true, password);
        assertFalse(second);
    }
    
    @Test
    void shouldTreatTrimmedLoginAsSameUser() {
        IUserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);

        boolean first = service.register("kevin", "Kevin", "ADMIN", true, "Abc123!");
        assertTrue(first);

        // mismo login pero con espacios    
        boolean second = service.register("  kevin  ", "Otro", "USER", true, "Abc123!");
        assertFalse(second);
    }
}
