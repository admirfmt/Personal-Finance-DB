package personal.finance.service;

import personal.finance.commands.Command;

public interface ICommandService {
    void registerCommand(Command command);
    void executeCommand(int input);
}
