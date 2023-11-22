package models;

import chess.BenChessPiece;
import chess.ChessPiece;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PieceAdapter implements JsonDeserializer<ChessPiece> {
    public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return new Gson().fromJson(el, BenChessPiece.class);
    }
}