package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import persistence.dao.*;
import persistence.entity.Cooked;
import persistence.entity.RcpPart;
import persistence.entity.Recipe;
import persistence.entity.Refrigerator;

import java.util.List;

public class RecipeService {
    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final RcpPartDAO rcpPartDAO = RcpPartDAO.getInstance();
    private final RefrigeratorDAO refrigeratorDAO = RefrigeratorDAO.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final CookedDAO cookedDAO = CookedDAO.getInstance();

    public Response info(Request req) {

        return Response.builder().build();
    }

    public Response exprtDate(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");

        List<Refrigerator> refrigerators = refrigeratorDAO.ascFindAllBy("user", userDAO.findByKey(userSerial), 0, 5);
        List<Recipe> recipes = recipeDAO.semiCookable(userSerial);

        JSONArray recipeJsons = new JSONArray();
        try {
            for (Refrigerator ref : refrigerators) {
                for (Recipe rcp : recipes) {
                    List<RcpPart> rcpParts = rcpPartDAO.findAllBy("recipe", rcp);
                    boolean haveIng = false;
                    for (RcpPart rcpPart : rcpParts) {
                        if (rcpPart.getIngredient().getId().equals(ref.getIngredient().getId())) {
                            haveIng = true;
                            break;
                        }
                    }

                    if (haveIng) {
                        String jsonString = (rcp).getRcpJson();
                        JSONObject rcpJson = (JSONObject) jsonParser.parse(jsonString);

                        boolean isDuplicate = false;
                        String rcpSeq = (String) rcpJson.get("RCP_SEQ");
                        for (Object jo : recipeJsons) {
                            String curRcpSeq = (String) ((JSONObject)jo).get("RCP_SEQ");
                            if (rcpSeq.equals(curRcpSeq)) {
                                isDuplicate = true;
                            }
                        }
                        if (!isDuplicate) {
                            recipeJsons.add(rcpJson);
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        resBody.put("recipes", recipeJsons);
        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response cookable(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");

        List<Recipe> recipes = recipeDAO.cookable(userSerial);

        JSONArray recipeJsons = new JSONArray();
        try {
            for (Object recipe : recipes) {
                String jsonString = ((Recipe) recipe).getRcpJson();
                recipeJsons.add(jsonParser.parse(jsonString));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        resBody.put("recipes", recipeJsons);
        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response semiCookable(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");

        List<Recipe> recipes = recipeDAO.semiCookable(userSerial);

        JSONArray recipeJsons = new JSONArray();
        try {
            for (Object recipe : recipes) {
                String jsonString = ((Recipe) recipe).getRcpJson();
                recipeJsons.add(jsonParser.parse(jsonString));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        resBody.put("recipes", recipeJsons);
        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }

    public Response recent(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");

        List<Recipe> recipes = recipeDAO.semiCookable(userSerial);
        List<Cooked> cookedRecipes = cookedDAO.recentFindAllBy(userSerial);


        JSONArray recipeJsons = new JSONArray();
        try {
            for (Recipe recipe : recipes) {
                boolean unCooked = true;

                for(Cooked cookedRecipe : cookedRecipes) {
                    if(recipe.getId().equals(cookedRecipe.getRecipe().getId())) {
                        unCooked = false;
                        break;
                    }
                }
                if (unCooked) {
                    recipeJsons.add(jsonParser.parse(recipe.getRcpJson()));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        resBody.put("recipes", recipeJsons);
        return Response.builder()
                .status(ResponseState.SUCCESS)
                .body(resBody)
                .build();
    }
}
