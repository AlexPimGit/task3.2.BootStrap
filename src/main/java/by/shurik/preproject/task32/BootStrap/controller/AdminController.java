package by.shurik.preproject.task32.BootStrap.controller;


import by.shurik.preproject.task32.BootStrap.model.Role;
import by.shurik.preproject.task32.BootStrap.model.User;
import by.shurik.preproject.task32.BootStrap.service.RoleService;
import by.shurik.preproject.task32.BootStrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/welcome")
    public String getWelcome(Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userService.findByUserEmail(authUser.getEmail()));
        model.addAttribute("users", userService.listUser());
        return "welcome";
    }

    @GetMapping("/addUser")
    public ModelAndView showAddForm() {
        return new ModelAndView("addUser", "user", new User());
    }

    @PostMapping("/addUser")
    public String addUser(@Valid User user,
                          @RequestParam(value = "allRoles[]", required = false) String[] allRoles,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "addUser";
        }
        Set<Role> roles = createRoleSet(allRoles);
        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/admin/welcome";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "allRoles[]", required = false) String[] allRoles) {

        if (result.hasErrors()) {
            user.setId(id);
            return "admin/update";
        }
        Set<Role> roles = createRoleSet(allRoles);
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin/welcome";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/welcome";
    }

    private Set<Role> createRoleSet(String[] allRoles) {
        Set<Role> roles = new HashSet<>();
        if (Arrays.asList(allRoles).contains(roleService.getRoleById(1L).getName())) {
            roles.add(roleService.getRoleById(1L));
        }
        if (Arrays.asList(allRoles).contains(roleService.getRoleById(2L).getName())) {
            roles.add(roleService.getRoleById(2L));
        }
        return roles;
    }
}
