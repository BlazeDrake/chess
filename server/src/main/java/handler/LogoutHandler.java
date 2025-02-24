package handler;

import service.LogoutService;
import data_access.DataAccessException;
import data_access.local_implementation.MockDatabase;
import network.requests.LogoutRequest;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    MockDatabase db;
    LogoutService service;
    public LogoutHandler(MockDatabase db){

        this.db=db;
        service = new LogoutService(db);
    }
    public String logout(Request req, Response res) throws DataAccessException {
        var logoutRequest=new LogoutRequest(req.headers("authorization"));
        service.logout(logoutRequest);
        return "{}";
    }
}
