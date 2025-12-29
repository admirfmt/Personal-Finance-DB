package personal.finance.utility;

import personal.finance.models.Transaction;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.Scanner;

import static personal.finance.models.Transaction.DATE_FORMAT;

public class Helper {
    public static final Scanner scanner = new Scanner(System.in);

    public static LocalDate dateInput(String input) {
        while (true) {
            System.out.print(input);
            Scanner scanner = new Scanner(System.in);
            String inputDate = scanner.nextLine().trim();

            if (inputDate.isEmpty()) {
                System.out.println("Datumet kan inte vara tomt.");
                continue;
            }

            try {
                return LocalDate.parse(inputDate, DATE_FORMAT);
            } catch (Exception ignored) {
                System.out.println("Fel format. Använd yyyy-MM-dd");
            }
        }
    }

    public static void printTransactions(Transaction t, String incomePeriod) {
        LocalDate date = t.getDate().toLocalDate();
        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        if (incomePeriod.equals("vecka")) {
            System.out.printf("%s %.2f %s %s vecka: %d\n", t.getDescription(), t.getAmount(), t.getType(), date, weekNumber);
        } else {
            System.out.printf("%s %.2f %s %s\n", t.getDescription(), t.getAmount(), t.getType(), date);
        }
    }

    public static Transaction loadFileString(String line) {
        try {
            String[] lines = line.split("\\|");
            // vi har 4 element
            if (lines.length == 4) {
                String description = lines[0];
                double amount = Double.parseDouble(lines[1].replace(",", "."));
                String type = lines[2];
                LocalDate date = LocalDate.parse(lines[3], DATE_FORMAT);

                return new Transaction(description, amount, type, date.atStartOfDay());
            }
        } catch (Exception exception) {
            System.out.println("Fel i transaktion läsning: " + line);
        }
        return null;
    }
}
