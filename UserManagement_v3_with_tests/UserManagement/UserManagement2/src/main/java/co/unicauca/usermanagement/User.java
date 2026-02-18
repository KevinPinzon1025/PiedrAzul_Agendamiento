package co.unicauca.usermanagement;

public class User {
    private String login;
    private String fullName;
    private String role;       // "Médico/Terapista", "Agendador de citas", "Administrador"
    private boolean active;    // true=Activo, false=Inactivo
    private String passwordHash;
    private String passwordSalt; // NUEVO (Base64)

    public User() {}

    public User(String login, String fullName, String role, boolean active, String passwordHash, String passwordSalt) {
        this.login = login;
        this.fullName = fullName;
        this.role = role;
        this.active = active;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }  
   
    public String getPasswordSalt() {
        return passwordSalt; 
    }
    
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt; 
    }

    @Override
    public String toString() {
        return login + " | " + fullName + " | " + role + " | " + (active ? "Activo" : "Inactivo");
    }
}
