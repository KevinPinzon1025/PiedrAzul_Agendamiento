package co.unicauca.usermanagement;

public abstract class User {
    private double idUser;
    private String login;
    private String passwordHash;
    private String passwordSalt; // NUEVO (Base64)
    private boolean active;    // true=Activo, false=Inactivo
    private String firstName;
    private String firstLastName;
    
    public User() {}

    public User(double idUser, String login, String passwordHash, String passwordSalt, boolean active, String firstName, String firstLastName) {
        this.idUser = idUser;
        this.login = login;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.active = active;
        this.firstName = firstName;
        this.firstLastName = firstLastName;
    }

    public double getIdUser() {
        return idUser;
    }

    public void setIdUser(double idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }
/*
    @Override
    public String toString() {
        return login + " | " + fullName + " | " + role + " | " + (active ? "Activo" : "Inactivo");
    }
*/
}
