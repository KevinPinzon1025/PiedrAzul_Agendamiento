package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.usermanagement.acces.IAppointmentRepository;
import co.unicauca.usermanagement.acces.IProfessionalRepository;
import co.unicauca.usermanagement.User;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AppointmentServiceImplTest {

    @Test
    void shouldReturnFalseWhenAppointmentIsNull() {
        FakeAppointmentRepository repository = new FakeAppointmentRepository();
        AppointmentServiceImpl service = new AppointmentServiceImpl(repository, new FakeProfessionalRepository());

        assertFalse(service.scheduleAppointment(null));
        assertEquals(0, repository.saveCalls);
    }

    @Test
    void shouldNotifyListenersWhenAppointmentIsSaved() {
        FakeAppointmentRepository repository = new FakeAppointmentRepository();
        repository.saveResult = true;
        AppointmentServiceImpl service = new AppointmentServiceImpl(repository, new FakeProfessionalRepository());

        CountingAppointmentListener goodListener = new CountingAppointmentListener();
        ThrowingAppointmentListener badListener = new ThrowingAppointmentListener();
        service.addAppointmentChangeListener(goodListener);
        service.addAppointmentChangeListener(badListener);

        boolean result = service.scheduleAppointment(new Appointment());

        assertTrue(result);
        assertEquals(1, goodListener.calls);
        assertEquals(1, badListener.calls);
    }

    @Test
    void shouldNotNotifyListenersWhenRepositoryFails() {
        FakeAppointmentRepository repository = new FakeAppointmentRepository();
        repository.saveResult = false;
        AppointmentServiceImpl service = new AppointmentServiceImpl(repository, new FakeProfessionalRepository());

        CountingAppointmentListener listener = new CountingAppointmentListener();
        service.addAppointmentChangeListener(listener);

        boolean result = service.scheduleAppointment(new Appointment());

        assertFalse(result);
        assertEquals(0, listener.calls);
    }

    @Test
    void shouldReturnFalseForWeekendDates() {
        AppointmentServiceImpl service = new AppointmentServiceImpl(new FakeAppointmentRepository(), new FakeProfessionalRepository());

        LocalDate saturday = nextDate(DayOfWeek.SATURDAY);
        LocalDate sunday = nextDate(DayOfWeek.SUNDAY);

        assertFalse(service.isDateAvailable(saturday));
        assertFalse(service.isDateAvailable(sunday));
    }

    @Test
    void shouldReturnTrueForWeekdayDates() {
        AppointmentServiceImpl service = new AppointmentServiceImpl(new FakeAppointmentRepository(), new FakeProfessionalRepository());
        LocalDate monday = nextDate(DayOfWeek.MONDAY);

        assertTrue(service.isDateAvailable(monday));
    }

    @Test
    void shouldDelegateGetAllProfessionalsToRepository() {
        FakeProfessionalRepository repository = new FakeProfessionalRepository();
        repository.names = List.of("Dr. Ana", "Dr. Luis");
        AppointmentServiceImpl service = new AppointmentServiceImpl(new FakeAppointmentRepository(), repository);

        assertEquals(List.of("Dr. Ana", "Dr. Luis"), service.getAllProfessionals());
    }

    private static LocalDate nextDate(DayOfWeek dayOfWeek) {
        LocalDate date = LocalDate.now();
        while (date.getDayOfWeek() != dayOfWeek) {
            date = date.plusDays(1);
        }
        return date;
    }

    private static class FakeAppointmentRepository implements IAppointmentRepository {
        boolean saveResult;
        int saveCalls;

        @Override
        public boolean saveAppointment(Appointment newAppointment) {
            saveCalls++;
            return saveResult;
        }

        @Override public Appointment findById(double id) { return null; }
        @Override public boolean updateAppointment(Appointment newAppointment) { return false; }
        @Override public void initializeDatabase() { }
        @Override public List<Appointment> getAll() { return List.of(); }
        @Override public List<Appointment> findByProfessionalAndDate(String professional, LocalDate date) { return List.of(); }
        @Override public List<String> getAllProfessionals() { return List.of(); }
    }

    private static class FakeProfessionalRepository implements IProfessionalRepository {
        List<String> names = new ArrayList<>();

        @Override public List<String> getActiveProfessionalNames() { return names; }
        @Override public boolean save(User user) { return false; }
        @Override public User findByLogin(String login) { return null; }
        @Override public List<User> list() { return List.of(); }
    }

    private static class CountingAppointmentListener implements IAppointmentChangeListener {
        int calls;
        @Override public void onAppointmentsChanged() { calls++; }
    }

    private static class ThrowingAppointmentListener implements IAppointmentChangeListener {
        int calls;
        @Override public void onAppointmentsChanged() {
            calls++;
            throw new RuntimeException("listener failure");
        }
    }
}
