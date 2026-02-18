package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;

public class UserService {
    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    public boolean register(String login, String fullName, String role, boolean active, String passwordPlain) {
        if (login == null || login.isBlank()) return false;
        if (fullName == null || fullName.isBlank()) return false;
        if (role == null || role.isBlank()) return false;

        if (!PasswordPolicy.isValid(passwordPlain)) return false;

        if (repository.findByLogin(login) != null) return false;

        String salt = PasswordHasher.newSaltBase64();
        String hash = PasswordHasher.hashBase64(passwordPlain.toCharArray(), salt);

        User user = new User(login.trim(), fullName.trim(), role, active, hash, salt);
        return repository.save(user);
    }

    public User login(String login, String password) {
        User user = repository.findByLogin(login);
        if (user == null) return null;
        if (!user.isActive()) return null;

        boolean ok = PasswordHasher.verify(password, user.getPasswordSalt(), user.getPasswordHash());
        return ok ? user : null;
    }
}

