package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void saveUser(User user, List<Long> roleIds) {
        if (roleIds != null) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            userRepository.save(user);
        }
    }

        @Override
        @Transactional
        public void updateUserWithRoles (User user, List < Long > roleIds){
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(roles);

            User existingUser = userRepository.findById(user.getId()).orElse(null);

            if (existingUser != null) {
                if (user.getPassword().equals(existingUser.getPassword())) {
                    user.setPassword(existingUser.getPassword());
                } else {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(user);
        }

        @Override
        @Transactional
        public void deleteUserById (Long id){
            User user = userRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("User not found"));
            user.getRoles().clear();
            userRepository.save(user);
            userRepository.delete(user);
        }

        @Override
        @Transactional(readOnly = true)
        public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
            User user = userRepository.findByEmailWithRoles(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return user;
        }
    }
