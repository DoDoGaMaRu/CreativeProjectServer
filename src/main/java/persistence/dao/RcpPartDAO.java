package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class RcpPartDAO extends DAO<RcpPart> {
    public RcpPart insert(RcpPart rcpPart) {
        return (RcpPart) execQuery(em -> {
            em.persist(rcpPart);
            return rcpPart;
        });
    }

    public RcpPart selectById(Long id) {
        return (RcpPart) execQuery(em -> em.find(RcpPart.class, id));
    }

    public List<RcpPart> selectAll() {
        return (List<RcpPart>) execQuery(em -> {
            Query query = em.createQuery("SELECT r FROM RcpPart r");
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            RcpPart target =  em.find(RcpPart.class, id);
            em.remove(target);
            return null;
        });
    }

    public RcpPart update(RcpPart rcpPart) {
        return (RcpPart) execQuery(em -> em.merge(rcpPart));
    }

    public Long count() {
        return (Long) execQuery(em -> {
            Query query = em.createQuery("SELECT COUNT(DISTINCT r.recipe.id) FROM RcpPart r");
            return (Long) query.getSingleResult();
        });
    }

    public List<RcpPart> selectAllByRcpId(Long recipeId) {
        return (List<RcpPart>) execQuery(em -> {
            Query query = em.createQuery("SELECT r FROM RcpPart r WHERE r.recipe.id = :recipeId");
            query.setParameter("recipeId", recipeId);
            return query.getResultList();
        });
    }
}