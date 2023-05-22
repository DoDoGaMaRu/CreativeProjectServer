package persistence.dao;

import persistence.entity.Recipe;

public class RecipeDAO extends DAO<Recipe>{
    public Recipe insert(Recipe rcp) {
        return (Recipe) execQuery(em -> {
            em.persist(rcp);
            return rcp;
        });
    }
}
