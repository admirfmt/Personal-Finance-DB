package personal.finance.repositories;

import personal.finance.models.User;

import java.util.Optional;

public interface IUserRepository {
    User register(String username, String password);
    User login(String username, String password);
    Optional<User> findByUsername(String username);
}
