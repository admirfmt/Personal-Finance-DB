package personal.finance.commands;

import personal.finance.models.User;
import personal.finance.service.IUserService;

import java.util.Optional;

import static personal.finance.utility.Helper.scanner;

public class LoginUserCommand extends Command {

    public LoginUserCommand(IUserService userService) {
        super("Logga in", "Logga in med användarnamn och lösenord", userService);
    }

    @Override
    public void execute() {
        System.out.println("==== LOGGA IN ====");
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
        if (username.isBlank()) {
            System.out.println("Användarnamn kan inte vara tomt!");
            return;
        }

        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isEmpty()) {
            System.out.println("Användarnamnet '" + username + "' finns inte i databasen. Du måste registrera först.");
            return;
        }

        System.out.print("Lösenord: ");
        String password = scanner.nextLine();

        User user = userService.login(username, password);
        if (user != null) {
            System.out.println("Inloggad som " + user.getUsername());
        } else {
            System.out.println("Fel användarnamn eller lösenord.");
        }
    }
}
