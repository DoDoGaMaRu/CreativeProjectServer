package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class RecipeDAO extends DAO<Recipe>{
    public Recipe insert(Recipe rcp) {
        return (Recipe) execQuery(em -> {
            em.persist(rcp);
            return rcp;
        });
    }

    public Recipe selectById(Long id) {
        return (Recipe) execQuery(em -> em.find(Recipe.class, id));
    }

    public List<Recipe> selectAll() {
        return (List<Recipe>) execQuery(em -> {
            Query query = em.createQuery("SELECT r FROM Recipe r");
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            Recipe target =  em.find(Recipe.class, id);
            em.remove(target);
            return null;
        });
    }

    public Recipe update(Recipe rcp) {
        return (Recipe) execQuery(em -> em.merge(rcp));
    }
}
