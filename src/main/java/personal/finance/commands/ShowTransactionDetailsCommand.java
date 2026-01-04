package personal.finance.commands;

import personal.finance.repositories.PostgresTransactionRepository;
import personal.finance.service.ITransactionService;

import java.sql.SQLException;

public class ShowTransactionDetailsCommand extends Command {

    private final PostgresTransactionRepository postgresTransactionRepository;

    public ShowTransactionDetailsCommand(ITransactionService transactionService, PostgresTransactionRepository postgresTransactionRepository) {
        super("Transaktion detaljer", "Visar detaljer om transaktioner", transactionService);
        this.postgresTransactionRepository = postgresTransactionRepository;
    }

    @Override
    public void execute() {
        postgresTransactionRepository.showTransactionDetails();
    }
}
