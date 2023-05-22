package util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonUtil {
    public static JSONObject convertJsonObj(String jsonStr) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
