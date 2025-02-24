package Handler;

import Service.LogoutService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.localImplementation.MockDatabase;
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
