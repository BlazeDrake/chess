package server;

import Handler.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.UnauthorizedException;
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
    RegisterHandler registerHandler;

    Gson gson;

    public Server(){
        testDB = new MockDatabase();
        clearHandler = new ClearHandler(testDB);
        registerHandler = new RegisterHandler(testDB);

        gson=new Gson();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerEndpoints();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerEndpoints() {
        Spark.post("/user",(req,res)->handleRequest(req,res,(reqIn,resIn)->registerHandler.register(reqIn,resIn,gson)));

        Spark.delete("/db",(req,res)->handleRequest(req,res,(reqIn,resIn)->clearHandler.clear(reqIn,resIn)));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRequest(Request req, Response res, RequestPredicate predicate){
        try{
            return predicate.handle(req,res);
        }
        catch(UnauthorizedException ex){
            res.status(401);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        }
        catch(TakenException ex){
            res.status(403);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        }
        catch(DataAccessException ex){
            res.status(500);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        }
    }
}
