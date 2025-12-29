package personal.finance;

import personal.finance.commands.*;
import personal.finance.repositories.FileTransactionRepository;
import personal.finance.repositories.ITransactionRepository;
import personal.finance.service.DefaultTransactionService;
import personal.finance.service.ITransactionService;
import personal.finance.service.TerminalCommandService;

public class Main {
    public static void main(String[] args) {
        ITransactionRepository transactionRepository = new FileTransactionRepository();
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
