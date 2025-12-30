package personal.finance;

import personal.finance.commands.*;
import personal.finance.repositories.ITransactionRepository;
import personal.finance.repositories.PostgresTransactionRepository;
import personal.finance.service.DefaultTransactionService;
import personal.finance.service.ITransactionService;
import personal.finance.service.TerminalCommandService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String dbUrl = System.getenv("DATABASE_URL");
        String dbUser = System.getenv("DATABASE_USER");
        String dbPassword = System.getenv("DATABASE_PASSWORD");

        ITransactionRepository transactionRepository = null;
        try {
            transactionRepository = new PostgresTransactionRepository(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        ITransactionService transactionService = new DefaultTransactionService(transactionRepository);
        TerminalCommandService commandService = new TerminalCommandService();

        commandService.registerCommand(new AddTransactionCommand(transactionService));
        commandService.registerCommand(new DeleteTransactionCommand(transactionService));
        commandService.registerCommand(new ShowBalanceCommand(transactionService));
        commandService.registerCommand(new ShowOutcomesCommand(transactionService));
        commandService.registerCommand(new ShowIncomesCommand(transactionService));
        commandService.registerCommand(new ExitCommand(transactionService));

        commandService.run();
    }
}
