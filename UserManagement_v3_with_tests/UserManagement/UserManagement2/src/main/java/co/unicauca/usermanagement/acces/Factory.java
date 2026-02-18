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
        if ("sqlite".equals(type)) {
            return new UserRepositorySqlite();
        }
        return null;
    }
}

