package co.unicauca.usermanagement;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Sam
 */
public class Patient extends User {
    private String secondName;
    private String secondLastName;
    private LocalDate birthdate;
    private double cellnumber;
    private char gender; //F -Femenino, M - Masculino
    private String email;

//constructor sin parametros
    public Patient() {    
    }

//constructor con parametros
    public Patient(String secondName, String secondLastName, LocalDate birthdate, double cellnumber, char gender, double idUser, String login, String passwordHash, String passwordSalt, boolean active, String firstName, String firstLastName) {
        super(idUser, login, passwordHash, passwordSalt, active, firstName, firstLastName);
        this.secondName = secondName;
        this.secondLastName = secondLastName;
        this.birthdate = birthdate;
        this.cellnumber = cellnumber;
        this.gender = gender;
    }
//getters & setters

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public double getCellnumber() {
        return cellnumber;
    }

    public void setCellnumber(double cellnumber) {
        this.cellnumber = cellnumber;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email=email;
    }
    
}
