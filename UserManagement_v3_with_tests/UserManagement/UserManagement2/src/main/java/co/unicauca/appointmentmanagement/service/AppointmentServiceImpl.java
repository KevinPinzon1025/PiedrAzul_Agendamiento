package co.unicauca.appointmentmanagement.service;

import co.unicauca.microkernel.piedaazul.common.entity.AppointmentEntity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceImpl implements IAppointmentService {

    private static final String URL = "jdbc:sqlite:db/users.db";

    public AppointmentServiceImpl() {
        createTables();
        seedData();
    }

    @Override
    public List<AppointmentEntity> getAll() {
        List<AppointmentEntity> appointments = new ArrayList<>();

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
                LocalDateTime fechaAgendamiento = LocalDateTime.parse(rs.getString("fecha_agendamiento"));
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                LocalTime hora = LocalTime.parse(rs.getString("hora"));
                LocalDateTime fechaCita = LocalDateTime.of(fecha, hora);

                AppointmentEntity appointment = new AppointmentEntity(
                        fechaAgendamiento,
                        fechaCita,
                        rs.getString("motivo"),
                        rs.getString("agendador"),
                        rs.getString("paciente"),
                        rs.getString("profesional"),
                        rs.getInt("documento_paciente")
                );

                appointments.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    @Override
    public List<AppointmentEntity> findByProfessionalAndDate(String professional, LocalDate date) {
        List<AppointmentEntity> result = new ArrayList<>();

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
                    LocalDateTime fechaAgendamiento = LocalDateTime.parse(rs.getString("fecha_agendamiento"));
                    LocalDate fecha = LocalDate.parse(rs.getString("fecha"));
                    LocalTime hora = LocalTime.parse(rs.getString("hora"));
                    LocalDateTime fechaCita = LocalDateTime.of(fecha, hora);

                    AppointmentEntity appointment = new AppointmentEntity(
                            fechaAgendamiento,
                            fechaCita,
                            rs.getString("motivo"),
                            rs.getString("agendador"),
                            rs.getString("paciente"),
                            rs.getString("profesional"),
                            rs.getInt("documento_paciente")
                    );

                    result.add(appointment);
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

    @Override
    public boolean isDateAvailable(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
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
                documento_paciente INTEGER NOT NULL
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
                    1059237786
            );

            insertAppointment(ps,
                    "2026-02-15T09:10:00",
                    "2026-04-01",
                    "11:30",
                    "Control general",
                    "Sch Juan Perez",
                    "pat. Pedro Medina",
                    "Dr. Jose Ignacio",
                    105896324
            );

            insertAppointment(ps,
                    "2026-01-01T09:00:00",
                    "2026-04-02",
                    "11:35",
                    "Dolor de estómago",
                    "Sch Juan Perez",
                    "pat. Pedro Medina",
                    "Dr. Ibis Gonzales",
                    105896324
            );

            insertAppointment(ps,
                    "2026-02-23T10:20:00",
                    "2026-04-03",
                    "09:15",
                    "Fractura de brazo",
                    "Sch Juan Perez",
                    "pat. Eduardo Santos",
                    "Dr. Clara Ines",
                    1059237269
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
                                   int documentoPaciente) throws Exception {

        ps.setString(1, fechaAgendamiento);
        ps.setString(2, fecha);
        ps.setString(3, hora);
        ps.setString(4, motivo);
        ps.setString(5, agendador);
        ps.setString(6, paciente);
        ps.setString(7, profesional);
        ps.setInt(8, documentoPaciente);
        ps.executeUpdate();
    }
}