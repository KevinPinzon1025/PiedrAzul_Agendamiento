package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfessionalRepositorySQL implements IUserRepository {

    private Connection conn;
    private static final String URL = "jdbc:sqlite:db/users.db";

    public ProfessionalRepositorySQL() {
        connect();
        createTable();
        seedData();
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS app_professional (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL UNIQUE,
                activo INTEGER NOT NULL
            )
        """;

        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedData() {
        String countSql = "SELECT COUNT(*) FROM app_professional";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String insertSql = """
            INSERT INTO app_professional (nombre, activo)
            VALUES (?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            insertProfessional(ps, "Dr. Jose Ignacio", 1);
            insertProfessional(ps, "Dr. Ibis Gonzales", 1);
            insertProfessional(ps, "Dr. Clara Ines", 1);
            insertProfessional(ps, "Dr. Maria Lopez", 1);
            insertProfessional(ps, "Dr. Pedro Ruiz", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertProfessional(PreparedStatement ps, String nombre, int activo) throws Exception {
        ps.setString(1, nombre);
        ps.setInt(2, activo);
        ps.executeUpdate();
    }

    public List<String> getActiveProfessionalNames() {
        List<String> professionals = new ArrayList<>();

        String sql = """
            SELECT nombre
            FROM app_professional
            WHERE activo = 1
            ORDER BY nombre
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                professionals.add(rs.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return professionals;
    }

    @Override
    public boolean save(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User findByLogin(String login) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<User> list() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}