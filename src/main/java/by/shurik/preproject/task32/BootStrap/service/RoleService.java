package by.shurik.preproject.task32.BootStrap.service;

import by.shurik.preproject.task32.BootStrap.model.Role;

import java.util.List;

public interface RoleService {
    void addRole(Role role);

    void updateRole(Role role);

    void removeRole(Long id);

    List listRole();

    Role getRoleByName(String name);
}
