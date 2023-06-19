package persistence.dao;

import persistence.entity.*;
import javax.persistence.*;
import java.util.*;

public class IngredientDAO extends DAO<Ingredient, Long> {
    private static IngredientDAO ingredientDAO;

    public static IngredientDAO getInstance() {
        if (ingredientDAO == null) {
            ingredientDAO = new IngredientDAO();
        }
        return ingredientDAO;
    }

    private IngredientDAO() {
        super(Ingredient.class);
    }

    public List<Ingredient> search(String name) {
        return (List<Ingredient>) execQuery(em -> {
            Query query = em.createQuery( "SELECT t FROM "+ entityClass.getSimpleName() +" t WHERE t.name LIKE :name");
            query.setParameter("name", "%"+name+"%");
            return query.getResultList();
        });
    }
}