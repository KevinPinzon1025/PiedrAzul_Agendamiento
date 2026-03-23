package co.unicauca.usermanagement.main;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.AppointmentDirector;
import co.unicauca.appointmentmanagement.service.AppointmentServiceImpl;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.appointmentmanagement.service.ManualAppointmentBuilder;
import co.unicauca.appointmentmanagement.service.RescheduleAppointmentBuilder;
import co.unicauca.appointmentmanagement.service.SelfServiceAppointmentBuilder;
import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import co.unicauca.usermanagement.view.SearchAppointmentFrame;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import javafx.application.Application;


public class ClientMain {

    public static void main(String[] args) {
        Application.launch(SearchAppointmentFrame.class, args);

        AppointmentDirector director = new AppointmentDirector();

        // =========================
        // CREAR PACIENTE
        // =========================
        Patient patient = new Patient();
        patient.setIdUser(1059237786);
        patient.setLogin("alanb");
        patient.setPasswordHash("hash123");
        patient.setPasswordSalt("salt123");
        patient.setActive(true);
        patient.setFirstName("Alan");
        patient.setFirstLastName("Brito");
        patient.setSecondName("Andres");
        patient.setSecondLastName("Lopez");
        patient.setBirthdate(new Date());
        patient.setCellnumber(3201234567.0);
        patient.setGender('M');

        // =========================
        // CREAR PROFESIONAL
        // =========================
        Professional professional = new Professional();
        professional.setIdUser(2001);
        professional.setLogin("jignacio");
        professional.setPasswordHash("hash456");
        professional.setPasswordSalt("salt456");
        professional.setActive(true);
        professional.setFirstName("Jose");
        professional.setFirstLastName("Ignacio");
        professional.setSpeciality("Medicina General");
        professional.setType("Planta");
        professional.setTimeWindow(30);

        // =========================
        // CREAR SCHEDULER
        // =========================
        Scheduler scheduler = new Scheduler();
        scheduler.setIdUser(3001);
        scheduler.setLogin("juanp");
        scheduler.setPasswordHash("hash789");
        scheduler.setPasswordSalt("salt789");
        scheduler.setActive(true);
        scheduler.setFirstName("Juan");
        scheduler.setFirstLastName("Perez");

        // =========================
        // 1. CITA MANUAL
        // =========================
        ManualAppointmentBuilder manualBuilder = new ManualAppointmentBuilder(
                patient,
                professional,
                scheduler,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                "Consulta médica general"
        );

        director.setAppointmentBuilder(manualBuilder);
        director.buildManualAppointment();

        Appointment manualAppointment = director.getAppointment();
        System.out.println("CITA MANUAL:");
        printAppointment(manualAppointment);

        // =========================
        // 2. CITA DE AUTONOMA
        // =========================
        SelfServiceAppointmentBuilder selfServiceBuilder = new SelfServiceAppointmentBuilder(
                patient,
                professional,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                "Cita creada por autoservicio"
        );

        director.setAppointmentBuilder(selfServiceBuilder);
        director.buildSelfServiceAppointment();

        Appointment selfServiceAppointment = director.getAppointment();
        System.out.println("\nCITA AUTONOMA:");
        printAppointment(selfServiceAppointment);

        // =========================
        // 3. CITA REAGENDADA
        // =========================
        RescheduleAppointmentBuilder rescheduleBuilder = new RescheduleAppointmentBuilder(
                patient,
                professional,
                scheduler,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                "Cita reagendada por disponibilidad"
        );

        director.setAppointmentBuilder(rescheduleBuilder);
        director.buildRescheduleAppointment(manualAppointment);

        Appointment rescheduledAppointment = director.getAppointment();
        System.out.println("\nCITA REAGENDADA:");
        printAppointment(rescheduledAppointment);
    }

    private static void printAppointment(Appointment appointment) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        System.out.println("Fecha de agendamiento: " + appointment.getSchedulingDate().format(formatter));
        System.out.println("Fecha de la cita: " + appointment.getAppointmenDate().format(formatter));
        System.out.println("Observación: " + appointment.getObservation());

        if (appointment.getScheduler() != null) {
            System.out.println("Agendador: " 
                    + appointment.getScheduler().getFirstName() + " "
                    + appointment.getScheduler().getFirstLastName());
        } else {
            System.out.println("Agendador: No aplica, agendamiento autonomo");
        }

        System.out.println("Paciente: "
                + appointment.getPatient().getFirstName() + " "
                + appointment.getPatient().getFirstLastName());

        System.out.println("Profesional: "
                + appointment.getProfessional().getFirstName() + " "
                + appointment.getProfessional().getFirstLastName());

        System.out.println("-----------------------------------");
    }
}