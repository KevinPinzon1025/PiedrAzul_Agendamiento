package co.unicauca.usermanagement.service;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.acces.IUserRepository;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PatientServiceImplTest {

    @Test
    void shouldNotifyListenersWhenPatientIsRegisteredSuccessfully() {
        FakeUserRepository repository = new FakeUserRepository();
        repository.saveResult = true;
        PatientServiceImpl service = new PatientServiceImpl(repository);

        CountingPatientListener goodListener = new CountingPatientListener();
        ThrowingPatientListener badListener = new ThrowingPatientListener();
        service.addPatientChangeListener(goodListener);
        service.addPatientChangeListener(badListener);

        boolean result = service.register(new Patient());

        assertTrue(result);
        assertEquals(1, repository.saveCalls);
        assertEquals(1, goodListener.calls);
        assertEquals(1, badListener.calls);
    }

    @Test
    void shouldNotNotifyListenersWhenRegisterFails() {
        FakeUserRepository repository = new FakeUserRepository();
        repository.saveResult = false;
        PatientServiceImpl service = new PatientServiceImpl(repository);

        CountingPatientListener listener = new CountingPatientListener();
        service.addPatientChangeListener(listener);

        boolean result = service.register(new Patient());

        assertFalse(result);
        assertEquals(1, repository.saveCalls);
        assertEquals(0, listener.calls);
    }

    @Test
    void shouldIgnoreNullListener() {
        FakeUserRepository repository = new FakeUserRepository();
        repository.saveResult = true;
        PatientServiceImpl service = new PatientServiceImpl(repository);

        service.addPatientChangeListener(null);

        assertDoesNotThrow(() -> service.register(new Patient()));
    }

    @Test
    void shouldReturnAllPatientsFromRepository() {
        FakeUserRepository repository = new FakeUserRepository();
        Patient p1 = new Patient();
        p1.setFirstName("Ana");
        Patient p2 = new Patient();
        p2.setFirstName("Luis");
        repository.users = List.of(p1, p2);

        PatientServiceImpl service = new PatientServiceImpl(repository);

        assertEquals(2, service.getAllPatients().size());
        assertEquals("Ana", service.getAllPatients().get(0).getFirstName());
    }

    private static class FakeUserRepository implements IUserRepository {
        boolean saveResult;
        int saveCalls;
        List<User> users = new ArrayList<>();

        @Override
        public boolean save(User user) {
            saveCalls++;
            return saveResult;
        }

        @Override public User findByLogin(String login) { return null; }
        @Override public List<User> list() { return users; }
    }

    private static class CountingPatientListener implements IPatientChangeListener {
        int calls;
        @Override public void onPatientsChanged() { calls++; }
    }

    private static class ThrowingPatientListener implements IPatientChangeListener {
        int calls;
        @Override public void onPatientsChanged() {
            calls++;
            throw new RuntimeException("listener failure");
        }
    }
}
