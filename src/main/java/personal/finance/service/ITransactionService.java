package personal.finance.service;

import personal.finance.models.Transaction;

import java.util.List;

public interface ITransactionService {
    // lägg in transaktion i AddTransactionCommand
    void addTransaction(Transaction transaction);
    // radera transaktion i DeleteTransactionCommand
    Transaction deleteTransaction(int index);
    // ENDAST för ExitCommand i System.exit()
    void saveTransactionToFile();
    double calculateBalance();
    List<Transaction> getAllTransactions();
}
