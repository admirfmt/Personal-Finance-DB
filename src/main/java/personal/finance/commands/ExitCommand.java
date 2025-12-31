package personal.finance.commands;

import personal.finance.service.ITransactionService;

public class ExitCommand extends Command {

    public ExitCommand(ITransactionService transactionService) {
        super("Avsluta", "Avslutar programmet", transactionService);
    }

    @Override
    public void execute() {
        // transactionService.saveTransactionToFile();
        System.exit(0);
    }
}
