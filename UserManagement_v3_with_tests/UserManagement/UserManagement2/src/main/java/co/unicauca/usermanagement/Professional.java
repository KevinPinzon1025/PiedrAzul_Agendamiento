package co.unicauca.usermanagement;

/**
 *
 * @author Sam
 */
public class Professional extends User {
    private String speciality;
    private String type;
    private long timeWindow;

//constructor sin parametros
    public Professional() {
    }
    
//constructor con parametros
    public Professional(String speciality, String type, long timeWindow, double idUser, String login, String passwordHash, String passwordSalt, boolean active, String firstName, String firstLastName) {
        super(idUser, login, passwordHash, passwordSalt, active, firstName, firstLastName);
        this.speciality = speciality;
        this.type = type;
        this.timeWindow = timeWindow;
    }

//getters & setters

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(long timeWindow) {
        this.timeWindow = timeWindow;
    }
    
}
