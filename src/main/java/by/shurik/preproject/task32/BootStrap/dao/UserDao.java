package by.shurik.preproject.task32.BootStrap.dao;

import by.shurik.preproject.task32.BootStrap.model.User;

import java.util.List;


public interface UserDao {
    void addUser(User user);

    void updateUser(User user);

    void removeUser(Long id);

    List listUser();

    User findByUsername(String name);

    User findByUserEmail(String email);
}
