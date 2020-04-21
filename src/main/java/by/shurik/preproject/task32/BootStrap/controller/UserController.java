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
public class UserController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ModelAndView getUserPage() {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ModelAndView("user", "user", userService.findByUsername(authUser.getName()));
    }

    @RequestMapping(path = "/admin", method = RequestMethod.GET)
    public ModelAndView getAdminPage() {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ModelAndView("admin", "user", userService.findByUsername(authUser.getName()));
    }

    @GetMapping({"/admin/welcome", "/admin/getAllUsers"})
    public String getWelcome(Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userService.findByUsername(authUser.getName()));
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
        Set<Role> roles = createRoleSet(allRoles);//result
        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/admin/getAllUsers";
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
        return "redirect:/admin/getAllUsers";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/getAllUsers";
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
