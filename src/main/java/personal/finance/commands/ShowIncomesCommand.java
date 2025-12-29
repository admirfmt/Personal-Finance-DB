package personal.finance.commands;

import personal.finance.models.Transaction;
import personal.finance.service.ITransactionService;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

import static personal.finance.utility.Helper.*;

public class ShowIncomesCommand extends Command {

    public ShowIncomesCommand(ITransactionService transactionService) {
        super("Inkomst", "Visar inkomsten (årsvis, månadsvis, veckovis och dagvis)", transactionService);
    }

    @Override
    public void execute() {
        System.out.print("Skriv en tidsperiod (dag, vecka, månad, år): ");
        String incomePeriod = scanner.nextLine().toLowerCase();
        List<Transaction> filterIncome = new ArrayList<>();
        List<Transaction> transactions = transactionService.getAllTransactions();

        switch (incomePeriod) {
            case "dag":
                LocalDate dayDate = dateInput("Ange ett datum (yyyy-MM-dd): ");
                for (Transaction t : transactions) {
                    if (t.getAmount() > 0 && t.getDate().toLocalDate().equals(dayDate)) {
                        filterIncome.add(t);
                    }
                }
                break;
            case "vecka":
                LocalDate weekDate = dateInput("Ange ett datum i veckan (yyyy-MM-dd): ");
                int weekNumber = weekDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                int weekYear = weekDate.getYear();
                for (Transaction t : transactions) {
                    LocalDate transDate = t.getDate().toLocalDate();
                    int transWeek = transDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                    int transYear = transDate.getYear();
                    if (t.getAmount() > 0 && weekNumber == transWeek && weekYear == transYear) {
                        filterIncome.add(t);
                    }
                }
                break;
            case "månad":
                LocalDate monthDate = dateInput("Ange ett datum i månad (yyyy-MM-dd): ");
            { // kodblock eftersom "year" är redan deklarerat under "case år"
                int month = monthDate.getMonthValue();
                int year = monthDate.getYear();
                for (Transaction t : transactions) {
                    LocalDate transDate = t.getDate().toLocalDate();
                    if (t.getAmount() > 0 && transDate.getMonthValue() == month && transDate.getYear() == year) {
                        filterIncome.add(t);
                    }
                }
            }
            break;
            case "år":
                System.out.print("Ange år (yyyy): ");
                int year = scanner.nextInt();
                scanner.nextLine();
                for (Transaction t : transactions) {
                    LocalDate transDate = t.getDate().toLocalDate();
                    if (t.getAmount() > 0 && transDate.getYear() == year) {
                        filterIncome.add(t);
                    }
                }
                break;
            default:
                System.out.println("Fel alternativet. Försök igen.");
                break;
        }

        if (filterIncome.isEmpty()) {
            System.out.println("Inga inkomster att visa under denna period.");
        } else {
            System.out.println("Inkomster för denna period: ");
            for (Transaction t : filterIncome) {
                printTransactions(t, incomePeriod);
            }
        }
    }
}
