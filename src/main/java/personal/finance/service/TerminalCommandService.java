package personal.finance.service;

import personal.finance.commands.*;
import personal.finance.repositories.FileTransactionRepository;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TerminalCommandService implements ICommandService{
    private final List<Command> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    FileTransactionRepository fileTransactionRepository = new FileTransactionRepository();

    public void run() {

        fileTransactionRepository.load();

        while (true) {

            System.out.println("\n=== PERSONAL FINANCE APPLICATION ===");
            System.out.println("Välj ett alternativ: ");

            // auto-index loop genom alla kommandon
            for (int i = 0; i < commands.size(); i++) {
                Command cmd = commands.get(i);
                System.out.printf("%d. %s - %s%n", i + 1, cmd.getName(), cmd.getDescription());
            }

            int option;

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException exception) {
                System.out.println("Du måste skriva ett nummer!");
                scanner.nextLine();
                continue;
            }
            executeCommand(option);
        }
    }

    @Override
    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public void executeCommand(int input) {
        if (input <= commands.size()) {
            commands.get(input - 1).execute();
        }
    }
}
