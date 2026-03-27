package co.unicauca.appointmentmanagement.service;

import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentBuilderAndDirectorTest {

    @Test
    void createAppointmentShouldInitializeAnEmptyAppointment() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder(
                createPatient(), createProfessional(), createScheduler(),
                baseSchedulingDate(), baseAppointmentDate(), "Control general"
        );

        builder.createAppointment();

        assertNotNull(builder.getAppointment());
    }

    @Test
    void setAppointmentShouldRejectNullAppointment() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder(
                createPatient(), createProfessional(), createScheduler(),
                baseSchedulingDate(), baseAppointmentDate(), "Observacion"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setAppointment(null)
        );

        assertEquals("La cita no puede ser nula", exception.getMessage());
    }

    @Test
    void setAppointmentBuilderShouldRejectNullBuilder() {
        AppointmentDirector director = new AppointmentDirector();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> director.setAppointmentBuilder(null)
        );

        assertEquals("El builder no puede ser nulo", exception.getMessage());
    }

    @Test
    void directorShouldRejectOperationsWithoutConfiguredBuilder() {
        AppointmentDirector director = new AppointmentDirector();

        IllegalStateException getException = assertThrows(
                IllegalStateException.class,
                director::getAppointment
        );
        assertEquals("No se ha configurado un AppointmentBuilder", getException.getMessage());

        IllegalStateException buildException = assertThrows(
                IllegalStateException.class,
                director::buildManualAppointment
        );
        assertEquals("No se ha configurado un AppointmentBuilder", buildException.getMessage());
    }

    @Test
    void buildRescheduleAppointmentShouldRejectNullPreviousAppointment() {
        AppointmentDirector director = new AppointmentDirector();
        director.setAppointmentBuilder(new RescheduleAppointmentBuilder(
                createPatient(), createProfessional(), createScheduler(),
                baseSchedulingDate(), baseAppointmentDate().plusDays(2), "Reagendada"
        ));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> director.buildRescheduleAppointment(null)
        );

        assertEquals("La cita previa no puede ser nula", exception.getMessage());
    }

    @Test
    void directorShouldExecuteManualBuilderStepsInExpectedOrder() {
        TrackingAppointmentBuilder builder = new TrackingAppointmentBuilder();
        AppointmentDirector director = new AppointmentDirector();
        director.setAppointmentBuilder(builder);

        director.buildManualAppointment();

        assertEquals(List.of(
                "createAppointment",
                "buildSchedulerData",
                "buildPatientData",
                "buildProfessionalData",
                "buildSchedulingDate",
                "buildAppointmentDate",
                "buildObservationData"
        ), builder.steps);
    }

    private static class TrackingAppointmentBuilder extends AppointmentBuilder {
        private final List<String> steps = new ArrayList<>();

        @Override
        public void createAppointment() {
            super.createAppointment();
            steps.add("createAppointment");
        }

        @Override
        public void buildPatientData() {
            steps.add("buildPatientData");
        }

        @Override
        public void buildProfessionalData() {
            steps.add("buildProfessionalData");
        }

        @Override
        public void buildSchedulerData() {
            steps.add("buildSchedulerData");
        }

        @Override
        public void buildSchedulingDate() {
            steps.add("buildSchedulingDate");
        }

        @Override
        public void buildAppointmentDate() {
            steps.add("buildAppointmentDate");
        }

        @Override
        public void buildObservationData() {
            steps.add("buildObservationData");
        }
    }

    static Patient createPatient() {
        return new Patient(
                "Maria",
                "Lopez",
                LocalDate.now(),
                3001234567.0,
                'F',
                1,
                "patient1",
                "hash",
                "salt",
                true,
                "Ana",
                "Perez"
        );
    }

    static Professional createProfessional() {
        return new Professional(
                "Psicologia",
                "Medico",
                30,
                2,
                "professional1",
                "hash",
                "salt",
                true,
                "Carlos",
                "Ramirez"
        );
    }

    static Scheduler createScheduler() {
        return new Scheduler(3, "scheduler1", "hash", "salt", true, "Laura", "Gomez");
    }

    static LocalDateTime baseSchedulingDate() {
        return LocalDateTime.of(2026, 3, 20, 8, 0);
    }

    static LocalDateTime baseAppointmentDate() {
        return LocalDateTime.of(2026, 3, 22, 10, 30);
    }
}