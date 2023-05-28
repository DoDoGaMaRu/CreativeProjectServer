package service;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;
import org.json.simple.JSONObject;
import persistence.dao.RefrigeratorDAO;
import persistence.dao.UserDAO;
import persistence.entity.User;

public class RefrigeratorService {
    private static RefrigeratorDAO refrigeratorDAO = new RefrigeratorDAO();

    public Response putIn(Request req) {

        return Response.builder().build();
    }

    public Response putOut(Request req) {

        return Response.builder().build();
    }

    /*
    public Response read(Request req) {

        return Response.builder().build();
    }
    */
}
