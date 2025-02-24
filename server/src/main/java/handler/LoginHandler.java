package handler;

import service.LoginService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import network.requests.LoginRequest;
import spark.Request;
import spark.Response;

public class LoginHandler {
    MockDatabase db;
    LoginService service;
    public LoginHandler(MockDatabase db){

        this.db=db;
        service=new LoginService(db);
    }
    public String login(Request req, Response res, Gson gson) throws DataAccessException {
        var loginRequest=gson.fromJson(req.body(), LoginRequest.class);
        var result=service.login(loginRequest);
        return gson.toJson(result);
    }
}
