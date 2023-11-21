package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Turn objects into JSON
 */
public class Stringifier {

    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static String jsonify(Object o) {
        return gson.toJson(o);
    }
}
