package personal.finance.repositories;

import personal.finance.models.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostgresTransactionRepository implements ITransactionRepository {
    private final Connection connection;
    private long currentUserId;

    public PostgresTransactionRepository(String url, String user, String password) throws SQLException {

        connection = DriverManager.getConnection(url, user, password);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id SERIAL PRIMARY KEY," +
                    "user_id BIGINT NOT NULL," +
                    "description TEXT NOT NULL," +
                    "amount DOUBLE PRECISION NOT NULL," +
                    "type TEXT NOT NULL," +
                    "date TIMESTAMP NOT NULL" +
                    ");");
        }
    }

    public void setCurrentUserId(long userId) {
        this.currentUserId = userId;
    }

    @Override
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        if (currentUserId == 0) {
            return transactions; // Ingen användare inloggad
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
             statement.setLong(1, currentUserId);

             try (ResultSet rs = statement.executeQuery()) {
                 while (rs.next()) {
                     long id = rs.getLong("id");
                     long userId = rs.getLong("user_id");
                     String description = rs.getString("description");
                     double amount = rs.getDouble("amount");
                     String type = rs.getString("type");
                     LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                     transactions.add(new Transaction(id, userId, description, amount, type, date));
                 }
             }
        } catch (SQLException e) {
            System.out.println("Kunde inte ladda transaktioner: " + e.getMessage());
        }

        transactions.sort(Comparator.comparing(Transaction::getDate));
        return transactions;
    }

    @Override
    public void save(List<Transaction> transactions) {
        // används inte med databasen
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM transactions WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setLong(2, currentUserId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fel vid radering av transaktion: " + e.getMessage());
        }
    }

    @Override
    public void update(long id, Transaction t) {
        String sql = "UPDATE transactions SET description = ?, amount = ?, type = ?, date = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, t.getDescription());
            statement.setDouble(2, t.getAmount());
            statement.setString(3, t.getType());
            statement.setTimestamp(4, Timestamp.valueOf(t.getDate()));
            statement.setLong(5, id);
            statement.setLong(6, currentUserId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Fel vid uppdatering av transaktion: " + e.getMessage());
        }
    }

    @Override
    public void insert(Transaction t) {
        String sql = "INSERT INTO transactions(user_id, description, amount, type, date) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, currentUserId);
            statement.setString(2, t.getDescription());
            statement.setDouble(3, t.getAmount());
            statement.setString(4, t.getType());
            statement.setTimestamp(5, Timestamp.valueOf(t.getDate()));

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    t.setId(id);
                    t.setUserId(currentUserId);
                }
            }

        } catch (SQLException e) {
            System.out.println("Fel vid insert av transaktion: " + e.getMessage());
        }
    }

    @Override
    public void showTransactionDetails() {
        String sql = "SELECT u.username, t.description, t.amount, t.type, t.date " +
                "FROM transactions t " +
                "INNER JOIN users u ON t.user_id = u.id " +
                "WHERE t.user_id = ? ";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, currentUserId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("Användare: %s | %s | %.2f | %s%n",
                    rs.getString("username"),
                    rs.getString("description"),
                    rs.getDouble("amount"),
                    rs.getString("type"));
                }
            } catch (SQLException e) {
                System.out.println("Något gick fel: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
