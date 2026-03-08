
package co.unicauca.usermanagement.acces;

import co.unicauca.usermanagement.Administrator;
import co.unicauca.usermanagement.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminRepositorySql implements IUserRepository {

    private Connection conn;

    public AdminRepositorySql() {
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
            CREATE TABLE IF NOT EXISTS Admin (
                idAdmin INTEGER PRIMARY KEY,
                login TEXT UNIQUE NOT NULL,
                firstName TEXT NOT NULL,
                firstLastName TEXT NOT NULL,
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
    public boolean save(User admin) {
        String sql = "INSERT INTO Admin (idAdmin, login, firstName, firstLastName, active, passwordHash, passwordSalt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (int)admin.getIdUser());
            ps.setString(2, admin.getLogin());
            ps.setString(3, admin.getFirstName());
            ps.setString(4, admin.getFirstLastName());
            ps.setInt(5, admin.isActive() ? 1 : 0);
            ps.setString(6, admin.getPasswordHash());
            ps.setString(7, admin.getPasswordSalt());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public User findByLogin(String login) {
        String sql = "SELECT * FROM Admin WHERE login = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Administrator(
                    rs.getInt("idAdmin"),
                    rs.getString("login"),
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt"),
                    rs.getInt("active") == 1,
                    rs.getString("firstName"),
                    rs.getString("firstLastName")
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
        String sql = "SELECT * FROM Admin";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new Administrator(
                    rs.getInt("idAdmin"),
                    rs.getString("login"),
                    rs.getString("passwordHash"),
                    rs.getString("passwordSalt"),
                                rs.getInt("active") == 1,
                    rs.getString("firstName"),
                    rs.getString("firstLastName")
                   
                     
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
