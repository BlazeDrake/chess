package handler;

import service.ListGamesService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;

import network.requests.ListGamesRequest;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    ListGamesService service;

    public ListGamesHandler() {
        this.service = new ListGamesService(db);
    }

    public String listGames(Request req, Response res, Gson gson) throws DataAccessException {
        var listRequest = new ListGamesRequest(req.headers("authorization"));
        var result = service.listGames(listRequest);
        return gson.toJson(result);
    }

}
