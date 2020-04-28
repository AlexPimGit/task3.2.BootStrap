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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin/welcome")// сделать один адрес
    public String getWelcome(Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userService.findByUserEmail(authUser.getEmail()));
        model.addAttribute("users", userService.listUser());
        return "welcome";
    }

    @GetMapping("/admin/addUser")
    public ModelAndView showAddForm() {
        return new ModelAndView("addUser", "user", new User());
    }

    @PostMapping("/admin/addUser")
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

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "allRoles[]", required = false) String[] allRoles) {

        if (result.hasErrors()) {
            user.setId(id);
            return "admin/update";
        }
        Set<Role> roles = createRoleSet(allRoles);//result
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin/welcome";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/welcome";
    }

    private Set<Role> createRoleSet(String[] roleAdmin) {
        Set<String> setRole = new HashSet<>(Arrays.asList(roleAdmin));//preResult
        setRole.removeIf(Objects::isNull);
        Set<Role> roles = new HashSet<>();
        Iterator<String> itr = setRole.iterator();
        while (itr.hasNext()) {
            itr.forEachRemaining(roleName -> roles.add(roleService.getRoleByName(roleName)));
        }
        return roles;
    }
}
