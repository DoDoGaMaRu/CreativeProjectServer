package api;

import network.protocol.Request;
import network.protocol.Response;
import network.protocol.ResponseState;

public class _user {
    public static Response _postLogin(Request req) {
        return Response.builder()
                .status(ResponseState.SUCCESS)
                .build();
    }
}
