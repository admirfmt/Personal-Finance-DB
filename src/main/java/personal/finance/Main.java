package personal.finance;

import personal.finance.commands.*;
import personal.finance.repositories.PostgresTransactionRepository;
import personal.finance.repositories.PostgresUserRepository;
import personal.finance.service.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        String dbUrl = System.getenv("DATABASE_URL");
        String dbUser = System.getenv("DATABASE_USER");
        String dbPassword = System.getenv("DATABASE_PASSWORD");

        PostgresUserRepository userRepository = new PostgresUserRepository(dbUrl, dbUser, dbPassword);
        PostgresTransactionRepository transactionRepository = new PostgresTransactionRepository(dbUrl, dbUser, dbPassword);

        IUserService userService = new DefaultUserService(userRepository);
        ITransactionService transactionService = new DefaultTransactionService(transactionRepository);

        TerminalCommandService commandService = new TerminalCommandService(
                userService,
                transactionService,
                transactionRepository
        );

        // user-kommandon
        commandService.registerCommand(new RegisterUserCommand(userService));
        commandService.registerCommand(new LoginUserCommand(userService));
        commandService.registerCommand(new LogoutUserCommand(userService));

        // transaktion-kommandon
        commandService.registerCommand(new AddTransactionCommand(transactionService));
        commandService.registerCommand(new DeleteTransactionCommand(transactionService));
        commandService.registerCommand(new UpdateTransactionCommand(transactionService));
        commandService.registerCommand(new ShowBalanceCommand(transactionService));
        commandService.registerCommand(new ShowOutcomesCommand(transactionService));
        commandService.registerCommand(new ShowIncomesCommand(transactionService));
        commandService.registerCommand(new ShowTransactionDetailsCommand(transactionService, transactionRepository));
        commandService.registerCommand(new ExitCommand(transactionService));

        commandService.run();
    }
}
