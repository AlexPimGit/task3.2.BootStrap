package by.shurik.preproject.task32.BootStrap.service;

import by.shurik.preproject.task32.BootStrap.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    void updateUser(User user);

    void removeUser(Long id);

    List<User> listUser();

    User findByUsername(String name);

    User findByUserEmail(String email);
}
