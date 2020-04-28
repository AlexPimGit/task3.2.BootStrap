package by.shurik.preproject.task32.BootStrap.service;

import by.shurik.preproject.task32.BootStrap.model.Role;

import java.util.List;

public interface RoleService {
    List listRole();

    Role getRoleByName(String name);

    Role getRoleById(Long id);
}
