package persistence.dao;

import javax.persistence.EntityTransaction;
import javax.persistence.EntityManager;

import persistence.CEntityManagerFactory;

public abstract class DAO<T> {
    protected Object execQuery(Executable<Object> query) {
        Object res = null;

        // set entity transaction
        CEntityManagerFactory.initialization();
        EntityManager em = CEntityManagerFactory.createEntityManger();
        EntityTransaction et = em.getTransaction();

        // try query
        try {
            et.begin();
            res = query.exec(em);
            et.commit();
        }
        catch (Exception e) {
            et.rollback();
        }
        finally {
            em.close();
        }

        return res;
    }
}
