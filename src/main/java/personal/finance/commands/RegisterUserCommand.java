package personal.finance.commands;

import personal.finance.models.User;
import personal.finance.service.IUserService;

import java.util.Optional;

import static personal.finance.utility.Helper.scanner;

public class RegisterUserCommand extends Command {

    public RegisterUserCommand(IUserService userService) {
        super("Registrera användare", "Registrera ny användare", userService);
    }

    @Override
    public void execute() {
        System.out.println("==== REGISTRERA ANVÄNDARE ====");
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();

        if (username.isBlank()) {
            System.out.println("Användarnamn kan inte vara tomt!");
            return;
        }

        // Kolla om användare redan finns
        Optional<User> existingUser = userService.findByUsername(username);
        if (existingUser.isPresent()) {
            System.out.println("Användarnamnet '" + username + "' är redan upptaget. Välj ett annat.");
            return;
        }

        System.out.print("Lösenord: ");
        String password = scanner.nextLine();

        User user = userService.register(username, password);
        if (user != null) {
            System.out.println("Användare registrerad: " + user.getUsername());
        } else {
            System.out.println("Registrering misslyckades.");
        }
    }
}
