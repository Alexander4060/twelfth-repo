package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String getUsers(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getPrincipal());
        model.addAttribute("roles", authentication.getAuthorities());
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @PostMapping("/create")
    public String saveOrUpdateUser(@ModelAttribute("user") User user,
                                   @RequestParam(value = "roles", required = false) List<Long> roleIds) {
        if (user.getId() != null) {
            userService.updateUserWithRoles(user, roleIds);
        } else {
            userService.saveUser(user, roleIds);
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/new")
    public String getNewUserForm(Model model, Authentication authentication) {
        model.addAttribute("currentUser", authentication.getPrincipal());
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "new";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit")
    public String getUserForUpdate(@RequestParam("id") Long id, Model model, Authentication authentication) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("allRoles", authentication.getAuthorities());
        return "new";
    }

    @GetMapping("/user")
    public String viewUser(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getPrincipal());
        model.addAttribute("roles", authentication.getAuthorities());
        return "admin-user-profile";
    }
}
