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

    public User selectBySerial(Long serial) {
        return (User) execQuery(em -> em.find(User.class, serial));
    }

    public User selectByColumn(Object column) {
        return (User) execQuery(em -> em.find(User.class, column));
    }

    public List<User> selectAll() {
        return (List<User>) execQuery(em -> {
            Query query = em.createQuery("SELECT u FROM User u");
            return query.getResultList();
        });
    }

    public void delete(Long serial) {
        execQuery(em -> {
            User target =  em.find(User.class, serial);
            em.remove(target);
            return null;
        });
    }

    public User update(User user) {
        return (User) execQuery(em -> em.merge(user));
    }
}
