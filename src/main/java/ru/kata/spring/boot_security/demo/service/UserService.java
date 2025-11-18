package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUsers();
    User getUserById(Long id);
    void saveUser(User user, List<Long> roleIds);
    void deleteUserById(Long id);
    void updateUserWithRoles(User user, List<Long> roleIds);
}
