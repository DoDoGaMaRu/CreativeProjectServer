package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import persistence.dao.CookedDAO;
import persistence.dao.RecipeDAO;
import persistence.dao.UserDAO;
import persistence.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = UserDAO.getInstance();
    private final RecipeDAO recipeDAO = RecipeDAO.getInstance();
    private final CookedDAO cookedDAO = CookedDAO.getInstance();

    public Response login(Request req) {
        JSONObject resBody = new JSONObject();

        String id = req.getBody().get("id").toString();
        String pw = req.getBody().get("pw").toString();

        User user = userDAO.findBy("id", id);

        if(user != null && pw.equals(user.getPw())) {
            resBody.put("serial", user.getSerial());
            resBody.put("token", id);
        }

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(resBody).
                build();
    }

    public Response regist(Request req) {
        JSONObject resBody = new JSONObject();
        resBody.put("IDDupl", false);
        resBody.put("phNumDupl", false);
        boolean isDupl = false;

        String id = req.getBody().get("id").toString();
        String pw = req.getBody().get("pw").toString();
        String name = req.getBody().get("name").toString();
        String phone = req.getBody().get("phone").toString();

        if(userDAO.findBy("id", id) != null) {
            resBody.put("IDDupl", true);
            isDupl = true;
        }
        if(userDAO.findBy("phone", phone) != null) {
            resBody.put("phNumDupl", true);
            isDupl = true;
        }

        if (!isDupl){
            userDAO.create(User.builder()
                    .id(id)
                    .pw(pw)
                    .name(name)
                    .phone(phone).build());
        }

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(resBody).
                build();
    }

    public Response cooked(Request req) {
        JSONObject resBody = new JSONObject();

        Long userSerial = (Long) req.getCookie().get("serial");
        Long rcpSeq = (Long) req.getBody().get("rcpSeq");

        User user = userDAO.findByKey(userSerial);
        Recipe recipe = recipeDAO.findBy("rcpSeq", rcpSeq);

        Cooked cooked = Cooked.builder()
                .user(user)
                .recipe(recipe)
                .regdate(LocalDateTime.now())
                .build();
        cookedDAO.create(cooked);

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(resBody).
                build();
    }

    public Response usedRcp(Request req) {
        JSONObject resBody = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        Long userSerial = (Long) req.getCookie().get("serial");

        User user = userDAO.findByKey(userSerial);
        List<Cooked> cookedList = cookedDAO.descFindAllBy("user", user, 0, 15);

        JSONArray myCookedList = new JSONArray();
        for (Cooked cooked : cookedList) {
            JSONObject myCooked = new JSONObject();
            try {
                Recipe recipe = cooked.getRecipe();
                myCooked.put("key", cooked.getId());
                myCooked.put("rcpkey", recipe.getId());
                myCooked.put("regdate", cooked.getRegdate());
                myCooked.put("recipe", jsonParser.parse(recipe.getRcpJson()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            myCookedList.add(myCooked);
        }

        resBody.put("myCookedList", myCookedList);

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(resBody).
                build();
    }
}
