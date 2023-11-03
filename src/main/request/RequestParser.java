package request;

import com.google.gson.Gson;
import dataAccess.DataAccessException;

import java.lang.reflect.Field;

/**
 * HTTP Request
 */
public class RequestParser {

    static Gson gson = new Gson();

    public static <T> T parse(String json, Class<T> classOfT) throws DataAccessException {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            throw new DataAccessException(400, "bad request");
        }
    }
}
