package personal.finance.repositories;

import personal.finance.models.Transaction;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PostgresTransactionRepository implements ITransactionRepository {
    private final Connection connection;

    public PostgresTransactionRepository(String url, String user, String password) throws SQLException {

        connection = DriverManager.getConnection(url, user, password);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id SERIAL PRIMARY KEY," +
                    "description TEXT NOT NULL," +
                    "amount DOUBLE PRECISION NOT NULL," +
                    "type TEXT NOT NULL," +
                    "date TIMESTAMP NOT NULL" +
                    ");");
        }
    }

    @Override
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String description = rs.getString("description");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                transactions.add(new Transaction(id, description, amount, type, date));
            }
        } catch (SQLException e) {
            System.out.println("Kunde inte ladda transaktioner: " + e.getMessage());
        }

        transactions.sort(Comparator.comparing(Transaction::getDate));
        return transactions;
    }


    @Override
    public void save(List<Transaction> transactions) {
        String sql = "INSERT INTO transactions(description, amount, type, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Transaction t : transactions) {
                statement.setString(1, t.getDescription());
                statement.setDouble(2, t.getAmount());
                statement.setString(3, t.getType());
                statement.setTimestamp(4, Timestamp.valueOf(t.getDate()));
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fel vid radering av transaktion: " + e.getMessage());
        }
    }

}
