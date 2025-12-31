package personal.finance.repositories;

import personal.finance.models.Transaction;

import java.util.List;

public interface ITransactionRepository {
    // ladda från filen
    List<Transaction> load();
    // spara till filen
    void save(List<Transaction> transactions);
    // radera från databas
    void deleteById(long id);
    // uppdatera transaktion i databas
    void update(long id, Transaction transaction);
    // lägg en transaktion
    void insert(Transaction transaction);
}
