package co.unicauca.usermanagement.main;

import co.unicauca.microkernel.core.ReportPluginManager;
import co.unicauca.usermanagement.acces.AdminRepositorySql;
import co.unicauca.usermanagement.acces.IUserRepository;
import co.unicauca.usermanagement.acces.PatientRepositorySQL;
import co.unicauca.usermanagement.acces.ProfessionalRepositorySQL;
import co.unicauca.usermanagement.acces.SchedulerRepositorySQL;

import co.unicauca.usermanagement.service.UserServiceFacade;
import co.unicauca.usermanagement.view.StartFrame;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientMain {

    public static void main(String[] args) {
/*        
        IUserRepository adminRepo = new AdminRepositorySql();
        IUserRepository profRepo = new ProfessionalRepositorySQL();
        IUserRepository schedulerRepo = new SchedulerRepositorySQL();
        IUserRepository patientRepo = new PatientRepositorySQL();
        
        UserServiceFacade service = new UserServiceFacade(adminRepo, patientRepo, profRepo, schedulerRepo);
        
        // crea admin si no existe (simple)
        
        //service.getAdminService().register("admin", "Administrador General", "Administrador", true, "Admin123*");

        
        java.awt.EventQueue.invokeLater(() -> {new StartFrame(service).setVisible(true);});
*/
    //Inicializar el plugin manager con la ruta base de la aplicación.
        String basePath = getBaseFilePath();
        try {
            ReportPluginManager.init(basePath);

            Console presentationObject = new Console();
            presentationObject.start();

        } catch (Exception ex) {
            Logger.getLogger("Application").log(Level.SEVERE, "Error al ejecutar la aplicación", ex);
        }
    
    }
    
     /**
     * Obtiene la ruta base donde está corriendo la aplicación, sin importar que
     * sea desde un archivo .class o desde un paquete .jar.
     *
     */
    private static String getBaseFilePath() {
        try {
            String path = ClientMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = URLDecoder.decode(path, "UTF-8"); //This should solve the problem with spaces and special characters.
            File pathFile = new File(path);
            if (pathFile.isFile()) {
                path = pathFile.getParent();
                
                if (!path.endsWith(File.separator)) {
                    path += File.separator;
                }
                
            }

            return path;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, "Error al eliminar espacios en la ruta del archivo", ex);
            return null;
        }
    }

}