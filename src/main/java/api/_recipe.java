package api;

import network.protocol.Request;
import network.protocol.Response;
import service.RecipeService;

public class _recipe {
    private static RecipeService recipeService = new RecipeService();

    public static Response _getInfo(Request req) {
        return recipeService.info(req);
    }
    public static Response _getExprtDate(Request req) {
        return recipeService.exprtDate(req);
    }
    public static Response _getCookable(Request req) {
        return recipeService.cookable(req);
    }
    public static Response _getNutrient(Request req) {
        return recipeService.nutrient(req);
    }
}
