package service;

import data_access.local_implementation.MockDatabase;

public class CreateGameService {
    MockDatabase db;
    public CreateGameService(MockDatabase db){
        this.db=db;
    }
}
