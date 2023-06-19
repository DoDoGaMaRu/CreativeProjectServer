package persistence.dao;

import persistence.entity.*;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO extends DAO<Recipe, Long>{
    private static RecipeDAO recipeDAO;

    public static RecipeDAO getInstance() {
        if (recipeDAO == null) {
            recipeDAO = new RecipeDAO();
        }
        return recipeDAO;
    }

    private RecipeDAO() {
        super(Recipe.class);
    }

    public List<Recipe> cookable(Long user_serial) {
        return (List<Recipe>) execQuery(em -> {
            Query query = em.createQuery("SELECT DISTINCT rp.recipe " +
                    "FROM RcpPart rp " +
                    "JOIN Refrigerator r ON rp.ingredient.id = r.ingredient.id " +
                    "WHERE r.user.serial = :user_serial " +
                    "GROUP BY rp.recipe.id " +
                    "HAVING COUNT(DISTINCT rp.ingredient.id) = ( " +
                    "    SELECT COUNT(rp2) FROM RcpPart rp2 WHERE rp2.recipe.id = rp.recipe.id " +
                    ")"
            );
            query.setParameter("user_serial", user_serial);
            return query.getResultList();
        });
    }

    public List<Recipe> semiCookable(Long user_serial) {
        return (List<Recipe>) execQuery(em -> {
            Query query = em.createQuery("SELECT DISTINCT rp.recipe " +
                    "FROM RcpPart rp " +
                    "JOIN Refrigerator r ON rp.ingredient.id = r.ingredient.id " +
                    "WHERE r.user.serial = :user_serial " +
                    "GROUP BY rp.recipe.id " +
                    "HAVING (COUNT(DISTINCT rp.ingredient.id) / (SELECT COUNT(rp2) FROM RcpPart rp2 WHERE rp2.recipe.id = rp.recipe.id)) > 0.5"
            );
            query.setParameter("user_serial", user_serial);
            return query.getResultList();
        });
    }
}
