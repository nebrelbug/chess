package request;

import chess.BenChessPiece;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    public static <T> T parse(String json, Class<T> classOfT) throws RequestException {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            System.out.println(e);
            throw new RequestException("bad request");
        }
    }
}