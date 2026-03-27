package co.unicauca.usermanagement.controller;

import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.appointmentmanagement.Appointment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchAppointmentController {

    public interface View {
        void setProfessionals(List<String> professionals);

        String getSelectedProfessional();
        void setSelectedProfessional(String professional);

        LocalDate getSelectedDate();
        String getSearchText();

        void clearAppointments();
        void setAppointments(List<Appointment> appointments);
        void setTotal(int total);

        void showAlert(String message);

        void navigateToScheduleAppointment();
        void closeCurrentWindow();
    }

    private final View view;
    private final IAppointmentService appointmentService;

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

    public SearchAppointmentController(View view, IAppointmentService appointmentService) {
        this.view = view;
        this.appointmentService = appointmentService;
    }

    public void onInit() {
        if (appointmentService == null) return;
        view.setProfessionals(appointmentService.getAllProfessionals());
        view.setTotal(0);
        view.clearAppointments();
    }

    public boolean isDateSelectable(LocalDate date) {
        if (date == null) return false;
        if (date.isBefore(LocalDate.now())) return false;
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return false;
        if (holidays.contains(date)) return false;
        return appointmentService == null || appointmentService.isDateAvailable(date);
    }

    public void onNavigateToSchedule() {
        view.navigateToScheduleAppointment();
        view.closeCurrentWindow();
    }

    public void onSearchByText() {
        String text = view.getSearchText();
        if (text == null || text.isBlank()) {
            view.showAlert("Escriba el nombre de un profesional para realizar la búsqueda.");
            return;
        }

        String matched = findProfessionalIgnoreCase(text.trim());
        if (matched == null) {
            view.showAlert("El profesional ingresado no existe en la lista disponible.");
            return;
        }

        view.setSelectedProfessional(matched);
        onSearch();
    }

    public void onSearch() {
        view.clearAppointments();

        String professional = view.getSelectedProfessional();
        LocalDate date = view.getSelectedDate();

        if (professional == null || professional.isBlank() || date == null) {
            view.setTotal(0);
            view.showAlert("Por favor seleccione un profesional y una fecha.");
            return;
        }

        if (!isDateSelectable(date)) {
            view.setTotal(0);
            view.showAlert("La fecha seleccionada no tiene atención disponible.");
            return;
        }

        if (appointmentService == null) {
            view.setTotal(0);
            view.showAlert("Servicio no disponible.");
            return;
        }

        List<Appointment> list = appointmentService.findByProfessionalAndDate(professional, date);

        if (list.isEmpty()) {
            view.setTotal(0);
            view.showAlert("No hay citas programadas para este profesional en esa fecha.");
            return;
        }

        view.setAppointments(list);
        view.setTotal(list.size());
    }

    private String findProfessionalIgnoreCase(String text) {
        if (appointmentService == null) return null;
        for (String professional : appointmentService.getAllProfessionals()) {
            if (professional.equalsIgnoreCase(text)) return professional;
        }
        return null;
    }
}

