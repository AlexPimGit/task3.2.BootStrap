package by.shurik.preproject.task32.BootStrap.dao;

import by.shurik.preproject.task32.BootStrap.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class RoleDaoImpl implements RoleDao {
    private Logger LOGGER = Logger.getLogger(RoleDaoImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

    public RoleDaoImpl() {
    }

    @Override
    public List<Role> listRole() {
        List<Role> roleList = entityManager.createQuery("FROM Role").getResultList();
        for (Role role : roleList) {
            LOGGER.log(Level.INFO, "Role list: " + role);
        }
        return roleList;
    }

    @Override
    public Role getRoleByName(String name) {
        String hql = "FROM Role role WHERE role.name= :name";
        Role role = (Role) entityManager.createQuery(hql).setParameter("name", name).getSingleResult();
        return role;
    }

    @Override
    public Role getRoleById(Long id) {
        String hql = "FROM Role role WHERE role.id= :id";
        return (Role) entityManager.createQuery(hql).setParameter("id", id).getSingleResult();
    }
}

