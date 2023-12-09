package server;

import chess.ChessGame;
import exceptions.ResponseException;

import com.google.gson.Gson;
import models.AuthToken;
import models.Game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    record loginCredentials(String username, String password) {
    }

    record registerCredentials(String username, String password, String email) {
    }

    record createGameInfo(String gameName) {
    }

    record createGameResponse(String gameID) {
    }

    record gameList(ArrayList<Game> games) {
    }

    record joinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
    }

    public AuthToken login(String username, String password) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST",
                path,
                new loginCredentials(username, password),
                null,
                models.AuthToken.class);
    }

    public AuthToken register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST",
                path,
                new registerCredentials(username, password, email),
                null,
                models.AuthToken.class);
    }

    public int create(String tokenString, String gameName) throws ResponseException {
        var path = "/game";
        var res = this.makeRequest("POST",
                path,
                new createGameInfo(gameName),
                tokenString,
                createGameResponse.class);

        return Integer.parseInt(res.gameID);
    }

    public ArrayList<Game> list(String tokenString) throws ResponseException {
        var path = "/game";

        return this.makeRequest("GET",
                path,
                null,
                tokenString,
                gameList.class).games;
    }

    public void join(String tokenString, int gameId, ChessGame.TeamColor color) throws ResponseException {
        var path = "/game";

        this.makeRequest("PUT",
                path,
                new joinGameRequest(color, gameId),
                tokenString,
                null);
    }

    public Game getGame(String tokenString, int gameId) throws ResponseException {
        var path = "/game/" + gameId;

        return this.makeRequest("GET",
                path,
                null,
                tokenString,
                Game.class);
    }

    public void logout(String tokenString) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE",
                path,
                null,
                tokenString,
                null);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException, ResponseException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = models.Deserializer.parse(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        // since this is integer division, it will return true with any 2XX status code
        return status / 100 == 2;
    }
}
