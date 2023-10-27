//package services;
//
//import models.AuthToken;
//import request.Request;
//import response.Response;
//
///**
// * Service for joining a game
// */
//public class JoinGameService {
//
//    public static class JoinGameResult extends Response {
//        public JoinGameResult(int code) {
//            super(code);
//        }
//    }
//
//    public static class JoinGameRequest extends Request {
//        final AuthToken authToken;
//        final String playerColor;
//        final String gameID;
//
//        public JoinGameRequest(AuthToken authToken, String playerColor, String gameID) {
//            super(RequestMethod.PUT, "/game");
//            this.authToken = authToken;
//            this.playerColor = playerColor;
//            this.gameID = gameID;
//        }
//    }
//
//    public static JoinGameResult join(JoinGameRequest request) {
//        return null;
//    }
//
//}