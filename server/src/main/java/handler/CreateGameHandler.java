package handler;

import data_access.local_implementation.MockDatabase;

public class CreateGameHandler {
    MockDatabase db;
    public CreateGameHandler(MockDatabase db){
        this.db=db;
    }
}
