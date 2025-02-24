package service;

import dataaccess.localimplementation.MockDatabase;

public class CreateGameService {
    MockDatabase db;
    public CreateGameService(MockDatabase db){
        this.db=db;
    }
}
