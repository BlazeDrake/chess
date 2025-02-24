package service;

import data_access.local_implementation.MockDatabase;

public class JoinGameService {
    MockDatabase db;
    public JoinGameService(MockDatabase db){
        this.db=db;
    }
}
