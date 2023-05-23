package persistence.dao;

import persistence.entity.Cooked;
import javax.persistence.*;
import java.util.*;

public class CookedDAO extends DAO<Cooked> {
    public Cooked insert(Cooked cooked) {
        return (Cooked) execQuery(em -> {
            em.persist(cooked);
            return cooked;
        });
    }

    public Cooked selectById(Long id) {
        return (Cooked) execQuery(em -> em.find(Cooked.class, id));
    }

    public List<Cooked> selectAll() {
        return (List<Cooked>) execQuery(em -> {
            Query query = em.createQuery("SELECT c FROM Cooked c");
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            Cooked target =  em.find(Cooked.class, id);
            em.remove(target);
            return null;
        });
    }

    public Cooked update(Cooked cooked) {
        return (Cooked) execQuery(em -> em.merge(cooked));
    }
}