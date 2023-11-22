package models;

import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.ResponseException;

import java.io.Reader;

public class Deserializer {
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

    public static <T> T parse(Reader jsonReader, Class<T> classOfT) throws ResponseException {
        try {
            return gson.fromJson(jsonReader, classOfT);
        } catch (Exception e) {
            throw new ResponseException(400, "bad request");
        }
    }
}
