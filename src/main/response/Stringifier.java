package response;

import com.google.gson.Gson;

/**
 * Turn objects into JSON
 */
public class Stringifier {

    static Gson gson = new Gson();

    public static String jsonify(Object o) {
        return gson.toJson(o);
    }
}
