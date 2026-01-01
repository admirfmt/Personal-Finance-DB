package personal.finance.service;

import personal.finance.models.User;
import personal.finance.repositories.IUserRepository;

public class DefaultUserService implements IUserService {

    private final IUserRepository userRepository;
    private User currentUser;

    public DefaultUserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String password) {
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
}
