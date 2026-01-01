package personal.finance.commands;

import personal.finance.models.User;
import personal.finance.service.IUserService;

import static personal.finance.utility.Helper.scanner;

public class LoginUserCommand extends Command {

    private final IUserService userService;

    public LoginUserCommand(IUserService userService) {
        super("Logga in", "Logga in med användarnamn och lösenord", null);
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("==== LOGGA IN ====");
        System.out.print("Användarnamn: ");
        String username = scanner.nextLine();
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
