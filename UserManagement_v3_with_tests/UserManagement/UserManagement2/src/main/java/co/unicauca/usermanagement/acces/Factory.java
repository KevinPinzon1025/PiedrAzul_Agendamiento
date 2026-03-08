package co.unicauca.usermanagement.acces;

public class Factory {

    private static Factory instance;

    private Factory() {}

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public IUserRepository getRepository(String type) {
        
        switch(type){
            case "admin" -> {
                return new AdminRepositorySql();
            }
            case "scheduler" -> {
                return new SchedulerRepositorySQL();
            }
            case "professional" -> {
                return new ProfessionalRepositorySQL();
            }
            case "patient" -> {
                return new PatientRepositorySQL();
            }
        }
       
        return null;
    }
}

