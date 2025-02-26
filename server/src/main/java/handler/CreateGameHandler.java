package handler;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import network.requests.CreateGameRequest;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private MockDatabase db;
    private CreateGameService service;

    public CreateGameHandler(MockDatabase db) {
        this.db = db;
        service = new CreateGameService(db);
    }

    public String createGame(Request req, Response res, Gson gson) throws DataAccessException {
        var body = gson.fromJson(req.body(), CreateGameRequest.class);
        var createRequest = new CreateGameRequest(req.headers("authorization"), body.gameName());

        var result = service.createGame(createRequest);
        return gson.toJson(result);
    }
}
