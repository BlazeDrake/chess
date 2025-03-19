package server;

import com.google.gson.Gson;
import network.datamodels.UserData;
import network.requests.*;
import network.results.*;

import java.io.*;
import java.net.*;
import java.util.List;


public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    //server methods
    //FIXME: move handleError to the CLI handler, to localize printing errors. Put try catch around each run
    private void handleError(ResponseException ex) {
        String msg = switch (ex.StatusCode()) {
            case 400 -> "Error: bad command. Double check it.";
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Internal error";
        };
        System.out.println(msg);
    }

    public void register(UserData req) throws ResponseException {
        makeRequest("POST", "/user", req, RegisterResult.class, null);
    }

    public LoginResult login(UserData req) throws ResponseException {
        return makeRequest("POST", "/session", req, LoginResult.class, null);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ResponseException {
        return makeRequest("GET", "/game", null, ListGamesResult.class, req.authToken());
    }

    public void createGame(CreateGameRequest req) throws ResponseException {
        makeRequest("POST", "/game", req, CreateGameRequest.class, req.authToken());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeAuth(authToken, http);
            writeBody(request, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeAuth(String authToken, HttpURLConnection http) {
        if (authToken != null) {
            http.setRequestProperty("authorization", authToken);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr, status);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
