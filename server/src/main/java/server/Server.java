package server;

import dataaccess.BadRequestException;
import handler.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.UnauthorizedException;
import dataaccess.localimplementation.MockDatabase;
import network.ErrorResponse;
import spark.*;


public class Server {

    private static interface RequestPredicate {
        String handle(Request req, Response res) throws DataAccessException;
    }

    //ONLY FOR TESTING
    private MockDatabase testDB;

    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;

    private ListGamesHandler listGamesHandler;
    private JoinGameHandler joinGameHandler;
    private CreateGameHandler createGameHandler;

    private Gson gson;

    public Server() {
        testDB = new MockDatabase();
        clearHandler = new ClearHandler(testDB);
        registerHandler = new RegisterHandler(testDB);
        loginHandler = new LoginHandler(testDB);
        logoutHandler = new LogoutHandler(testDB);

        listGamesHandler = new ListGamesHandler(testDB);
        joinGameHandler = new JoinGameHandler(testDB);
        createGameHandler = new CreateGameHandler(testDB);

        gson = new Gson();
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
        Spark.post("/user", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> registerHandler.register(reqIn, resIn, gson)));
        Spark.post("/session", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> loginHandler.login(reqIn, resIn, gson)));
        Spark.delete("/session", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> logoutHandler.logout(reqIn, resIn)));

        Spark.get("/game", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> listGamesHandler.listGames(reqIn, resIn, gson)));
        Spark.post("/game", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> createGameHandler.createGame(reqIn, resIn, gson)));

        Spark.delete("/db", (req, res) -> handleRequest(req, res, (reqIn, resIn) -> clearHandler.clear(reqIn, resIn)));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRequest(Request req, Response res, RequestPredicate predicate) {
        try {
            return predicate.handle(req, res);
        } catch (BadRequestException ex) {
            res.status(400);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        } catch (UnauthorizedException ex) {
            res.status(401);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        } catch (TakenException ex) {
            res.status(403);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        } catch (DataAccessException ex) {
            res.status(500);
            var error = new ErrorResponse(ex.getMessage());
            return gson.toJson(error);
        }
    }
}
