package by.shurik.preproject.task32.BootStrap.dao;

import by.shurik.preproject.task32.BootStrap.model.Role;

import java.util.List;

public interface RoleDao {
    List listRole();

    Role getRoleByName(String name);

    Role getRoleById(Long id);
}
