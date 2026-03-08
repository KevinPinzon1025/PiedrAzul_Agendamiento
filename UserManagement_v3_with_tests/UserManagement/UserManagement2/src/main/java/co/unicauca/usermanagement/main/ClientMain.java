package co.unicauca.usermanagement.main;

import co.unicauca.usermanagement.acces.AdminRepositorySql;
import co.unicauca.usermanagement.acces.Factory;
import co.unicauca.usermanagement.acces.IUserRepository;
import co.unicauca.usermanagement.acces.PatientRepositorySQL;
import co.unicauca.usermanagement.acces.ProfessionalRepositorySQL;
import co.unicauca.usermanagement.acces.SchedulerRepositorySQL;
import co.unicauca.usermanagement.service.AdminServiceImpl;
import co.unicauca.usermanagement.service.IUserService;
import co.unicauca.usermanagement.service.UserServiceFacade;
import co.unicauca.usermanagement.view.StartFrame;


public class ClientMain {

    public static void main(String[] args) {
        
        IUserRepository adminRepo = new AdminRepositorySql();
        IUserRepository profRepo = new ProfessionalRepositorySQL();
        IUserRepository schedulerRepo = new SchedulerRepositorySQL();
        IUserRepository patientRepo = new PatientRepositorySQL();
        
        UserServiceFacade service = new UserServiceFacade(adminRepo, patientRepo, profRepo, schedulerRepo);
        
        // crea admin si no existe (simple)
        
        //service.getAdminService().register("admin", "Administrador General", "Administrador", true, "Admin123*");

        
        java.awt.EventQueue.invokeLater(() -> {new StartFrame(service).setVisible(true);
});

    }

}