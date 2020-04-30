package by.shurik.preproject.task32.BootStrap.dao;


import by.shurik.preproject.task32.BootStrap.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDaoImpl implements UserDao {
    private Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl() {
    }

    @Override
    public void addUser(User user) {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        entityManager.persist(user);
        LOGGER.log(Level.INFO, "User successfully saved. User details: " + user);
    }

    @Override
    public void updateUser(User user) {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        entityManager.merge(user);//
        LOGGER.log(Level.INFO, "User successfully updated. User details: " + user);
    }

    @Override
    public void removeUser(Long id) {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
        LOGGER.log(Level.INFO, "User successfully deleted. User details: " + user);
    }

    @Override
    public List<User> listUser() {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        List<User> userList = entityManager.createQuery("FROM User").getResultList();
        for (User user : userList) {
            LOGGER.log(Level.INFO, "User list: " + user);
        }
        return userList;
    }

    @Override
    public User findByUsername(String name) {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        String hql = "FROM User user WHERE user.name= :name";
        User user = (User) entityManager.createQuery(hql).setParameter("name", name).getSingleResult();
        return user;
    }

    @Override
    public User findByUserEmail(String email) {
        System.out.println("UserDaoImpl / EntityManager entityManager: " + entityManager);
        String hql = "FROM User user WHERE user.email= :email";
        User user = (User) entityManager.createQuery(hql).setParameter("email", email).getSingleResult();
        return user;
    }
}
