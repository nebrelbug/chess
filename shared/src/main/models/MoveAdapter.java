package models;

import chess.BenChessMove;
import chess.ChessMove;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MoveAdapter implements JsonDeserializer<ChessMove> {
    public BenChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return ctx.deserialize(el, BenChessMove.class);
    }
}