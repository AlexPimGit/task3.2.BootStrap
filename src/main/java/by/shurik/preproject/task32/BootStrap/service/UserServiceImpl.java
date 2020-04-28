package by.shurik.preproject.task32.BootStrap.service;

import by.shurik.preproject.task32.BootStrap.dao.RoleDao;
import by.shurik.preproject.task32.BootStrap.dao.UserDao;
import by.shurik.preproject.task32.BootStrap.model.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
//не надо вызывать типовой код try entityManager / .begin();/.commit();/catch .rollback(); рекомендуется на каждый метод ставить (нет гибкости)
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    //@Autowired
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
        System.out.println("password = " + user.getUserPassword());

        if (user.getUserPassword().startsWith("$") || user.getUserPassword().equals("")) {
            user.setUserPassword(user.getPassword());
            userDao.updateUser(user);
            System.out.println("password = " + user.getUserPassword());
        } else {
            user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
            userDao.updateUser(user);
        }
    }

    @Override
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUser() {
        List<User> users = userDao.listUser();
        for (User user : users) {
            Hibernate.initialize(user.getRoles());
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String name) {
        return userDao.findByUsername(name);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUserEmail(String email) {
        User user = userDao.findByUserEmail(email);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }
}
