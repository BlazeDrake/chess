package service;

import dataaccess.localimplementation.MockDatabase;
import network.requests.JoinGameRequest;

public class JoinGameService {
    MockDatabase db;

    public JoinGameService(MockDatabase db) {
        this.db = db;
    }

    public void joinGame(JoinGameRequest request) {

    }
}
