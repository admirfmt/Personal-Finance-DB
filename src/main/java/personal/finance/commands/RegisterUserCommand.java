package personal.finance.commands;

import personal.finance.models.User;
import personal.finance.repositories.IUserRepository;
import personal.finance.service.IUserService;

import java.util.Optional;

import static personal.finance.utility.Helper.scanner;

public class RegisterUserCommand extends Command {

    private final IUserService userService;
    private IUserRepository userRepository;

    public RegisterUserCommand(IUserService userService) {
        super("Registrera användare", "Registrera ny användare", null);
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("==== REGISTRERA ANVÄNDARE ====");
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
        System.out.print("Lösenord: ");
        String password = scanner.nextLine();

        User user = userService.register(username, password);
        if (user != null) {
            System.out.println("Användare registrerad: " + user.getUsername());
        } else {
            System.out.println("Registrering misslyckades (kanske finns användarnamnet redan).");
        }
    }
}
