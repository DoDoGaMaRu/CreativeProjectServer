package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class RefrigeratorDAO extends DAO<Refrigerator> {
    public Refrigerator insert(Refrigerator refrigerator) {
        return (Refrigerator) execQuery(em -> {
            em.persist(refrigerator);
            return refrigerator;
        });
    }

    public Refrigerator selectById(Long id) {
        return (Refrigerator) execQuery(em -> em.find(Refrigerator.class, id));
    }

    public List<Refrigerator> selectAll() {
        return (List<Refrigerator>) execQuery(em -> {
            Query query = em.createQuery("SELECT r FROM Refrigerator r");
            return query.getResultList();
        });
    }


    public List<Refrigerator> selectAllBySerial(Long serial) {
        return (List<Refrigerator>) execQuery(em -> {
            Query query = em.createQuery("SELECT r FROM Refrigerator r WHERE r.serial = :serial");
            query.setParameter("serial", serial);
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            Refrigerator target =  em.find(Refrigerator.class, id);
            em.remove(target);
            return null;
        });
    }

    public Refrigerator update(Refrigerator refrigerator) {
        return (Refrigerator) execQuery(em -> em.merge(refrigerator));
    }
}
