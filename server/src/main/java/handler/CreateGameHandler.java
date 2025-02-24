package handler;

import dataaccess.localimplementation.MockDatabase;

public class CreateGameHandler {
    MockDatabase db;
    public CreateGameHandler(MockDatabase db){
        this.db=db;
    }
}
