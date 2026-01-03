package personal.finance.service;

import personal.finance.commands.*;
import personal.finance.models.User;
import personal.finance.repositories.FileTransactionRepository;
import personal.finance.repositories.PostgresTransactionRepository;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TerminalCommandService implements ICommandService{
    private final List<Command> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final IUserService userService;
    private final PostgresTransactionRepository postgresTransactionRepository;
    private ITransactionService transactionService;

    FileTransactionRepository fileTransactionRepository = new FileTransactionRepository();

    public TerminalCommandService(IUserService userService, ITransactionService transactionService, PostgresTransactionRepository postgresTransactionRepository) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.postgresTransactionRepository = postgresTransactionRepository;
    }

    private void updateTransactionService() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            postgresTransactionRepository.setCurrentUserId(currentUser.getId());
        } else {
            postgresTransactionRepository.setCurrentUserId(0);
        }

        transactionService = new DefaultTransactionService(postgresTransactionRepository);

        // Uppdatera alla kommandon som använder transactionService
        for (Command cmd : commands) {
            if (cmd.transactionService != null) {
                cmd.transactionService = transactionService;
            }
        }
    }

    public void run() {

        fileTransactionRepository.load();

        while (true) {
            User currentUser = userService.getCurrentUser();

            System.out.println("\n=== PERSONAL FINANCE APPLICATION ===");
            if (currentUser != null) {
                System.out.println("Inloggad som: " + currentUser.getUsername());
            } else {
                System.out.println("Ingen användare inloggad.");
            }
            System.out.println("Välj ett alternativ: ");

            int index = 1;
            List<Integer> availableCommands = new ArrayList<>();

            for (int i = 0; i < commands.size(); i++) {
                Command cmd = commands.get(i);
                // Om inte inloggad visa bara Register, Login och Exit
                if (currentUser == null) {
                    if (cmd instanceof RegisterUserCommand ||
                            cmd instanceof LoginUserCommand ||
                            cmd instanceof ExitCommand) {
                        System.out.printf("%d. %s - %s%n", index++, cmd.getName(), cmd.getDescription());
                        availableCommands.add(i);
                    }
                } else {
                    // Om inloggad visa allt annat
                    if (!(cmd instanceof RegisterUserCommand ||
                            cmd instanceof LoginUserCommand)) {
                        System.out.printf("%d. %s - %s%n", index++, cmd.getName(), cmd.getDescription());
                        availableCommands.add(i);
                    }
                }
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

            if (option > 0 && option <= availableCommands.size()) {
                int indexCommand = availableCommands.get(option - 1);
                Command selectedCommand = commands.get(indexCommand);
                executeCommand(indexCommand + 1);

                // Kolla om det är login/logout kommando
                if (selectedCommand instanceof LoginUserCommand || selectedCommand instanceof LogoutUserCommand) {
                    updateTransactionService();
                }
            } else {
                System.out.println("Ogiltigt val!");
            }
        }
    }

    @Override
    public void registerCommand(Command command) {
        this.commands.add(command);
    }

    @Override
    public void executeCommand(int input) {
        if (input > 0 && input <= commands.size()) {
            commands.get(input - 1).execute();
        }
    }
}
