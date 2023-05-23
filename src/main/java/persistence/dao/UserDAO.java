package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class UserDAO extends DAO<User> {
    public User insert(User user) {
        return (User) execQuery(em -> {
            em.persist(user);
            return user;
        });
    }

    public User selectById(Long id) {
        return (User) execQuery(em -> em.find(User.class, id));
    }

    public List<User> selectAll() {
        return (List<User>) execQuery(em -> {
            Query query = em.createQuery("SELECT u FROM User u");
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            User target =  em.find(User.class, id);
            em.remove(target);
            return null;
        });
    }

    public User update(User user) {
        return (User) execQuery(em -> em.merge(user));
    }
}
