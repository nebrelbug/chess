package models;

import chess.BenChessGame;
import chess.ChessGame;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameAdapter implements JsonDeserializer<ChessGame> {
    public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return ctx.deserialize(el, BenChessGame.class);
    }
}