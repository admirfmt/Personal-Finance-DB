package personal.finance.commands;

import personal.finance.models.Transaction;
import personal.finance.service.ITransactionService;

import java.util.List;

import static personal.finance.utility.Helper.*;

public class DeleteTransactionCommand extends Command {

    public DeleteTransactionCommand(ITransactionService transactionService) {
        super("Radera transaktion", "Raderar en transaktion", transactionService);
    }

    @Override
    public void execute() {
        System.out.println("==== RADERA TRANSAKTION ====");
        List<Transaction> transactions = transactionService.getAllTransactions();
        // kolla om det inte är tomt
        if (transactions.isEmpty()) {
            System.out.println("Inga transaktioner att radera.");
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.printf("%d. %s %.2f %s %s\n", i + 1,
                    transactions.get(i).getDescription(),
                    transactions.get(i).getAmount(),
                    transactions.get(i).getType(),
                    transactions.get(i).getDate().toLocalDate());
        }

        System.out.print("\nSkriv transaktionsnummer som du vill radera (skriv 0 för avbryt): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= transactions.size()) {
            Transaction removeTransaction = transactionService.deleteTransaction(choice - 1);
            System.out.printf("Transaktion raderat: %s\n", removeTransaction.getDescription());
        } else if (choice != 0) {
            System.out.println("Du valde fel.");
        }
    }
}
