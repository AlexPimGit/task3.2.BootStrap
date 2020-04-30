package by.shurik.preproject.task32.BootStrap.service;

import by.shurik.preproject.task32.BootStrap.dao.UserDao;
import by.shurik.preproject.task32.BootStrap.model.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void addUser(User user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword().startsWith("$") || user.getPassword().equals("")) {
            user.setUserPassword(userDao.findByUserEmail(user.getEmail()).getPassword());
            userDao.updateUser(user);
        } else {
            user.setUserPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDao.updateUser(user);
        }
    }

    @Override
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    public List<User> listUser() {
        List<User> users = userDao.listUser();
        for (User user : users) {
            Hibernate.initialize(user.getRoles());
        }
        return users;
    }


    @Override
    public User findByUsername(String name) {
        return userDao.findByUsername(name);
    }

    @Override
    public User findByUserEmail(String email) {
        User user = userDao.findByUserEmail(email);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }
}
