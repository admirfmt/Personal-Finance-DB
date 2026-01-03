package personal.finance.service;

import personal.finance.models.User;
import personal.finance.repositories.IUserRepository;

import java.util.Optional;

public class DefaultUserService implements IUserService {

    private final IUserRepository userRepository;
    private User currentUser;

    public DefaultUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                System.err.println("Användarnamnet finns redan!");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Fel vid kontroll av användarnamn: " + e.getMessage());
            return null;
        }

        return userRepository.register(username, password);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.login(username, password);
        if (user != null) {
            currentUser = user;
        }
        return user;
    }

    @Override
    public void logout() {
        currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            System.err.println("Fel vid sökning av användare: " + e.getMessage());
            return Optional.empty();
        }
    }
}
