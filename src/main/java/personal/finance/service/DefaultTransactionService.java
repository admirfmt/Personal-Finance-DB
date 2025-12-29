package personal.finance.service;

import personal.finance.models.Transaction;
import personal.finance.repositories.ITransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final List<Transaction> transactionList;

    public DefaultTransactionService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionList = new ArrayList<>(transactionRepository.load());
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
        transactionRepository.save(transactionList);
    }

    @Override
    public Transaction deleteTransaction(int index) {
        if (index >= 0 && index < transactionList.size()) {
            Transaction removedTransaction = transactionList.remove(index);
            transactionRepository.save(transactionList);
            return removedTransaction;
        }
        return null;
    }

    @Override
    public void saveTransactionToFile() {
        transactionRepository.save(transactionList);
    }

    @Override
    public double calculateBalance() {
        return transactionList.stream().mapToDouble(Transaction::getAmount).sum();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactionList);
    }
}
