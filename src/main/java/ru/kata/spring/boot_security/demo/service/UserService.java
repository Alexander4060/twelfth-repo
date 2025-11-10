package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUsers();
    User getUserById(Long id);
    void saveOrUpdateUser(User user);
    void deleteUserById(Long id);
}
