package service;

import dataaccess.localimplementation.MockDatabase;

public class JoinGameService {
    MockDatabase db;
    public JoinGameService(MockDatabase db){
        this.db=db;
    }
}
