package api;

import network.protocol.Request;
import network.protocol.Response;
import service.RefrigeratorService;

public class _refrigerator {
    private static RefrigeratorService refrigeratorService = new RefrigeratorService();

    public static Response _postPutIn(Request req) {
        return refrigeratorService.putIn(req);
    }
    public static Response _postPutOut(Request req) {
        return refrigeratorService.putOut(req);
    }
    public static Response _getOpen(Request req) {
        return refrigeratorService.open(req);
    }
}
