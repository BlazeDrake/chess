package server;

import Handler.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.localImplementation.MockDatabase;
import network.ErrorResponse;
import spark.*;


public class Server {

    private static interface RequestPredicate{
        String handle(Request req, Response res) throws DataAccessException;
    }
    //ONLY FOR TESTING
    MockDatabase testDB;

    ClearHandler clearHandler;

    public Server(){
        testDB = new MockDatabase();
        clearHandler = new ClearHandler(testDB);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db",(req,res)->handleRequest(req,res,(reqIn,resIn)->clearHandler.clear(reqIn,resIn)));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRequest(Request req, Response res, RequestPredicate predicate){
        try{
            return predicate.handle(req,res);
        }
        catch(DataAccessException ex){
            var serializer=new Gson();
            res.status(500);
            var error = new ErrorResponse(ex.getMessage());
            return serializer.toJson(error);
        }
    }
}
