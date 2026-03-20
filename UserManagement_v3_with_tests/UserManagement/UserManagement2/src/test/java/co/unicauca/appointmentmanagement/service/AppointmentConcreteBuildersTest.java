package co.unicauca.appointmentmanagement.service;

import co.unicauca.appointmentmanagement.Appointment;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentConcreteBuildersTest {

    @Test
    void manualAppointmentBuilderShouldBuildAValidAppointment() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                AppointmentBuilderAndDirectorTest.createScheduler(),
                AppointmentBuilderAndDirectorTest.baseSchedulingDate(),
                AppointmentBuilderAndDirectorTest.baseAppointmentDate(),
                null
        );
        builder.createAppointment();

        builder.buildSchedulerData();
        builder.buildPatientData();
        builder.buildProfessionalData();
        builder.buildSchedulingDate();
        builder.buildAppointmentDate();
        builder.buildObservationData();

        Appointment appointment = builder.getAppointment();

        assertAll(
                () -> assertNotNull(appointment.getScheduler()),
                () -> assertEquals("patient1", appointment.getPatient().getLogin()),
                () -> assertEquals("professional1", appointment.getProfessional().getLogin()),
                () -> assertEquals(AppointmentBuilderAndDirectorTest.baseSchedulingDate(), appointment.getSchedulingDate()),
                () -> assertEquals(AppointmentBuilderAndDirectorTest.baseAppointmentDate(), appointment.getAppointmenDate()),
                () -> assertEquals("", appointment.getObservation())
        );
    }

    @Test
    void manualAppointmentBuilderShouldRejectAppointmentDateBeforeSchedulingDate() {
        LocalDateTime schedulingDate = AppointmentBuilderAndDirectorTest.baseSchedulingDate();
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                AppointmentBuilderAndDirectorTest.createScheduler(),
                schedulingDate,
                schedulingDate.minusHours(2),
                "Observacion"
        );
        builder.createAppointment();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                builder::buildAppointmentDate
        );

        assertEquals("La fecha de la cita no puede ser anterior a la fecha de agendamiento", exception.getMessage());
    }

    @Test
    void manualAppointmentBuilderShouldRequireScheduler() {
        ManualAppointmentBuilder builder = new ManualAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                null,
                AppointmentBuilderAndDirectorTest.baseSchedulingDate(),
                AppointmentBuilderAndDirectorTest.baseAppointmentDate(),
                "Observacion"
        );
        builder.createAppointment();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                builder::buildSchedulerData
        );

        assertEquals("La cita manual requiere un scheduler", exception.getMessage());
    }

    @Test
    void selfServiceAppointmentBuilderShouldLeaveSchedulerAsNull() {
        SelfServiceAppointmentBuilder builder = new SelfServiceAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                AppointmentBuilderAndDirectorTest.baseSchedulingDate(),
                AppointmentBuilderAndDirectorTest.baseAppointmentDate(),
                null
        );
        builder.createAppointment();

        builder.buildSchedulerData();
        builder.buildPatientData();
        builder.buildProfessionalData();
        builder.buildSchedulingDate();
        builder.buildAppointmentDate();
        builder.buildObservationData();

        Appointment appointment = builder.getAppointment();

        assertAll(
                () -> assertNull(appointment.getScheduler()),
                () -> assertEquals("patient1", appointment.getPatient().getLogin()),
                () -> assertEquals("professional1", appointment.getProfessional().getLogin()),
                () -> assertEquals("", appointment.getObservation())
        );
    }

    @Test
    void rescheduleAppointmentBuilderShouldReusePreviousAppointmentAndSetDefaultObservation() {
        Appointment previousAppointment = new Appointment();
        previousAppointment.setObservation("Cita anterior");

        RescheduleAppointmentBuilder builder = new RescheduleAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                AppointmentBuilderAndDirectorTest.createScheduler(),
                AppointmentBuilderAndDirectorTest.baseSchedulingDate(),
                AppointmentBuilderAndDirectorTest.baseAppointmentDate().plusDays(3),
                null
        );
        builder.setAppointment(previousAppointment);

        builder.buildSchedulerData();
        builder.buildPatientData();
        builder.buildProfessionalData();
        builder.buildSchedulingDate();
        builder.buildAppointmentDate();
        builder.buildObservationData();

        Appointment appointment = builder.getAppointment();

        assertSame(previousAppointment, appointment);
        assertAll(
                () -> assertNotNull(appointment.getScheduler()),
                () -> assertEquals(AppointmentBuilderAndDirectorTest.baseAppointmentDate().plusDays(3), appointment.getAppointmenDate()),
                () -> assertEquals("Cita reagendada", appointment.getObservation())
        );
    }

    @Test
    void rescheduleAppointmentBuilderShouldRejectNewDateBeforeRescheduleDate() {
        LocalDateTime rescheduleDate = AppointmentBuilderAndDirectorTest.baseSchedulingDate();
        RescheduleAppointmentBuilder builder = new RescheduleAppointmentBuilder(
                AppointmentBuilderAndDirectorTest.createPatient(),
                AppointmentBuilderAndDirectorTest.createProfessional(),
                AppointmentBuilderAndDirectorTest.createScheduler(),
                rescheduleDate,
                rescheduleDate.minusDays(1),
                "Reagendada"
        );
        builder.createAppointment();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                builder::buildAppointmentDate
        );

        assertEquals("La nueva fecha de la cita no puede ser anterior a la fecha de reagendamiento", exception.getMessage());
    }
}
