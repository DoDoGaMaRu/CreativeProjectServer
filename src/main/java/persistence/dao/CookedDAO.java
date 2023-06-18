package persistence.dao;

import persistence.entity.Cooked;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class CookedDAO extends DAO<Cooked, Long> {
    private static CookedDAO cookedDAO;

    public static CookedDAO getInstance() {
        if (cookedDAO == null) {
            cookedDAO = new CookedDAO();
        }
        return cookedDAO;
    }

    private CookedDAO() {
        super(Cooked.class);
    }

    public List<Cooked> descFindAllBy(String column, Object value, int firstResult, int maxResult) {
        return (List<Cooked>) execQuery(em -> {
            Query query = em.createQuery("SELECT t FROM "+ entityClass.getSimpleName() +" t WHERE t."+column+"=:"+column+" ORDER BY t.regdate DESC")
                    .setParameter(column, value)
                    .setFirstResult(firstResult)
                    .setMaxResults(maxResult);
            return query.getResultList();
        });
    }

    public List<Cooked> recentFindAllBy(Long user_serial) {
        LocalDateTime standardDateTime = LocalDateTime.now().minusDays(4);
        return (List<Cooked>) execQuery(em -> {
            Query query = em.createQuery("SELECT t FROM "+ entityClass.getSimpleName() +" t WHERE t.user.serial=:user_serial AND t.regdate > :standardDate");
            query.setParameter("user_serial", user_serial);
            query.setParameter("standardDate", standardDateTime);
            return query.getResultList();
        });
    }
}