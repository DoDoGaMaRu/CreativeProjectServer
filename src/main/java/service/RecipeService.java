package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONObject;
import persistence.dao.RcpPartDAO;
import persistence.dao.RecipeDAO;
import persistence.dao.RefrigeratorDAO;
import persistence.dao.UserDAO;
import persistence.entity.RcpPart;
import persistence.entity.Recipe;
import persistence.entity.Refrigerator;
import persistence.entity.User;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {
    private static RecipeDAO recipeDAO = new RecipeDAO();

    //TODO COOKIE 생성

    public Response info(Request req) {

        return Response.builder().build();
    }

    public Response exprtDate(Request req) {

        return Response.builder().build();
    }

    public Response cookable(Request req) {
        RefrigeratorDAO refrigeratorDAO = new RefrigeratorDAO();
        RcpPartDAO rcpPartDAO = new RcpPartDAO();
        RecipeDAO recipeDAO = new RecipeDAO();

        JSONObject json = new JSONObject();

        // 쿠키 정보안에 있는 PK?, ID? 일단 PK라 생각하고 작성 함 + 내 내장고를 들고 옴
        String serial = req.getCookie().get("SERIAL").toString();
        List<Refrigerator> myRefrigerator = refrigeratorDAO.selectAllBySerial(Long.parseLong(serial));

        // 내 내장고에서 가지고 있는 재료 아이디 추출
        List<Long> ingredientIds = new ArrayList<>();
        for (Refrigerator refrigerator : myRefrigerator) {
            ingredientIds.add(refrigerator.getIngredient().getId());
        }

        List<Recipe> recipes = new ArrayList<>();

        // 하나의 레시피에 들어 있는 모든 재료를 통해 냉장고에 있는 재료랑 비교
        Long cnt = rcpPartDAO.count();
        for(Long i = 1L; i <= cnt; i++) {
            List<RcpPart> rcpIngredients = rcpPartDAO.selectAllByRcpId(i);

            if(ingredientIds.size() < rcpIngredients.size()) {
                continue;
            }
            else {
                boolean pass = true;
                for(Long j = 1L; j <= rcpIngredients.size(); j++) {
                    if(!ingredientIds.contains(j)) {
                        pass = false;
                        break;
                    }
                }
                if(pass) {
                    recipes.add(recipeDAO.selectById(i));
                }
            }
        }

        json.put("recipes", recipes);

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(json).
                build();
    }

    public Response nutrient(Request req) {

        return Response.builder().build();
    }
}
