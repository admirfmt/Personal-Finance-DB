package personal.finance.commands;

import personal.finance.service.ITransactionService;

public abstract class Command {
    protected final String name;
    protected final String description;
    public ITransactionService transactionService;

    protected Command(String name, String description, ITransactionService transactionService) {
        this.name = name;
        this.description = description;
        this.transactionService = transactionService;
    }

    public abstract void execute();

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
