package api;

import network.protocol.Request;
import network.protocol.Response;
import service.IngredientService;

public class _ingredient {
    private static IngredientService ingredientService = new IngredientService();

    public static Response _getSearch(Request req) {
        return ingredientService.search(req);
    }
}
