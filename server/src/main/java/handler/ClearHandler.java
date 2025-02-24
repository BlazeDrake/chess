package handler;

import service.ClearService;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import spark.Request;
import spark.Response;

public class ClearHandler {
    MockDatabase db;
    ClearService service;
    public ClearHandler(MockDatabase db){
        this.db=db;
        service = new ClearService(db);
    }

    public String clear(Request req, Response res) throws DataAccessException{
        service.clear();
        return "{}";
    }
}
