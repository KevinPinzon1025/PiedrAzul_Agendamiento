package co.unicauca.usermanagement.acces;

import co.unicauca.appointmentmanagement.Appointment;
import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import co.unicauca.usermanagement.Patient;
import co.unicauca.usermanagement.Professional;
import co.unicauca.usermanagement.Scheduler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepositorySQL implements IAppointmentRepository {

    private static final String URL = "jdbc:sqlite:db/users.db";

    @Override
    public void initializeDatabase() {
        createTables();
        seedData();
    }

    @Override
    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();

        String sql = """
            SELECT
                c.fecha_agendamiento,
                c.fecha,
                c.hora,
                c.motivo,
                c.agendador,
                c.paciente,
                c.profesional,
                c.documento_paciente
            FROM app_cita c
            ORDER BY c.fecha, c.hora
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByProfessionalAndDate(String professional, LocalDate date) {
        List<Appointment> result = new ArrayList<>();

        String sql = """
            SELECT
                c.fecha_agendamiento,
                c.fecha,
                c.hora,
                c.motivo,
                c.agendador,
                c.paciente,
                c.profesional,
                c.documento_paciente
            FROM app_cita c
            WHERE UPPER(c.profesional) = UPPER(?)
              AND c.fecha = ?
            ORDER BY c.hora
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, professional);
            ps.setString(2, date.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSetToAppointment(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<String> getAllProfessionals() {
        List<String> professionals = new ArrayList<>();

        String sql = """
            SELECT DISTINCT profesional
            FROM app_cita
            ORDER BY profesional
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                professionals.add(rs.getString("profesional"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return professionals;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws Exception {

        // Fechas
        LocalDateTime schedulingDate = LocalDateTime.parse(rs.getString("fecha_agendamiento"));
        LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
        LocalTime hora = LocalTime.parse(rs.getString("hora"));
        LocalDateTime appointmentDate = LocalDateTime.of(fecha, hora);

        // Datos simples
        String motivo = rs.getString("motivo");
        String agendadorStr = rs.getString("agendador");
        String pacienteStr = rs.getString("paciente");
        String profesionalStr = rs.getString("profesional");
        long documentoPaciente = rs.getLong("documento_paciente");

        // =========================================================
        // Reconstrucción de objetos (básica)
        // =========================================================

        Scheduler scheduler = new Scheduler();
        scheduler.setFirstName(agendadorStr); // simplificado

        Patient patient = new Patient();
        patient.setFirstName(pacienteStr); //️ simplificado
        patient.setIdUser(documentoPaciente);

        Professional professional = new Professional();
        professional.setFirstName(profesionalStr); //️ simplificado

        // =========================================================
        // Construcción del Appointment
        // =========================================================

        Appointment appointment = new Appointment();
        appointment.setSchedulingDate(schedulingDate);
        appointment.setAppointmenDate(appointmentDate);
        appointment.setObservation(motivo);
        appointment.setScheduler(scheduler);
        appointment.setPatient(patient);
        appointment.setProfessional(professional);

        return appointment;
    }

    private void createTables() {
        String sql = """
            CREATE TABLE IF NOT EXISTS app_cita (
                id_cita INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha_agendamiento TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                motivo TEXT NOT NULL,
                agendador TEXT NOT NULL,
                paciente TEXT NOT NULL,
                profesional TEXT NOT NULL,
                documento_paciente INTEGER NOT NULL,
                     
                CONSTRAINT UQ_fecha_hora UNIQUE (fecha,hora)
            )
            
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedData() {
        String countSql = "SELECT COUNT(*) FROM app_cita";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String insertSql = """
            INSERT INTO app_cita
            (fecha_agendamiento, fecha, hora, motivo, agendador, paciente, profesional, documento_paciente)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            insertAppointment(ps,
                    "2026-02-14T08:30:00",
                    "2026-04-01",
                    "10:45",
                    "Dolor de cabeza",
                    "Sch Juan Perez",
                    "pat. Alan Brito",
                    "Dr. Jose Ignacio",
                    1059237786L
            );

            insertAppointment(ps,
                    "2026-02-15T09:10:00",
                    "2026-04-01",
                    "11:30",
                    "Control general",
                    "Sch Juan Perez",
                    "pat. Pedro Medina",
                    "Dr. Jose Ignacio",
                    105896324L
            );

            insertAppointment(ps,
                    "2026-01-01T09:00:00",
                    "2026-04-02",
                    "11:35",
                    "Dolor de estómago",
                    "Sch Juan Perez",
                    "pat. Pedro Medina",
                    "Dr. Ibis Gonzales",
                    105896324L
            );

            insertAppointment(ps,
                    "2026-02-23T10:20:00",
                    "2026-04-03",
                    "09:15",
                    "Fractura de brazo",
                    "Sch Juan Perez",
                    "pat. Eduardo Santos",
                    "Dr. Clara Ines",
                    1059237269L
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertAppointment(PreparedStatement ps,
                                   String fechaAgendamiento,
                                   String fecha,
                                   String hora,
                                   String motivo,
                                   String agendador,
                                   String paciente,
                                   String profesional,
                                   long documentoPaciente) throws Exception {

        ps.setString(1, fechaAgendamiento);
        ps.setString(2, fecha);
        ps.setString(3, hora);
        ps.setString(4, motivo);
        ps.setString(5, agendador);
        ps.setString(6, paciente);
        ps.setString(7, profesional);
        ps.setLong(8, documentoPaciente);
        ps.executeUpdate();
    }

    @Override
    public boolean saveAppointment(Appointment newAppointment) {
        if (newAppointment == null) return false;

        // Mapea el modelo a los campos esperados por la tabla app_cita.
        String fechaAgendamiento = newAppointment.getSchedulingDate() != null
                ? newAppointment.getSchedulingDate().toString()
                : LocalDateTime.now().toString();

        LocalDate fecha = newAppointment.getAppointmenDate() != null
                ? newAppointment.getAppointmenDate().toLocalDate()
                : null;
        if (fecha == null) return false;

        LocalTime hora = newAppointment.getAppointmenDate().toLocalTime();

        String motivo = newAppointment.getObservation();
        if (motivo == null) motivo = "";

        Scheduler scheduler = newAppointment.getScheduler();
        String agendador = (scheduler != null)
                ? buildName("Sch", scheduler.getFirstName(), scheduler.getFirstLastName())
                : "Sch";

        Patient patient = newAppointment.getPatient();
        String paciente = (patient != null)
                ? buildName("pat.", patient.getFirstName(), patient.getFirstLastName())
                : "pat.";

        Professional professional = newAppointment.getProfessional();
        String profesional = (professional != null)
                ? buildName(professional.getFirstName(), null, professional.getFirstLastName())
                : "";

        long documentoPaciente = patient != null ? (long) patient.getIdUser() : 0L;

        ensureDatabaseExists();

        String insertSql = """
            INSERT INTO app_cita
            (fecha_agendamiento, fecha, hora, motivo, agendador, paciente, profesional, documento_paciente)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(insertSql)) {

            ps.setString(1, fechaAgendamiento);
            ps.setString(2, fecha.toString());
            ps.setString(3, hora.toString());
            ps.setString(4, motivo);
            ps.setString(5, agendador);
            ps.setString(6, paciente);
            ps.setString(7, profesional);
            ps.setLong(8, documentoPaciente);

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Appointment findById(double id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateAppointment(Appointment newAppointment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void ensureDatabaseExists() {
        // Garantiza que la tabla exista al menos una vez (evita fallos en ejecución si no se llamó
        // a initializeDatabase() desde ClientMain).
        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement()) {
            String sql = """
            CREATE TABLE IF NOT EXISTS app_cita (
                id_cita INTEGER PRIMARY KEY AUTOINCREMENT,
                fecha_agendamiento TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                motivo TEXT NOT NULL,
                agendador TEXT NOT NULL,
                paciente TEXT NOT NULL,
                profesional TEXT NOT NULL,
                documento_paciente INTEGER NOT NULL
            )
        """;
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildName(String prefixOrFirst, String second, String last) {
        // Construye nombres tolerando nulls.
        String first = prefixOrFirst != null ? prefixOrFirst : "";
        String mid = second != null ? second : "";
        String l = last != null ? last : "";
        String value = (first + (mid.isBlank() ? "" : " " + mid) + (l.isBlank() ? "" : " " + l)).trim();
        return value;
    }
}