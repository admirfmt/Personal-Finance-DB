package personal.finance.service;

import personal.finance.models.User;

public interface IUserService {
    User register(String username, String password);
    User login(String username, String password);
    void logout();
    User getCurrentUser();
}
