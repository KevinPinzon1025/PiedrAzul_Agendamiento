package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositorySqlite implements IUserRepository {

    private Connection conn;

    public UserRepositorySqlite() {
        connect();
        createTable();
    }

    private void connect() {
        try {
            // Crea carpeta db si no existe
            java.io.File dir = new java.io.File("db");
            if (!dir.exists()) dir.mkdirs();

            conn = DriverManager.getConnection("jdbc:sqlite:db/users.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS User (
                login TEXT PRIMARY KEY,
                fullName TEXT NOT NULL,
                role TEXT NOT NULL,
                active INTEGER NOT NULL,
                passwordHash TEXT NOT NULL,
                passwordSalt TEXT NOT NULL
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO User (login, fullName, role, active, passwordHash, passwordSalt) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.isActive() ? 1 : 0);
            ps.setString(5, user.getPasswordHash());
            ps.setString(6, user.getPasswordSalt());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public User findByLogin(String login) {
        String sql = "SELECT * FROM User WHERE login = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("login"),
                    rs.getString("fullName"),
                    rs.getString("role"),
                    rs.getInt("active") == 1,
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<User> list() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                    rs.getString("login"),
                    rs.getString("fullName"),
                    rs.getString("role"),
                    rs.getInt("active") == 1,
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt") 
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
