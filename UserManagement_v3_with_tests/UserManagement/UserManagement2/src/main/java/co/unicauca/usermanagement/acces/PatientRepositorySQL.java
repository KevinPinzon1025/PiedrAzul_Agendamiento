
package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.User;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import co.unicauca.usermanagement.Patient;

/**
 *
 * @author Sam
 */
public class PatientRepositorySQL implements IUserRepository {

    private Connection conn;

    public PatientRepositorySQL() {
        connect();
        createTable();
        seedData();
    }

    /**
     * Ruta absoluta al fichero SQLite para no depender del directorio de trabajo del proceso
     * (evita conn null si el JDBC falla y el NPE en createTable queda oculto en un catch genérico).
     */
    private static String buildJdbcUrl() {
        try {
            Path dbDir = Paths.get("db").toAbsolutePath().normalize();
            Files.createDirectories(dbDir);
            Path dbFile = dbDir.resolve("users.db");
            return "jdbc:sqlite:" + dbFile.toString().replace('\\', '/');
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo preparar el directorio db/ para SQLite", e);
        }
    }
    
    private void connect() {
        try {
            conn = DriverManager.getConnection(buildJdbcUrl());
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo conectar a la base de datos SQLite (users.db). Compruebe el driver sqlite-jdbc.",
                    e);
        }
    }

    private void ensureConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Conexión SQL inválida o cerrada", e);
        }
    }
    
    private void createTable() {
        if (conn == null) {
            throw new IllegalStateException("Conexión SQL no inicializada tras connect()");
        }
        String sql = """
            CREATE TABLE IF NOT EXISTS app_user (
                id_user INTEGER PRIMARY KEY,
                login TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                password_salt TEXT NOT NULL,
                active INTEGER NOT NULL,
                first_name TEXT NOT NULL,
                first_last_name TEXT NOT NULL,
                second_name TEXT,
                second_last_name TEXT,
                birthdate TEXT,
                cellnumber REAL,
                gender TEXT
            )
        """;
        
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo crear la tabla app_user", e);
        }
    }
    
    private void seedData() {
        String countSql = "SELECT COUNT(*) FROM app_user";
        
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(countSql)) {
            
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String insertSql = """
            INSERT INTO app_user
            (id_user, login, password_hash, password_salt, active, first_name, first_last_name, second_name, second_last_name, birthdate, cellnumber, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            
            insertPatient(ps, 1, "juanp", "hash1", "salt1", 1, "Juan", "Pérez", "Carlos", "López", "1990-01-01", 3001234567.0, "M");
            insertPatient(ps, 2, "mariag", "hash2", "salt2", 1, "María", "Gómez", "Elena", "Rodríguez", "1985-05-15", 3002345678.0, "F");
            insertPatient(ps, 3, "carlosl", "hash3", "salt3", 1, "Carlos", "López", "Andrés", "Martínez", "1992-03-20", 3003456789.0, "M");
            insertPatient(ps, 4, "Luisal", "hash4", "salt4", 1, "Luisa", "Erazo", "Fernanda", "Díaz", "2005-03-25", 3003456789.0, "F");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void insertPatient(PreparedStatement ps, int id, String login, String hash, String salt, int active, String fn, String fln, String sn, String sln, String bd, double cell, String gen) throws Exception {
        ps.setInt(1, id);
        ps.setString(2, login);
        ps.setString(3, hash);
        ps.setString(4, salt);
        ps.setInt(5, active);
        ps.setString(6, fn);
        ps.setString(7, fln);
        ps.setString(8, sn);
        ps.setString(9, sln);
        ps.setString(10, bd);
        ps.setDouble(11, cell);
        ps.setString(12, gen);
        ps.executeUpdate();
    }
    
    @Override
    public boolean save(User user) {
        if (!(user instanceof Patient)) return false;
        Patient p = (Patient) user;
        
        String sql = """
            INSERT INTO app_user
            (id_user, login, password_hash, password_salt, active, first_name, first_last_name, second_name, second_last_name, birthdate, cellnumber, gender)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        ensureConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, p.getIdUser());
            ps.setString(2, p.getLogin());
            ps.setString(3, p.getPasswordHash());
            ps.setString(4, p.getPasswordSalt());
            ps.setInt(5, p.isActive() ? 1 : 0);
            ps.setString(6, p.getFirstName());
            ps.setString(7, p.getFirstLastName());
            ps.setString(8, p.getSecondName());
            ps.setString(9, p.getSecondLastName());
            ps.setString(10, p.getBirthdate() != null ? p.getBirthdate().toString() : null);
            ps.setDouble(11, p.getCellnumber());
            ps.setString(12, String.valueOf(p.getGender()));
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User findByLogin(String login) {
        ensureConnection();
        String sql = "SELECT * FROM app_user WHERE login = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> list() {
        ensureConnection();
        List<User> users = new ArrayList<>();
        
        String sql = "SELECT * FROM app_user ORDER BY first_name, first_last_name";
        
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToPatient(rs));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    private Patient mapResultSetToPatient(ResultSet rs) throws Exception {
        Patient p = new Patient();
        p.setIdUser(rs.getDouble("id_user"));
        p.setLogin(rs.getString("login"));
        p.setPasswordHash(rs.getString("password_hash"));
        p.setPasswordSalt(rs.getString("password_salt"));
        p.setActive(rs.getInt("active") == 1);
        p.setFirstName(rs.getString("first_name"));
        p.setFirstLastName(rs.getString("first_last_name"));
        p.setSecondName(rs.getString("second_name"));
        p.setSecondLastName(rs.getString("second_last_name"));
        // birthdate as Date, but stored as string, for simplicity skip or parse
        p.setCellnumber(rs.getDouble("cellnumber"));
        p.setGender(rs.getString("gender").charAt(0));
        return p;
    }
    
}
