package persistence.dao;

import persistence.entity.*;

import javax.persistence.Query;
import java.util.List;

public class RefrigeratorDAO extends DAO<Refrigerator, Long> {
    private static RefrigeratorDAO refrigeratorDAO;

    public static RefrigeratorDAO getInstance() {
        if (refrigeratorDAO == null) {
            refrigeratorDAO = new RefrigeratorDAO();
        }
        return refrigeratorDAO;
    }

    private RefrigeratorDAO() {
        super(Refrigerator.class);
    }

    public List<Refrigerator> ascFindAllBy(String column, Object value, int firstResult, int maxResult) {
        return (List<Refrigerator>) execQuery(em -> {
            Query query = em.createQuery("SELECT t FROM "+ entityClass.getSimpleName() +" t WHERE t."+column+"=:"+column+" ORDER BY t.exprt_date")
                    .setParameter(column, value)
                    .setFirstResult(firstResult)
                    .setMaxResults(maxResult);
            return query.getResultList();
        });
    }
}
