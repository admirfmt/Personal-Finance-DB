package personal.finance.service;

import personal.finance.models.Transaction;
import personal.finance.repositories.ITransactionRepository;

import java.util.ArrayList;
import java.util.Collections;
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
        if (transaction.getId() == null) {
            transactionRepository.insert(transaction);
        }
        transactionList.add(transaction);
    }

    @Override
    public Transaction deleteTransaction(int index) {
        if (index >= 0 && index < transactionList.size()) {
            Transaction removed = transactionList.remove(index);
            if (removed.getId() != null) {
                transactionRepository.deleteById(removed.getId());
            }
            return removed;
        }
        return null;
    }

    public void updateTransaction(int index, Transaction updated) {
        if (index >= 0 && index < transactionList.size()) {
            Transaction old = transactionList.get(index);
            Long id = old.getId();
            updated.setId(id);
            transactionList.set(index, updated);
            if (id != null) {
                transactionRepository.update(id, updated);
            }
        }
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
        return Collections.unmodifiableList(transactionList);
    }
}
