package personal.finance.commands;

import personal.finance.service.ITransactionService;
import personal.finance.service.IUserService;

public abstract class Command {
    protected final String name;
    protected final String description;
    public ITransactionService transactionService;
    public IUserService userService;

    protected Command(String name, String description, ITransactionService transactionService) {
        this.name = name;
        this.description = description;
        this.transactionService = transactionService;
    }

    // user-kommandon konstruktor
    protected Command(String name, String description, IUserService userService) {
        this.name = name;
        this.description = description;
        this.userService = userService;
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
