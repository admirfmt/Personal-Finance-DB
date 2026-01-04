package personal.finance.commands;

import personal.finance.service.IUserService;

public class LogoutUserCommand extends Command {

    public LogoutUserCommand(IUserService userService) {
        super("Logga ut", "Logga ut aktuell användare", userService);
    }

    @Override
    public void execute() {
        if (userService.getCurrentUser() == null) {
            System.out.println("Ingen användare är inloggad.");
            return;
        }
        System.out.println("Loggar ut: " + userService.getCurrentUser().getUsername());
        userService.logout();
    }
}
