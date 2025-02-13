package server;

import Handler.*;
import dataaccess.localImplementation.MockDatabase;
import spark.*;

public class Server {

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

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
