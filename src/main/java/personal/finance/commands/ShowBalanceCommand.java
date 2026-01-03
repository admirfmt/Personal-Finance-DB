package personal.finance.commands;

import personal.finance.models.Transaction;
import personal.finance.service.ITransactionService;

import java.util.List;

public class ShowBalanceCommand extends Command {

    public ShowBalanceCommand(ITransactionService transactionService) {
        super("Nuvarande balans", "Visar nuvarande balans", transactionService);
    }

    @Override
    public void execute() {
        System.out.println("==== ALLA TRANSAKTIONER ====");
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (!transactions.isEmpty()) {
            for (int i = 0; i < transactions.size(); i++) {
                System.out.printf("%d. %s %.2f %s %s\n", i + 1,
                        transactions.get(i).getDescription(),
                        transactions.get(i).getAmount(),
                        transactions.get(i).getType(),
                        transactions.get(i).getDate().toLocalDate());
            }
            System.out.println("\nTotalt " + transactions.size() + " transaktioner.");
        } else {
            System.out.println("\nInga transaktioner att visa.");
            return;
        }

        double balance = transactionService.calculateBalance();

        System.out.printf("\nDen nuvarande balansen: %.2f\n", balance);
    }
}

