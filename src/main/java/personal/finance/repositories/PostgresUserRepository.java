package personal.finance.repositories;

import personal.finance.models.User;

import java.sql.*;
import java.util.Optional;

public class PostgresUserRepository implements IUserRepository {
    private final Connection connection;

    public PostgresUserRepository(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")");
        }
    }

    @Override
    public User register(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return new User(id, username, password);
                }
            }
        } catch (SQLException e) {
            System.out.println("Kunde inte registrera användare: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    String uname = rs.getString("username");
                    String pwd = rs.getString("password");
                    return new User(id, uname, pwd);
                }
            }
        } catch (SQLException e) {
            System.out.println("Kunde inte logga in: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String uname = rs.getString("username");
                    String password = rs.getString("password");
                    return Optional.of(new User(id, uname, password));
                }
            }
        } catch (SQLException e) {
            System.err.println("Fel vid sökning av användare: " + e.getMessage());
        }
        return Optional.empty();
    }
}
