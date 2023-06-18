package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import persistence.dao.*;
import persistence.entity.Ingredient;
import persistence.entity.RcpPart;
import persistence.entity.Refrigerator;

import java.time.LocalDate;
import java.util.List;

public class IngredientService {
    private final UserDAO userDAO = UserDAO.getInstance();
    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance();
    private final RcpPartDAO rcpPartDAO = RcpPartDAO.getInstance();
    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final RefrigeratorDAO refrigeratorDAO = RefrigeratorDAO.getInstance();

    public Response search(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        JSONObject reqBody = req.getBody();
        String name = (String) reqBody.get("name");
        List<Ingredient> ingList = ingredientDAO.search(name);

        JSONArray ingredients = new JSONArray();
        for (Ingredient ing : ingList) {
            JSONObject ingJson = new JSONObject();
            ingJson.put("key", ing.getId());
            ingJson.put("name", ing.getName());

            ingredients.add(ingJson);
        }
        resBody.put("ingredients", ingredients);

        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response coincide(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");
        Long rcpSeq = (Long) req.getBody().get("rcpSeq");

        List<Refrigerator> refrigerators = refrigeratorDAO.findAllBy("user", userDAO.findByKey(userSerial));
        List<RcpPart> rcpParts = rcpPartDAO.findAllBy("recipe", recipeDAO.findBy("rcpSeq", rcpSeq));

        JSONArray myIngredients = new JSONArray();

        for (Refrigerator ref : refrigerators) {
            Long userIngId = ref.getIngredient().getId();
            for (RcpPart rp : rcpParts) {
                Ingredient rcpIng = rp.getIngredient();
                Long rcpIngId = rcpIng.getId();

                if (userIngId.equals(rcpIngId)) {
                    JSONObject myIng = new JSONObject();
                    LocalDate exprtDate = ref.getExprt_date();

                    myIng.put("myKey", ref.getId());
                    myIng.put("name", rcpIng.getName());
                    myIng.put("exprDate", (exprtDate!=null) ? exprtDate:LocalDate.MAX);

                    myIngredients.add(myIng);
                }
            }
        }

        resBody.put("myIngredients", myIngredients);

        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }
}
