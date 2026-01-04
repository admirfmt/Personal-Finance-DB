package personal.finance.repositories;

import personal.finance.models.Transaction;
import personal.finance.utility.Helper;
import personal.finance.utility.Helper.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FileTransactionRepository implements ITransactionRepository {
    private static final String TRANSACTION_FILE = "transactions.txt";

    @Override
    public List<Transaction> load() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            return transactions;
        }

        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Transaction transactionFile = Helper.loadFileString(line);
                if (transactionFile != null) {
                    transactions.add(transactionFile);
                }
            }
            fileScanner.close();
            transactions.sort(Comparator.comparing(Transaction::getDate));
        } catch (FileNotFoundException exception) {
            System.out.println("Kunde inte ladda transaktioner: " + exception.getMessage());
        }
        return transactions;
    }

    @Override
    public void save(List<Transaction> transaction) {
        try {
            transaction.sort(Comparator.comparing(Transaction::getDate));

            PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE));
            for (Transaction t : transaction) {
                String transactionData = t.saveFileString();
                writer.println(transactionData);
            }
            writer.close();
        } catch (IOException exception) {
            System.out.println("Fel uppstod: " + exception.getMessage());
        }
    }

    @Override
    public void deleteById(long id) {
        // används endast för databas
    }

    @Override
    public void update(long id, Transaction transaction) {
        // används endast för databas
    }

    @Override
    public void insert(Transaction transaction) {
        // används endast för databas
    }

    @Override
    public void showTransactionDetails() {
        // används endast för databas
    }
}
