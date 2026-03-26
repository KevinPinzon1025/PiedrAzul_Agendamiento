package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.usermanagement.acces.IAppointmentRepository;
import co.unicauca.usermanagement.acces.ProfessionalRepositorySQL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppointmentServiceImpl implements IAppointmentService {

    private final IAppointmentRepository repository;
    private final ProfessionalRepositorySQL professionalRepository;

    private final List<IAppointmentChangeListener> appointmentChangeListeners = new CopyOnWriteArrayList<>();

    public AppointmentServiceImpl(IAppointmentRepository repository,
                                  ProfessionalRepositorySQL professionalRepository) {
        this.repository = repository;
        this.professionalRepository = professionalRepository;
    }

    @Override
    public List<Appointment> getAll() {
        return repository.getAll();
    }

    @Override
    public List<Appointment> findByProfessionalAndDate(String professional, LocalDate date) {
        return repository.findByProfessionalAndDate(professional, date);
    }

    @Override
    public List<String> getAllProfessionals() {
        return professionalRepository.getActiveProfessionalNames();
    }

    @Override
    public boolean isDateAvailable(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        if (appointment == null) return false;

        boolean saved = repository.saveAppointment(appointment);
        if (saved) {
            notifyAppointmentsChanged();
        }
        return saved;
    }

    @Override
    public void addAppointmentChangeListener(IAppointmentChangeListener listener) {
        if (listener == null) return;
        appointmentChangeListeners.add(listener);
    }

    @Override
    public void removeAppointmentChangeListener(IAppointmentChangeListener listener) {
        if (listener == null) return;
        appointmentChangeListeners.remove(listener);
    }

    private void notifyAppointmentsChanged() {
        for (IAppointmentChangeListener listener : appointmentChangeListeners) {
            try {
                listener.onAppointmentsChanged();
            } catch (Exception ignored) {
                // Un observador defectuoso no debe romper el flujo del modelo.
            }
        }
    }

}