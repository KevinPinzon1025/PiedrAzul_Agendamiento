package co.unicauca.usermanagement.main;

import co.unicauca.usermanagement.acces.Factory;
import co.unicauca.usermanagement.service.UserService;
import co.unicauca.usermanagement.view.StartFrame;


public class ClientMain {

    public static void main(String[] args) {
        UserService service = new UserService(Factory.getInstance().getRepository("sqlite"));

        // crea admin si no existe (simple)
        service.register("admin", "Administrador General", "Administrador", true, "Admin123*");

        java.awt.EventQueue.invokeLater(() -> {new StartFrame(service).setVisible(true);
});

    }

}