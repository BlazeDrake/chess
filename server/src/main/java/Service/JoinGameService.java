package Service;

import dataaccess.localImplementation.MockDatabase;

public class JoinGameService {
    MockDatabase db;
    public JoinGameService(MockDatabase db){
        this.db=db;
    }
}
