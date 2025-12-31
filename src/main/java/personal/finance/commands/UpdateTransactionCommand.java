package personal.finance.commands;

import personal.finance.models.Transaction;
import personal.finance.service.ITransactionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static personal.finance.utility.Helper.scanner;

public class UpdateTransactionCommand extends Command {

    public UpdateTransactionCommand(ITransactionService transactionService) {
        super("Uppdatera transaktion", "Uppdaterar en transaktions belopp", transactionService);
    }

    @Override
    public void execute() {
        System.out.println("==== UPPDATERA TRANSAKTION ====");
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            System.out.println("Inga transaktioner att uppdatera.");
            return;
        }

        for (int i = 0; i < transactions.size(); i++) {
            System.out.printf("%d. %s %.2f %s %s%n", i + 1,
                    transactions.get(i).getDescription(),
                    transactions.get(i).getAmount(),
                    transactions.get(i).getType(),
                    transactions.get(i).getDate().toLocalDate());
        }

        System.out.print("\nSkriv transaktionsnummer som du vill uppdatera (0 för avbryt): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice <= 0 || choice > transactions.size()) {
            System.out.println("Du valde fel.");
            return;
        }

        Transaction oldTransaction = transactions.get(choice - 1);

        // kanske hitta bättre sätt att göra den?
        // beskrivning
        System.out.print("Ny beskrivning (lämna tom för att behålla \"" +
                oldTransaction.getDescription() + "\"): ");
        String newDescription = scanner.nextLine();
        if (newDescription.isBlank()) {
            newDescription = oldTransaction.getDescription();
        }
        // belopp
        System.out.print("Nytt belopp (lämna tom för att behålla \"" +
                oldTransaction.getAmount() + "\"): ");
        String amountInput = scanner.nextLine();
        double newAmount;
        if (amountInput.isBlank()) {
            newAmount = oldTransaction.getAmount();
        } else {
            newAmount = Double.parseDouble(amountInput.replace(",", "."));
        }
        // typ
        System.out.print("Ny typ (lämna tom för att behålla \"" +
                oldTransaction.getType() + "\"): ");
        String newType = scanner.nextLine();
        if (newType.isBlank()) {
            newType = oldTransaction.getType();
        }
        // datum
        System.out.println("Nytt datum (lämna tom rad för att behålla " +
                oldTransaction.getDate().toLocalDate() + "): ");
        String dateRaw = scanner.nextLine();
        LocalDateTime newDateTime;
        if (!dateRaw.isBlank()) {
            LocalDate newDate = LocalDate.parse(dateRaw, Transaction.DATE_FORMAT);
            newDateTime = newDate.atStartOfDay();
        } else {
            newDateTime = oldTransaction.getDate();
        }

        Transaction updatedTransaction = new Transaction(oldTransaction.getId(), newDescription, newAmount, newType, newDateTime);

        transactionService.updateTransaction(choice - 1, updatedTransaction);

        System.out.println("Transaktion uppdaterad.");
    }
}
