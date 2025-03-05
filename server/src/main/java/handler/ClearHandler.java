package handler;

import service.ClearService;
import dataaccess.DataAccessException;

import spark.Request;
import spark.Response;

public class ClearHandler {
    ClearService service;

    public ClearHandler() {
        service = new ClearService();
    }

    public String clear(Request req, Response res) throws DataAccessException {
        service.clear();
        return "{}";
    }
}
