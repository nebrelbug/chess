package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import services.ClearDbService;
import spark.Request;
import spark.Response;

public class ClearDbHandler {

    public static String handleRequest(Request request, Response result) {

        result.type("application/json");

        try {
            ClearDbService.clear();
            result.status(200);
        } catch (Exception e) {
            result.status(500);
        }


        return new Gson().toJson(new Object());
    }
}
