package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONObject;
import persistence.dao.IngredientDAO;
import persistence.dao.UserDAO;
import persistence.entity.User;

public class IngredientService {
    private static IngredientDAO ingredientDAO = new IngredientDAO();

    public Response search(Request req) {

        return Response.builder().build();
    }
}
