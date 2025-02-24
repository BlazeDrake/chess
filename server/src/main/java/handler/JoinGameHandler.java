package handler;

import data_access.local_implementation.MockDatabase;

public class JoinGameHandler {
    MockDatabase db;
    public JoinGameHandler(MockDatabase db){
        this.db=db;
    }
}
