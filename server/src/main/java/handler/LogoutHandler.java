package handler;

import service.LogoutService;
import dataaccess.DataAccessException;

import network.requests.LogoutRequest;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    LogoutService service;

    public LogoutHandler() {
        service = new LogoutService(db);
    }

    public String logout(Request req, Response res) throws DataAccessException {
        var logoutRequest = new LogoutRequest(req.headers("authorization"));
        service.logout(logoutRequest);
        return "{}";
    }
}
