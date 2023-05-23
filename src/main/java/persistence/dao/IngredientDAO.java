package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class IngredientDAO extends DAO<Ingredient> {
    public Ingredient insert(Ingredient ingredient) {
        return (Ingredient) execQuery(em -> {
            em.persist(ingredient);
            return ingredient;
        });
    }

    public Ingredient selectById(Long id) {
        return (Ingredient) execQuery(em -> em.find(Ingredient.class, id));
    }

    public List<Ingredient> selectAll() {
        return (List<Ingredient>) execQuery(em -> {
            Query query = em.createQuery("SELECT i FROM Ingredient i");
            return query.getResultList();
        });
    }

    public void delete(Long id) {
        execQuery(em -> {
            Ingredient target =  em.find(Ingredient.class, id);
            em.remove(target);
            return null;
        });
    }

    public Ingredient update(Ingredient ingredient) {
        return (Ingredient) execQuery(em -> em.merge(ingredient));
    }
}