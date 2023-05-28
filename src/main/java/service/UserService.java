package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONObject;
import persistence.dao.UserDAO;
import persistence.entity.User;

public class UserService {
    private static UserDAO userDAO = new UserDAO();

    //TODO COOKIE 생성

    public Response login(Request req) {
        JSONObject json = new JSONObject();
        json.put("cookie", "null");

        String id = req.getBody().get("ID").toString();
        String pw = req.getBody().get("PW").toString();

        User user = userDAO.selectByColumn(id);

        if(user != null && pw.equals(user.getPw())) {
            json.put("cookie", "인증 완료");
        }

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(json).
                build();
    }

    public Response regist(Request req) {
        JSONObject json = new JSONObject();
        json.put("IDDupl", "false");
        json.put("phNumDupl", "false");

        String id = req.getBody().get("ID").toString();
        String pw = req.getBody().get("PW").toString();
        String name = req.getBody().get("ID").toString();
        String phone = req.getBody().get("PW").toString();

        if(userDAO.selectByColumn(id) != null) {
            json.put("IDDupl", "true");
        }

        if(userDAO.selectByColumn(phone) != null) {
            json.put("phNumDupl", "true");
        }

        return Response.builder().
                status(ResponseState.SUCCESS).
                body(json).
                build();
    }
}
