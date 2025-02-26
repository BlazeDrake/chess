package handler;

import com.google.gson.Gson;
import dataaccess.localimplementation.MockDatabase;
import network.requests.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    MockDatabase db;
    JoinGameService service;

    public JoinGameHandler(MockDatabase db) {

        this.db = db;
        service = new JoinGameService(db);
    }

    public void joinGame(Request req, Response res, Gson gson) {
        var body = gson.fromJson(req.body(), JoinGameRequest.class);
        var joinRequest = new JoinGameRequest(req.headers("authorization"), body.playerColor(), body.gameID());
        service.joinGame(joinRequest);
    }
}
