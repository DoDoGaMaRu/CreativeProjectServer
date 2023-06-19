package api;

import network.protocol.Request;
import network.protocol.Response;
import service.UserService;

public class _user {
    private static UserService userService = new UserService();

    public static Response _postLogin(Request req) {
        return userService.login(req);
    }

    public static Response _postRegist(Request req) {
        return userService.regist(req);
    }

    public static Response _postCooked(Request req) {
        return userService.cooked(req);
    }

    public static Response _getCooked(Request req) {
        return userService.usedRcp(req);
    }
}
