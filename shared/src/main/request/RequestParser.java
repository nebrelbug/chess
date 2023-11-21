package request;

import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ResponseException;


/**
 * HTTP Request
 */
public class RequestParser {

    static GsonBuilder builder = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(
                    ChessPiece.class, new PieceAdapter()
            );

    static Gson gson = builder.create();

    public static <T> T parse(String json, Class<T> classOfT) throws ResponseException {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            throw new ResponseException(400, "bad request");
        }
    }
}
