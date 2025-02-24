package handler;

import service.RegisterService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import network.data_models.UserData;
import network.requests.RegisterRequest;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    MockDatabase db;
    RegisterService service;
    public RegisterHandler(MockDatabase db){

        this.db=db;
        service=new RegisterService(db);
    }

    public String register(Request req, Response res, Gson gson) throws DataAccessException{
        var userData=gson.fromJson(req.body(), UserData.class);
        var registerRequest=new RegisterRequest(userData);

        var result=service.register(registerRequest);
        return gson.toJson(result);

    }
}
