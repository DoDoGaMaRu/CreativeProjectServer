package updater;

import persistence.CEntityManagerFactory;
import persistence.dao.DAO;
import persistence.dao.Executable;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CustomConnectionDAO implements CustomConnection {
    private EntityManager em;
    private EntityTransaction et;

    @Override
    public Object execQuery(Executable<Object> query) {
        Object res = null;
        try {
            et.begin();
            res = query.exec(em);
            et.commit();
        }
        catch (Exception e) {
            et.rollback();
        }
        return res;
    }

    @Override
    public void initConnection() {
        CEntityManagerFactory.initialization();
        em = CEntityManagerFactory.createEntityManger();
        et = em.getTransaction();
    }

    @Override
    public void closeConnection() {
        em.close();
    }
}
