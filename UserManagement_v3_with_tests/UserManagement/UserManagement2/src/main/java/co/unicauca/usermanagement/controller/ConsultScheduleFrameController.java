package co.unicauca.usermanagement.controller;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.appointmentmanagement.service.IAppointmentChangeListener;
import co.unicauca.appointmentmanagement.service.IAppointmentService;
import co.unicauca.usermanagement.AvailableSlot;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;

public class ConsultScheduleFrameController {

    public interface View {
        void setAvailableSlots(List<AvailableSlot> slots);
        void setAppointments(List<Appointment> appointments);
    }

    private final View view;
    private final IAppointmentService appointmentService;

    private String currentProfessional;
    private LocalDate currentDate;

    private boolean showingAvailableTab = true;

    private final List<String> timeSlots = Arrays.asList(
            "08:00",
            "08:30",
            "09:00",
            "09:30",
            "10:00",
            "10:30",
            "11:00",
            "11:30"
    );

    private final IAppointmentChangeListener appointmentChangeListener = this::onAppointmentsChanged;

    public ConsultScheduleFrameController(
            View view,
            IAppointmentService appointmentService,
            String professional,
            LocalDate date
    ) {
        this.view = view;
        this.appointmentService = appointmentService;
        this.currentProfessional = professional;
        this.currentDate = date;
    }

    public void onInit() {
        if (appointmentService != null) {
            appointmentService.addAppointmentChangeListener(appointmentChangeListener);
        }
        refresh();
    }

    public void onClose() {
        if (appointmentService != null) {
            appointmentService.removeAppointmentChangeListener(appointmentChangeListener);
        }
    }

    public void onShowAvailableTab() {
        showingAvailableTab = true;
        refresh();
    }

    public void onShowAppointmentsTab() {
        showingAvailableTab = false;
        refresh();
    }

    private void onAppointmentsChanged() {
        Platform.runLater(this::refresh);
    }

    private void refresh() {
        if (appointmentService == null) return;
        if (currentProfessional == null || currentDate == null) return;

        List<Appointment> appointments = appointmentService.findByProfessionalAndDate(currentProfessional, currentDate);

        Set<String> occupiedTimes = new HashSet<>();
        for (Appointment appointment : appointments) {
            if (appointment == null || appointment.getAppointmenDate() == null) continue;
            occupiedTimes.add(appointment.getAppointmenDate().toLocalTime().toString());
        }

        List<AvailableSlot> availableList = new ArrayList<>();

        for (int i = 0; i < timeSlots.size(); i++) {

            String start = timeSlots.get(i);

            if (occupiedTimes.contains(start)) continue;

            // calcular hora fin
            String end = (i + 1 < timeSlots.size())
                    ? timeSlots.get(i + 1)
                    : start; 

            String day = currentDate.toString();

            availableList.add(new AvailableSlot(day, start, end));
        }

        view.setAvailableSlots(availableList);
        view.setAppointments(appointments);
    }
}

