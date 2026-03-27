package co.unicauca.usermanagement.controller;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.service.IPatientChangeListener;
import co.unicauca.usermanagement.service.IPatientService;
import co.unicauca.usermanagement.service.IProfessionalService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ScheduleAppointmentFrameController {

    public interface View {
        Stage getStage();

        void setPatients(List<String> patients);
        void setProfessionals(List<String> professionals);

        String getSelectedPatientDisplayName();
        String getSelectedProfessionalDisplayName();
        LocalDate getSelectedDate();
        String getSelectedTime();
        String getMotivo();

        void clearTimeSelection();
        void clearMotivo();

        void showFeedback(String message);

        void openRegisterNewPatient(Stage owner, IPatientService patientService);
        void openConsultSchedules(Stage owner, IAppointmentService appointmentService, String professional, LocalDate date);
        void openSearchAppointments();
        void closeCurrentWindow();
        
        void setAvailableHours(List<String> hours);
    }

    private final View view;
    private final IAppointmentService appointmentService;
    private final IPatientService patientService;
    private final IProfessionalService professionalService;

    private final IPatientChangeListener patientChangeListener = this::onPatientsChanged;

     private final Set<LocalDate> holidays = new HashSet<>(Arrays.asList(
        LocalDate.of(2026, 1, 1),   // Año Nuevo
        LocalDate.of(2026, 1, 12),  // Reyes Magos trasladado
        LocalDate.of(2026, 3, 23),  // San José trasladado
        LocalDate.of(2026, 4, 2),   // Jueves Santo
        LocalDate.of(2026, 4, 3),   // Viernes Santo
        LocalDate.of(2026, 5, 1),   // Día del Trabajo
        LocalDate.of(2026, 5, 18),  // Ascensión trasladada
        LocalDate.of(2026, 6, 8),   // Corpus Christi trasladado
        LocalDate.of(2026, 6, 15),  // Sagrado Corazón trasladado
        LocalDate.of(2026, 6, 29),  // San Pedro y San Pablo trasladado
        LocalDate.of(2026, 7, 20),  // Independencia
        LocalDate.of(2026, 8, 7),   // Batalla de Boyacá
        LocalDate.of(2026, 8, 17),  // Asunción trasladada
        LocalDate.of(2026, 10, 12), // Día de la Raza trasladado
        LocalDate.of(2026, 11, 2),  // Todos los Santos trasladado
        LocalDate.of(2026, 11, 16), // Independencia de Cartagena trasladado
        LocalDate.of(2026, 12, 8),  // Inmaculada Concepción
        LocalDate.of(2026, 12, 25)  // Navidad
    ));
     
    public ScheduleAppointmentFrameController(
            View view,
            IAppointmentService appointmentService,
            IPatientService patientService,
            IProfessionalService professionalService
    ) {
        this.view = view;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.professionalService = professionalService;
    }

    public void onInit() {
        refreshPatients();
        refreshProfessionals();
        registerPatientObserver();
    }

    public void onClose() {
        if (patientService != null) {
            patientService.removePatientChangeListener(patientChangeListener);
        }
    }

    public void onNavigateToSearchAppointments() {
        view.openSearchAppointments();
        view.closeCurrentWindow();
    }

    public void onNewPatient() {
        view.openRegisterNewPatient(view.getStage(), patientService);
    }

    public void onConsultSchedules() {
        String professional = view.getSelectedProfessionalDisplayName();
        LocalDate date = view.getSelectedDate();

        if (professional == null || date == null) {
            view.showFeedback("Seleccione un profesional y una fecha para consultar horarios.");
            return;
        }
        
        if (!isDateSelectable(date)) {
            view.showFeedback("La fecha seleccionada no tiene atención disponible.");
            return;
        }

        view.openConsultSchedules(view.getStage(), appointmentService, professional, date);
    }

    public void onRegisterAppointment() {
        String patientDisplayName = view.getSelectedPatientDisplayName();
        String professionalDisplayName = view.getSelectedProfessionalDisplayName();
        LocalDate date = view.getSelectedDate();
        String time = view.getSelectedTime();
        String motivo = view.getMotivo();

        if (patientDisplayName == null || professionalDisplayName == null || date == null || time == null
                || motivo == null || motivo.isBlank()) {
            view.showFeedback("Por favor complete todos los campos antes de registrar la cita.");
            return;
        }

        Patient patient = resolvePatientByDisplayName(patientDisplayName);
        if (patient == null) {
            view.showFeedback("No se encontró el paciente seleccionado en la base de datos.");
            return;
        }
        
        if (!isDateSelectable(date)) {
            view.showFeedback("La fecha seleccionada no tiene atención disponible.");
            return;
        }

        Professional professional = buildProfessionalFromDisplayName(professionalDisplayName);
        Scheduler scheduler = buildDefaultScheduler();

        LocalDateTime appointmentDateTime = LocalDateTime.of(date, LocalTime.parse(time));
        Appointment appointment = new Appointment(
                LocalDateTime.now(),
                appointmentDateTime,
                motivo,
                scheduler,
                patient,
                professional
        );

        boolean ok = appointmentService != null && appointmentService.scheduleAppointment(appointment);
        if (ok) {
            view.showFeedback("Cita registrada para " + patientDisplayName + " con " + professionalDisplayName
                    + " el " + date + " a las " + time + ".");
            view.clearTimeSelection();
            view.clearMotivo();
            onDateOrProfessionalChanged();
        } else {
            view.showFeedback("No se pudo registrar la cita. Intente nuevamente.");
        }
    }

    private void registerPatientObserver() {
        if (patientService == null) return;
        patientService.addPatientChangeListener(patientChangeListener);
    }

    private void onPatientsChanged() {
        Platform.runLater(this::refreshPatients);
    }

    private void refreshPatients() {
        if (patientService == null) return;
        List<User> patients = patientService.getAllPatients();
        List<String> patientNames = patients.stream()
                .map(p -> p.getFirstName() + " " + p.getFirstLastName())
                .collect(Collectors.toList());
        view.setPatients(patientNames);
    }

    private void refreshProfessionals() {
        if (professionalService == null) return;
        List<User> professionals = professionalService.getAllProfessionals();
        List<String> names = professionals.stream()
                .map(User::getFirstName)
                .collect(Collectors.toList());
        view.setProfessionals(names);
    }

    private Patient resolvePatientByDisplayName(String patientDisplayName) {
        if (patientDisplayName == null || patientService == null) return null;

        return patientService.getAllPatients().stream()
                .filter(u -> u instanceof Patient)
                .map(u -> (Patient) u)
                .filter(p -> (p.getFirstName() + " " + p.getFirstLastName()).equals(patientDisplayName))
                .findFirst()
                .orElse(null);
    }

    private Professional buildProfessionalFromDisplayName(String professionalDisplayName) {
        Professional professional = new Professional();
        professional.setFirstName(professionalDisplayName);
        professional.setFirstLastName("");
        return professional;
    }

    private Scheduler buildDefaultScheduler() {
        Scheduler scheduler = new Scheduler();
        scheduler.setFirstName("Miguel");
        scheduler.setFirstLastName("");
        scheduler.setIdUser(0);
        return scheduler;
    }
    
    public void onDateOrProfessionalChanged() {
        
        LocalDate date = view.getSelectedDate();
        String professional = view.getSelectedProfessionalDisplayName();

        if (date == null || professional == null) {
            view.setAvailableHours(List.of());
            return;
        }
      
        //quemados para mostrar la funcionalidad. Luego los ajusta un admin
        List<LocalTime> allTimes = List.of(
            LocalTime.of(8, 0),
            LocalTime.of(8, 30),
            LocalTime.of(9, 0),
            LocalTime.of(9, 30),
            LocalTime.of(10, 0),
            LocalTime.of(10,30),
            LocalTime.of(11, 0),
            LocalTime.of(11, 30)
        );

        List<Appointment> availableHoursApp = appointmentService.findByProfessionalAndDate(professional, date);
        
        
        List<LocalTime> bookedTimes = new ArrayList<>();

        for (Appointment a : availableHoursApp) {
            bookedTimes.add(a.getAppointmenDate().toLocalTime());
        }

        List<LocalTime> available = allTimes.stream()
        .filter(t -> !bookedTimes.contains(t))
        .toList();

        List<String> result = available.stream()
        .map(t -> t.toString())
        .toList();
        
        view.setAvailableHours(result);
    }
    
     public void onDateChanged() {
        
        LocalDate date = view.getSelectedDate();
        

        if (date == null) {
            view.setAvailableHours(List.of());
            return;
        }
        
        if (!isDateSelectable(date)) {
            view.showFeedback("La fecha seleccionada no tiene atención disponible.");
        }
     }
     
    public boolean isDateSelectable(LocalDate date) {
        if (date == null) return false;
        if (date.isBefore(LocalDate.now())) return false;
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return false;
        if (holidays.contains(date)) return false;
        return appointmentService == null || appointmentService.isDateAvailable(date);
    }
}

