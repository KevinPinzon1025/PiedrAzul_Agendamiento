
package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.Administrator;
import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;

/**
 *
 * @author Sam
 */
public class UserServiceFacade {
    private AdminServiceImpl adminService;
    private PatientServiceImpl patientService;
    private ProfessionalServiceImpl professionalService;
    private SchedulerServiceImpl schedulerService;

    public UserServiceFacade(
            IUserRepository adminRepo, 
            IUserRepository patientRepo, 
            IUserRepository profRepo,
            IUserRepository schedulerRepo) 
    {
        this.adminService = new AdminServiceImpl(adminRepo);
        this.patientService = new PatientServiceImpl(patientRepo);
        this.professionalService = new ProfessionalServiceImpl(profRepo);
        this.schedulerService = new SchedulerServiceImpl(schedulerRepo);
    }

    //getter and setter
    public AdminServiceImpl getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    public PatientServiceImpl getPatientService() {
        return patientService;
    }

    public void setPatientService(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    public ProfessionalServiceImpl getProfessionalService() {
        return professionalService;
    }

    public void setProfessionalService(ProfessionalServiceImpl professionalService) {
        this.professionalService = professionalService;
    }

    public SchedulerServiceImpl getSchedulerService() {
        return schedulerService;
    }

    public void setSchedulerService(SchedulerServiceImpl schedulerService) {
        this.schedulerService = schedulerService;
    }
    
    //login que prueba con cada una de las implementaciones
    public User login(String login, String password){
        
        Administrator admin = (Administrator)this.adminService.login(login, password);
        if(admin != null) return admin;
        
        Scheduler scheduler = (Scheduler)this.schedulerService.login(login, password);
        if(scheduler != null) return scheduler;
        
        Patient patient = (Patient)this.patientService.login(login, password);
        if(patient != null) return patient;
        
        Professional professional = (Professional)this.professionalService.login(login, password);
        if(professional != null) return professional;
        
        return null;
    }
    
}
