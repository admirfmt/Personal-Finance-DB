package personal.finance.commands;

import personal.finance.models.Transaction;
import personal.finance.service.ITransactionService;

import java.time.LocalDate;
import java.util.Comparator;

import static personal.finance.utility.Helper.*;

public class AddTransactionCommand extends Command {

    public AddTransactionCommand(ITransactionService transactionService) {
        super("Lägg in transaktion", "Lägger en ny transaktion", transactionService);
    }

    @Override
    public void execute() {
        System.out.println("==== LÄGG IN TRANSAKTION ====");
        // lön, inköp, donation, hyra, mat, godis m.m
        System.out.print("Beskriv din transaktion: ");
        String description = scanner.nextLine();

        // positiv nummer för inkomst (lön, donation), negativ nummer för utgifter (hyra, mat, inköp, godis)
        System.out.print("Belopp (skriv - för utgift): ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        // Typ
        System.out.print("Transaktionstyp (inkomst / utgift): ");
        String type = scanner.nextLine();

        // Datum
        LocalDate date = dateInput("Datum (yyyy-MM-dd): ");

        Transaction transaction = new Transaction(description, amount, type, date.atStartOfDay());
        transactionService.addTransaction(transaction);
        // sortera transaktioner
        transactionService.getAllTransactions().sort(Comparator.comparing(Transaction::getDate));

        System.out.println("Transaktionen lyckades att läggas in.");
    }
}
