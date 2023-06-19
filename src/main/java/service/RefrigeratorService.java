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

public class RefrigeratorService {
    private final UserDAO userDAO = UserDAO.getInstance();
    private final RefrigeratorDAO refrigeratorDAO = RefrigeratorDAO.getInstance();
    private final IngredientDAO ingredientDAO = IngredientDAO.getInstance();

    public Response putIn(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");
        Long ingKey = (Long) req.getBody().get("key");
        LocalDate exprDate = (LocalDate) req.getBody().get("exprDate");

        Refrigerator newRef = Refrigerator.builder()
                .user(userDAO.findByKey(userSerial))
                .ingredient(ingredientDAO.findByKey(ingKey))
                .exprt_date(exprDate)
                .build();

        resBody.put("success", Boolean.TRUE);
        refrigeratorDAO.create(newRef);

        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response putOut(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long ingKey = (Long) req.getBody().get("myKey");

        refrigeratorDAO.delete(refrigeratorDAO.findByKey(ingKey));
        resBody.put("success", Boolean.TRUE);

        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response open(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");
        List<Refrigerator> refrigerators = refrigeratorDAO.findAllBy("user", userDAO.findByKey(userSerial));

        JSONArray myIngredients = new JSONArray();
        for (Refrigerator ref : refrigerators) {
            JSONObject myIng = new JSONObject();
            LocalDate exprtDate = ref.getExprt_date();

            myIng.put("myKey", ref.getId());
            myIng.put("name", ref.getIngredient().getName());
            myIng.put("exprDate", (exprtDate!=null) ? exprtDate:LocalDate.MAX);

            myIngredients.add(myIng);
        }

        resBody.put("myIngredients", myIngredients);

        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }
}
